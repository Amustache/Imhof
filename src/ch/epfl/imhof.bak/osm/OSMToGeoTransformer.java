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
import ch.epfl.imhof.geometry.OpenPolyLine;
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

        // Init du mapBuilder
        Map.Builder mapBuilder = new Map.Builder();

        // declarations necaissaires
        List<OSMWay> osmWayList = map.ways();
        List<OSMRelation> osmRelationList = map.relations();
        List<Attributed<Polygon>> attributedPolygonList;
        Attributes att;
        final HashSet<String> keysToKeepPolyLine = new HashSet<String>(Arrays.asList("bridge", "highway",
                "layer", "man_made", "railway", "tunnel", "waterway"));
        final HashSet<String> keysToKeepPolygon = new HashSet<String>(Arrays.asList("building",
                "landuse", "layer", "leisure", "natural", "waterway"));


        // tri des attributs et convertion des OSMWay
        for (int i = 0; i < osmWayList.size(); ++i) {
            if (isPolygon(osmWayList.get(i))) {
                //tri des attributs
                att = osmWayList.get(i).attributes().keepOnlyKeys(keysToKeepPolygon);
                if (!att.isEmpty()) {
                    //conversion en polygon
                    mapBuilder.addPolygon(wayToPolygon(osmWayList.get(i), att));
                }
            } else {
                att = osmWayList.get(i).attributes().keepOnlyKeys(keysToKeepPolyLine);
                if (!att.isEmpty()) {
                    //conversionen PolyLine
                    mapBuilder.addPolyLine(wayToPolyLine(osmWayList.get(i), att));
                }
            }
        }

        // tri des attributs et convertion des OSMRelation
        for (int i = 0; i < osmRelationList.size(); ++i) {
            //tri des attributs
            att = osmRelationList.get(i).attributes().keepOnlyKeys(keysToKeepPolygon);
            if (!att.isEmpty()) {
                //convertion
                attributedPolygonList = assemblePolygon(osmRelationList.get(i), att);

                for (int j = 0; j < attributedPolygonList.size(); ++j) {
                    mapBuilder.addPolygon(attributedPolygonList.get(j));
                }
            }
        }
        return mapBuilder.build();
    }

    /**
     * @param way
     * @return retourne true ssi l OSMWay doit etre consideree comme un polygon
     */
    private boolean isPolygon(OSMWay way) {
        ArrayList<String> keysToKeep;

        if (way.isClosed()) {
            if (way.attributeValue("area") != null) {
                if (way.attributeValue("area").equals("1")
                        || way.attributeValue("area").equals("yes")
                        || way.attributeValue("area").equals("true")) {
                    return true;
                }
            }
            keysToKeep = new ArrayList<String>(Arrays.asList("aeroway", "amenity", "building", "harbour", "historic",
                    "landuse", "leisure", "man_made", "military", "natural",
                    "office", "place", "power", "public_transport", "shop",
                    "sport", "tourism", "water", "waterway", "wetland"));
            for (int i = 0; i < keysToKeep.size(); ++i) {
                if (way.hasAttribute(keysToKeep.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param way
     * @param attributes
     * @return retourne une polyLine attribuee avec les attributs en parametres
     */
    private Attributed<PolyLine> wayToPolyLine(OSMWay way, Attributes attributes) {
        List<OSMNode> osmNodeList = way.nonRepeatingNodes();
        PolyLine.Builder polyLineBuilder = new PolyLine.Builder();

        for (int i = 0; i < osmNodeList.size(); ++i) {
            polyLineBuilder.addPoint(projection.project(osmNodeList.get(i).position()));
        }

        return new Attributed<PolyLine>(polyLineBuilder.buildOpen(), attributes);
    }

    /**
     * @param way
     * @param attributes
     * @return retourne un polygon attribue avec les attributs en parametres
     */
    private Attributed<Polygon> wayToPolygon(OSMWay way, Attributes attributes) {
        List<OSMNode> osmNodeList = way.nonRepeatingNodes();
        PolyLine.Builder polyLineBuilder = new PolyLine.Builder();

        for (int i = 0; i < osmNodeList.size(); ++i) {
            polyLineBuilder.addPoint(projection.project(osmNodeList.get(i).position()));
        }
        
        return new Attributed<Polygon>(new Polygon(polyLineBuilder.buildClosed()), attributes);
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

        List<OSMWay> ways = new ArrayList<OSMWay>();

        // On trie les OSMWay avec le role demandé
        for (Member member : relation.members()) {
            if (member.type().equals(Member.Type.WAY)
                    && member.role().equals(role)) {
                ways.add((OSMWay) member.member());
            }
        }

        for (OSMWay osmWay : ways) {
            graphBuilder.addNode(osmWay.firstNode());
            for (int i = 1; i < osmWay.nodesCount(); ++i) {
                graphBuilder.addNode(osmWay.nodes().get(i));
                graphBuilder.addEdge(osmWay.nodes().get(i),
                        osmWay.nodes().get(i - 1));
            }
        }

        // On construit le graphe non oriente
        Graph<OSMNode> graph = graphBuilder.build();

        // Si un noeud n'a pas exactement 2 voisins, on retourne une liste vide
        for (OSMNode osmNode : graph.nodes()) {
            if (graph.neighborsOf(osmNode).size() != 2) {
                return new ArrayList<ClosedPolyLine>();
            }
        }

        // On construit un set avec les noeuds non visites
        Set<OSMNode> nonVisited = new HashSet<OSMNode>(graph.nodes());
        List<Point> points = new ArrayList<Point>();

        // Liste des polylines
        List<ClosedPolyLine> polylines = new ArrayList<ClosedPolyLine>();

        OSMNode current = null, currentNeigh1 = null, currentNeigh2 = null;
        Iterator<OSMNode> iterNeigh = null;

        // Tant qu'un noeud n'a pas ete visite
        while (nonVisited.iterator().hasNext()) {
            // On vide la liste des points
            points.clear();

            // On selectionne un noeud
            current = nonVisited.iterator().next();

            // On l'ajoute a la liste des points - c'est le premier noeud - puis
            // on le degage de la liste des non visites
            points.add(projection.project(current.position()));
            nonVisited.remove(current);

            // Il y a encore des points a visiter ?
            boolean more = true;

            // On visite les voisins
            while (more) {
                more = false;

                iterNeigh = graph.neighborsOf(current).iterator();

                // On selectionne le voisin 1
                currentNeigh1 = iterNeigh.next();

                // A-t-il ete visite ?
                if (nonVisited.contains(currentNeigh1)) {
                    // On l'ajoute a la liste des points
                    points.add(projection.project(currentNeigh1.position()));

                    // Il est desormais visite
                    nonVisited.remove(currentNeigh1);

                    // On le met en nouveau current
                    current = currentNeigh1;

                    // On a encore des points a visiter
                    more = true;
                } else {
                    // On selectionne le voisin 2
                    currentNeigh2 = iterNeigh.next();

                    // A-t-il ete visite ?
                    if (nonVisited.contains(currentNeigh2)) {
                        // On l'ajoute a la liste des points
                        points.add(projection.project(currentNeigh2.position()));

                        // Il est desormais visite
                        nonVisited.remove(currentNeigh2);

                        // On le met en nouveau current
                        current = currentNeigh2;

                        // On a encore des points a visiter
                        more = true;
                    }
                }
            }

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

        ArrayList<Attributed<Polygon>> attributedPolygon = new ArrayList<Attributed<Polygon>>();
        // System.out.println("CALLED OUTER");
        List<ClosedPolyLine> outers = this.ringsForRole(relation, "outer");
        // System.out.println("CALLED INNER");
        List<ClosedPolyLine> inners = this.ringsForRole(relation, "inner");

        // mis en ordres du plus petit au plus grand des ClosedPolyLine
        CompareClosedPolyLine c = new CompareClosedPolyLine();
        Collections.sort(outers, c);
        Collections.sort(inners, c);

        // declaration des tableaux qui nous servirons pour contenir les polygon
        // et les trous
        ArrayList<ClosedPolyLine> holes = new ArrayList<>();
        ArrayList<Polygon> polygon = new ArrayList<>();

        // creation de tout les polygon avec leurs trou
        for (int i = 0; i < outers.size(); ++i) {
            for (int j = 0; j < inners.size(); ++j) {
                if (outers.get(i).containsPoint(inners.get(j).firstPoint())) {
                    holes.add(inners.get(j));
                    inners.remove(j);
                    j--;
                } // -455028.0327093767,-5534970.668373143)
            }
            polygon.add(new Polygon(outers.get(i), holes));
            holes.clear();
        }

        // attribution des polygons
        for (int i = 0; i < polygon.size(); ++i) {
            attributedPolygon.add(new Attributed<Polygon>(polygon.get(i),
                    attributes));
        }
        return attributedPolygon;
    }

    // comparateur pour ordrer les ClosedPolyLine par aire croissante
    private class CompareClosedPolyLine implements Comparator<ClosedPolyLine> {

        @Override
        public int compare(ClosedPolyLine o1, ClosedPolyLine o2) {
            if (o1.area() > o2.area()) {
                return 1;
            } else if (o1.area() < o2.area()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
