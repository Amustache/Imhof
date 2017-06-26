package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Carte projetee, composee d'entites geometriques attribuees
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * Construit une carte a partir des listes de polylignes et polygones
     * attribues donnes
     * 
     * @param polyLines
     *            Liste de polylignes
     * @param polygons
     *            Liste de polygones
     */
    public Map(List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections
                .unmodifiableList(new ArrayList<Attributed<PolyLine>>(polyLines));
        this.polygons = Collections
                .unmodifiableList(new ArrayList<Attributed<Polygon>>(polygons));
    }

    /**
     * Retourne la liste des polylignes attribuees de la carte
     * 
     * @return Liste des polylignes
     */
    public List<Attributed<PolyLine>> polyLines() {
        return new ArrayList<Attributed<PolyLine>>(this.polyLines);
    }

    /**
     * Retourne la liste des polygones attribues de la carte
     * 
     * @return Liste des polygones
     */
    public List<Attributed<Polygon>> polygons() {
        return new ArrayList<Attributed<Polygon>>(this.polygons);
    }

    public static class Builder {
        private List<Attributed<PolyLine>> polyLinesBuilder;
        private List<Attributed<Polygon>> polygonsBuilder;

        
        /**
         * construit un Map.Builder
         */
        public Builder(){
            this.polyLinesBuilder = new ArrayList<Attributed<PolyLine>>();
            this.polygonsBuilder = new ArrayList<Attributed<Polygon>>();
        }
        
        
        /**
         * Ajoute une polyligne attribuee a la carte en cours de construction
         * 
         * @param newPolyLine
         *            La polyligne a ajouter
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            this.polyLinesBuilder.add(newPolyLine);
        }

        /**
         * Ajoute un polygone attribue a la carte en cours de construction
         * 
         * @param newPolygon
         *            Le polygone a ajouter
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            this.polygonsBuilder.add(newPolygon);
        }

        /**
         * Construit une carte avec les polylignes et polygones ajoutes jusqu'a
         * present au batisseur
         * 
         * @return La carte construite
         */
        public Map build() {
            return new Map(
                    new ArrayList<Attributed<PolyLine>>(polyLinesBuilder),
                    new ArrayList<Attributed<Polygon>>(polygonsBuilder));
        }
    }
}
