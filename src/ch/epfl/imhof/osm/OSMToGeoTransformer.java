package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.projection.Projection;

/**
 * Convertisseur de donnees OSM en carte
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class OSMToGeoTransformer {
    private final Projection projection;

    // Filtres
    private final ArrayList<String> keysToKeep = new ArrayList<String>(
            Arrays.asList("aeroway", "amenity", "building", "harbour",
                    "historic", "landuse", "leisure", "man_made", "military",
                    "natural", "office", "place", "power", "public_transport",
                    "shop", "sport", "tourism", "water", "waterway", "wetland"));

    private final HashSet<String> keysToKeepPolyLine = new HashSet<String>(
            Arrays.asList("bridge", "highway", "layer", "man_made", "railway",
                    "tunnel", "waterway"));

    private final HashSet<String> keysToKeepPolygon = new HashSet<String>(
            Arrays.asList("building", "landuse", "layer", "leisure", "natural",
                    "waterway"));

    /**
     * Construit un convertisseur OSM en geometrie qui utilise la projection
     * donnee
     * 
     * @param projection
     *            Projection a utiliser
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * Convertit une map OSM en une carte geometrique projetee OSMWay.isClosed
     * => Polygon !OSMWay.isClosed => PolyLine
     * 
     * @param map
     *            La map OSM
     * @return La carte construite
     */
    public Map transform(OSMMap map) {

        // Initialisation du mapBuilder
        Map.Builder mapBuilder = new Map.Builder();

        Attributes att;

        // Tri des attributs et convertion des OSMWay
        for (OSMWay way : map.ways()) {
            if (isPolygon(way)) {
                // Tri des attributs
                att = way.attributes().keepOnlyKeys(keysToKeepPolygon);
                if (!att.isEmpty()) {
                    // Conversion en Polygon
                    mapBuilder.addPolygon(wayToPolygon(way, att));
                }
            } else {
                att = way.attributes().keepOnlyKeys(keysToKeepPolyLine);
                if (!att.isEmpty()) {
                    // Conversion en PolyLine
                    mapBuilder.addPolyLine(wayToPolyLine(way, att));
                }
            }
        }

        // Tri des attributs et convertion des OSMRelation
        for (OSMRelation relation : map.relations()) {
            // Tri des attributs
            att = relation.attributes().keepOnlyKeys(keysToKeepPolygon);
            if (!att.isEmpty()) {
                // Conversion
                for (Attributed<Polygon> attributed : assemblePolygon(relation,
                        att)) {
                    mapBuilder.addPolygon(attributed);
                }
            }
        }

        return mapBuilder.build();
    }

    /**
     * @param way
     *            La OSMWay a verifier
     * @return True ssi l'OSMWay doit etre consideree comme un polygon
     */
    private boolean isPolygon(OSMWay way) {
        if (way.isClosed()) {
            if (way.attributeValue("area") != null) {
                if (way.attributeValue("area").equals("1")
                        || way.attributeValue("area").equals("yes")
                        || way.attributeValue("area").equals("true")) {
                    return true;
                }
            }

            for (String key : keysToKeep) {
                if (way.hasAttribute(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Converti une OSMWay en PolyLine, Closed ou Open selon le cas
     * 
     * @param way
     *            La OSMWay a convertir
     * @return PolyLine correspondante
     */
    private PolyLine wayConverter(OSMWay way) {
        PolyLine.Builder polyLineBuilder = new PolyLine.Builder();

        for (OSMNode node : way.nonRepeatingNodes()) {
            polyLineBuilder.addPoint(projection.project(node.position()));
        }

        return way.isClosed() ? polyLineBuilder.buildClosed() : polyLineBuilder
                .buildOpen();
    }

    /**
     * @param way
     *            La OSMWay a attribuer
     * @param attributes
     *            Les attributs associes
     * @return Une polyLine attribuee avec les attributs en parametres
     */
    private Attributed<PolyLine> wayToPolyLine(OSMWay way, Attributes attributes) {
        return new Attributed<PolyLine>(wayConverter(way), attributes);
    }

    /**
     * @param way
     *            La OSMWay a attribuer
     * @param attributes
     *            Les attributs associes
     * @return Un polygon attribue avec les attributs en parametres
     */
    private Attributed<Polygon> wayToPolygon(OSMWay way, Attributes attributes) {
        return new Attributed<Polygon>(new Polygon(
                (ClosedPolyLine) wayConverter(way)), attributes);
    }

    /**
     * Calcule et retourne l'ensemble des anneaux de la relation donnee ayant le
     * role specifie
     *
     * @param relation
     *            La relation a analyser
     * @param role
     *            Le role choisi (inner/outer)
     * @return La liste des ClosedPolylines
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<OSMNode>();

        // On trie les OSMWay avec le role demandé
        for (Member member : relation.members()) {
            if (member.type().equals(Member.Type.WAY)
                    && member.role().equals(role)) {
                OSMWay way = (OSMWay) member.member();
                graphBuilder.addNode(way.firstNode());
                for (int i = 1; i < way.nodesCount(); ++i) {
                    graphBuilder.addNode(way.nodes().get(i));
                    graphBuilder.addEdge(way.nodes().get(i),
                            way.nodes().get(i - 1));
                }
            }
        }

        // On construit le graphe non oriente
        Graph<OSMNode> graph = graphBuilder.build();

        // Si un noeud n'a pas exactement 2 voisins, on retourne une liste vide
        for (OSMNode osmNode : graph.nodes()) {
            if (graph.neighborsOf(osmNode).size() != 2) {
                return Collections.emptyList();
            }
        }

        // On construit un set avec les noeuds non visites
        Set<OSMNode> nonVisited = new HashSet<OSMNode>(graph.nodes());
        List<Point> points = new ArrayList<Point>();

        // Liste des polylines
        List<ClosedPolyLine> polylines = new ArrayList<ClosedPolyLine>();

        // Tant qu'un noeud n'a pas ete visite
        while (nonVisited.iterator().hasNext()) {
            // On vide la liste de points
            points.clear();

            // On selectionne un noeud
            OSMNode current = nonVisited.iterator().next();

            // On l'ajoute a la liste de points - c'est le premier noeud
            points.add(projection.project(current.position()));

            // On l'enleve de la liste des non visites
            nonVisited.remove(current);

            // On initialise la liste des voisins
            Set<OSMNode> neighz;

            do {
                // On cree une liste des voisins non visistes
                neighz = new HashSet<OSMNode>(nonVisited);
                neighz.retainAll(graph.neighborsOf(current));

                // S'il existe un voisin non visite
                if (neighz.iterator().hasNext()) {

                    // On prend un voisin non visite
                    OSMNode currentNeigh = neighz.iterator().next();

                    // On l'ajoute a la liste des points
                    points.add(projection.project(currentNeigh.position()));

                    // On l'enleve de la liste des non visites
                    nonVisited.remove(currentNeigh);

                    // Il s'agit du nouveau point de reference
                    current = currentNeigh;
                }

                // On recommence tant qu'il reste un voisin non visite
            } while (neighz.iterator().hasNext());

            // On ajoute la polyline ainsi creee
            polylines.add(new ClosedPolyLine(points));
        }

        return polylines;
    }

    /**
     * Calcule et retourne la liste des polygones attribues de la relation
     * donnee, en leur attachant les attributs donnes.
     * 
     * @param relation
     *            La relation a analyser
     * @param attributes
     *            Les attributs a attacher
     * @return Une liste de Polygons avec leurs attributs
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {

        List<ClosedPolyLine> outers = this.ringsForRole(relation, "outer");
        List<ClosedPolyLine> inners = this.ringsForRole(relation, "inner");

        // Mise en ordre du plus petit au plus grand des ClosedPolyLine
        Comparator<ClosedPolyLine> c = new CompareClosedPolyLine();
        Collections.sort(outers, c);
        Collections.sort(inners, c);

        // Declaration des tableaux qui nous servirons pour contenir les Polygon
        // et les trous
        ArrayList<ClosedPolyLine> holes = new ArrayList<>();
        ArrayList<Polygon> polygons = new ArrayList<>();

        // Creation de tout les Polygon avec leurs trous
        for (ClosedPolyLine outer : outers) {
            Iterator<ClosedPolyLine> itInnerz = inners.iterator();
            while (itInnerz.hasNext()) {
                ClosedPolyLine inner = (ClosedPolyLine) itInnerz.next();

                if (inner.area() > outer.area()) {
                    break;
                }

                if (outer.containsPoint(inner.firstPoint())) {
                    holes.add(inner);
                    itInnerz.remove();
                }
            }

            polygons.add(new Polygon(outer, holes));
            holes.clear();
        }

        // Attribution des Polygon
        ArrayList<Attributed<Polygon>> attributedPolygon = new ArrayList<Attributed<Polygon>>();
        for (Polygon polygon : polygons) {
            attributedPolygon.add(new Attributed<Polygon>(polygon, attributes));
        }

        return attributedPolygon;
    }

    // Comparateur pour ordrer les ClosedPolyLine par aire croissante
    private class CompareClosedPolyLine implements Comparator<ClosedPolyLine> {

        @Override
        public int compare(ClosedPolyLine o1, ClosedPolyLine o2) {
            return Double.compare(o1.area(), o2.area());
        }
    }
}
