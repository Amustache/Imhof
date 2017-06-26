package ch.epfl.imhof;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.Collections.unmodifiableMap;

/**
 * Graphe non oriente
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 * @param <N>
 *            Type des noeuds du graphe
 */
public final class Graph<N> {
    private final Map<N, Set<N>> neighbors;

    /**
     * Construit un Graph
     * 
     * @param neighbors
     *            Table d'adjacence du graphe
     */
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> view = new HashMap<N, Set<N>>();

        for (N object : neighbors.keySet()) {
            view.put(object,
                    unmodifiableSet(new HashSet<N>(neighbors.get(object))));
        }

        this.neighbors = unmodifiableMap(new HashMap<>(view));
    }

    /**
     * 
     * @return L'ensemble des noeuds du graphe
     */
    public Set<N> nodes() {
        return this.neighbors.keySet();
    }

    /**
     * Retourne un Set contenant les voisins du noeud donne
     * 
     * @param node
     *            Noeud donne
     * @return L'ensemble des voisins du noeud donne
     * @throws IllegalArgumentException
     *             Sssi le noeud donne ne fait pas parti du graphe
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!this.neighbors.containsKey(node)) {
            throw new IllegalArgumentException();
        } else {
            return this.neighbors.get(node);
        }
    }

    /**
     * Batisseur generique de la classe Graph
     * 
     * @author Hugo Hueber (246095)
     * @author Maxime Pisa (247650)
     *
     */
    public static final class Builder<N> {
        private Map<N, Set<N>> neighbors;

        public Builder() {
            this.neighbors = new HashMap<N, Set<N>>();
        }

        /**
         * Ajoute le noeud donne au graphe s'il n'en faisait pas parti
         * 
         * @param n
         *            Le noeud a ajouter
         */
        public void addNode(N n) {
            if (!this.neighbors.containsKey(n)) {
                this.neighbors.put(n, new HashSet<N>());
            }
        }

        /**
         * Ajoute une arrete entre deux noeuds
         * 
         * @param n1
         *            Noeud 1
         * @param n2
         *            Noeud 2
         * @throws IllegalArgumentException
         *             Sssi l'un des noeuds n'existe pas
         */
        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!this.neighbors.containsKey(n1)
                    || !this.neighbors.containsKey(n2)) {
                throw new IllegalArgumentException();
            } else {
                this.neighbors.get(n1).add(n2);
                this.neighbors.get(n2).add(n1);
            }
        }

        /**
         * Construit le graphe actuellement bati
         * 
         * @return Le graphe construit
         */
        public Graph<N> build() {
            return new Graph<N>(neighbors);
        }
    }
}
