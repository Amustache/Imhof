/*
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 */

package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.imhof.Attributes;

import static java.util.Collections.unmodifiableList;

/**
 * Representation d'un chemin OSM
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class OSMWay extends OSMEntity {

    private final List<OSMNode> nodes;

    /**
     * @param id
     *            L'identifiant unique
     * @param nodes
     *            La liste des noeuds
     * @param attributes
     *            La liste des attributs
     * @throws IllegalArgumentException
     *             Sssi la liste de noeuds possède moins de deux éléments
     * 
     *             Construit un chemin étant donnés son identifiant unique, ses
     *             noeuds et ses attributs.
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
            throws IllegalArgumentException {
        super(id, attributes);
        if (nodes.size() < 2) {
            throw new IllegalArgumentException(
                    "L'OSMWay contient moins de deux noeuds");
        } else {
            this.nodes = unmodifiableList(new ArrayList<OSMNode>(nodes));
        }
    }

    /**
     * @return Retourne le nombre de noeuds du chemin
     */
    public int nodesCount() {
        return this.nodes.size();
    }

    /**
     * @return Retourne la liste des noeuds du chemin
     */
    public List<OSMNode> nodes() {
        return unmodifiableList(new ArrayList<OSMNode>(this.nodes));
    }

    /**
     * @return Retourne la liste des noeuds du chemin sans le dernier si
     *         celui-ci est identique au premier
     */
    public List<OSMNode> nonRepeatingNodes() {
        if (this.nodes.get(0).equals(this.nodes.get(this.nodesCount() - 1))) {
            ArrayList<OSMNode> withoutRepeating = new ArrayList<OSMNode>(
                    this.nodes);
            withoutRepeating.remove(withoutRepeating.size() - 1);
            return unmodifiableList(new ArrayList<OSMNode>(withoutRepeating));
        } else {
            return unmodifiableList(new ArrayList<OSMNode>(this.nodes));
        }
    }

    /**
     * @return Retourne le premier noeud du chemin
     */
    public OSMNode firstNode() {
        return this.nodes.get(0);
    }

    /**
     * @return Retourne le dernier noeud du chemin
     */
    public OSMNode lastNode() {
        return this.nodes.get(this.nodesCount() - 1);
    }

    /**
     * @return Retourne vrai si et seulement si le chemin est fermé, c-à-d que
     *         la méthode equals du premier noeud considère que le dernier noeud
     *         lui est égal
     */
    public boolean isClosed() {
        return this.firstNode().equals(this.lastNode());
    }

    /**
     * Batisseur d'OSMWay
     * 
     * @author Hugo Hueber (246095)
     * @author Maxime Pisa (247650)
     *
     */
    public final static class Builder extends OSMEntity.Builder {

        private final List<OSMNode> nodes;

        /**
         * @param id
         *            Identifiant unique
         * 
         *            Construit un bâtisseur pour un chemin ayant l'identifiant
         *            donné
         */
        public Builder(long id) {
            super(id);
            this.nodes = new ArrayList<OSMNode>();
        }

        /**
         * 
         * @return Vrai sssi le chemin en cours de construction est incomplete,
         *         a savoir qu'il contient moins de deux noeuds
         */
        @Override
        public boolean isIncomplete() {
            if (this.nodes.size() < 2) {
                return true;
            } else {
                return super.isIncomplete();
            }
        }

        /**
         * @param newNode
         *            Le noeud a ajouter
         * 
         *            Ajoute un noeud à (la fin) des noeuds du chemin en cours
         *            de construction
         */
        public void addNode(OSMNode newNode) {
            this.nodes.add(newNode);
        }

        /*
         * (non-Javadoc)
         * 
         * @see ch.epfl.imhof.osm.OSMEntity.Builder#build()
         * 
         * Construit et retourne le chemin ayant les noeuds et les attributs
         * ajoutés jusqu'à présent. Lève l'exception IllegalStateException si le
         * chemin en cours de construction est incomplet
         */
        public OSMWay build() throws IllegalStateException {
            if (this.isIncomplete()) {
                throw new IllegalStateException("OSMWay incomplete");
            } else {
                return new OSMWay(this.getId(), this.nodes, this.getBuilder()
                        .build());
            }
        }

    }

}
