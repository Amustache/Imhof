package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * Represente une entite OSM.
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public abstract class OSMEntity {
    private final long id;
    private final Attributes attributes;

    /**
     * Construit une nouvelle OSMEntity
     * 
     * @param id
     *            Identifiant unique
     * @param attributes
     *            Attributs donnes
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * 
     * @return Identifiant unique
     */
    public long id() {
        return this.id;
    }

    /**
     * 
     * @return Attributes de l'entite
     */
    public Attributes attributes() {
        return this.attributes;
    }

    /**
     * 
     * @param key
     *            Cle a tester
     * @return Vrai sssi l'entite possede la cle passee en argument
     */
    public boolean hasAttribute(String key) {
        if (this.attributes().contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @param key
     *            La cle a tester
     * @return La valeur correspondate a la cle
     */
    public String attributeValue(String key) {
        if (this.hasAttribute(key)) {
            return this.attributes().get(key);
        } else {
            return null;
        }
    }

    /**
     * Classe mere de toutes les classes batisseur d'entite OSMEntity
     * 
     * @author Hugo Hueber (246095)
     * @author Maxime Pisa (247650)
     *
     */
    public static abstract class Builder {
        private long id;
        private Attributes.Builder builder;
        private boolean incomplete;

        /**
         * 
         * @param id
         *            Identifiant unique
         */
        public Builder(long id) {
            this.id = id;
            this.builder = new Attributes.Builder();
            this.incomplete = false;
        }

        /**
         * Ajoute l'association (clef,valeur) a l'ensemble d'attributs
         * 
         * @param key
         *            La cle
         * @param value
         *            La valeur correspondante
         */
        public void setAttribute(String key, String value) {
            this.builder.put(key, value);
        }

        /**
         * Declare l'entite en cours de construction incomplete
         */
        public void setIncomplete() {
            this.incomplete = true;
        }

        /**
         * 
         * @return Vrai sssi l'entite en cours de construction est incomplete
         */
        public boolean isIncomplete() {
            return this.incomplete;
        }

        /**
         * 
         * @return Identifiant unique
         */
        public long getId() {
            return this.id;
        }

        /**
         * 
         * @return Ce builder
         */
        public Attributes.Builder getBuilder() {
            return this.builder;
        }

        /**
         * 
         * @return OSMEntity construite
         */
        public abstract OSMEntity build();
    }
}
