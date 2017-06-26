package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;

/**
 * Representation d'une carte OpenStreetMap, a savoir un ensemble de chemins et
 * de relations
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class OSMMap {

    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * @param ways
     *            La liste des chemins
     * @param relations
     *            La liste des relations
     *
     *            Construit une carte OSM avec les chemins et les relations
     *            donnés
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = unmodifiableList(new ArrayList<OSMWay>(ways));
        this.relations = unmodifiableList(new ArrayList<OSMRelation>(relations));
    }

    /**
     * @return Retourne la liste des chemins de la carte
     */
    public List<OSMWay> ways() {
        return this.ways;
    }

    /**
     * @return Retourne la liste des relations de la carte
     */
    public List<OSMRelation> relations() {
        return this.relations;
    }

    public final static class Builder {

        private Map<Long, OSMWay> ways;
        private Map<Long, OSMRelation> relations;
        private Map<Long, OSMNode> nodes;

        /**
         * Constructeur du Builder
         */
        public Builder() {
            this.ways = new HashMap<Long, OSMWay>();
            this.relations = new HashMap<Long, OSMRelation>();
            this.nodes = new HashMap<Long, OSMNode>();
        }

        /**
         * @param newNode
         *            Le Noeud a ajotuer
         *
         *            Ajoute le noeud donne au batisseur
         */
        public void addNode(OSMNode newNode) {
            this.nodes.put(newNode.id(), newNode);
        }

        /**
         * @param id
         *            Identifiant du noeud
         * @return Retourne le noeud dont l'identifiant unique est egal à celui
         *         donne, ou null si ce noeud n'a pas ete ajoute precedemment au
         *         batisseur
         */
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
        }

        /**
         * @param newWay
         *            Le nouveau chemin a ajouter
         * 
         *            Ajoute le chemin donne a la carte en cours de construction
         */
        public void addWay(OSMWay newWay) {
            this.ways.put(newWay.id(), newWay);
        }

        /**
         * @param id
         *            Identifiant du chemin a retourner
         * @return Retourne le chemin dont l'identifiant unique est egal a celui
         *         donne, ou null si ce chemin n'a pas ete ajoute precedemment
         *         au batisseur
         */
        public OSMWay wayForId(long id) {
            return ways.get(id);
        }

        /**
         * @param newRelation
         *            La relation a ajouter
         *
         *            Ajoute la relation donnée à la carte en cours de
         *            construction
         */
        public void addRelation(OSMRelation newRelation) {
            this.relations.put(newRelation.id(), newRelation);
        }

        /**
         * @param id
         *            L'identifiant de la relation
         * @return Retourne la relation dont l'identifiant unique est egal a
         *         celui donne, ou null si cette relation n'a pas ete ajoutee
         *         precedemment au batisseur
         */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        /**
         * @return Construit une carte OSM avec les chemins et les relations
         *         ajoutes jusqu'a present
         */
        public OSMMap build() {
            return new OSMMap(new ArrayList<OSMWay>(this.ways.values()),
                    new ArrayList<OSMRelation>(this.relations.values()));
        }
    }
}