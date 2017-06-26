package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * Represente un noeud OSM
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class OSMNode extends OSMEntity {

    private final PointGeo position;

    /**
     * @param id
     *            Identifiant unique
     * @param position
     *            La position du noeud
     * @param attributes
     *            Les attributs donnes
     *
     *            Construit un noeud OSM avec l'identifiant, la position et les
     *            attributs donnes
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * @return La position du noeud
     */
    public PointGeo position() {
        return this.position;
    }

    /**
     * Batisseur d'OSMNode
     * 
     * @author Hugo Hueber (246095)
     * @author Maxime Pisa (247650)
     *
     */
    public static final class Builder extends OSMEntity.Builder {

        private final PointGeo position;

        /**
         * @param id
         *            Identifiant unique
         * @param position
         *            Position donnee
         *
         *            Construit un batisseur pour un noeud ayant l'identifiant
         *            et la position donnes
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /*
         * (non-Javadoc)
         * 
         * @see ch.epfl.imhof.osm.OSMEntity.Builder#build()
         * 
         * Construit un noeud OSM avec l'identifiant et la position passes au
         * constructeur, et les eventuels attributs ajoutes jusqu'ici au
         * batisseur. Leve l'exception IllegalStateException si le noeud en
         * cours de construction est incomplet, c-a-d si la methode
         * setIncomplete a ete appelee sur ce batisseur depuis sa creation
         */
        public OSMNode build() throws IllegalStateException {
            if (this.isIncomplete()) {
                throw new IllegalStateException("OSMNode incomplet");
            } else {
                return new OSMNode(this.getId(), this.position, this
                        .getBuilder().build());
            }
        }
    }
}