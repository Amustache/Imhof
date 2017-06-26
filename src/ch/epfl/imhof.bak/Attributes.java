package ch.epfl.imhof;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableMap;

/**
 * Table associative d'un ensemble d'attributs et de leurs clés.
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class Attributes {
    private final Map<String, String> attributes;

    /**
     * Construit une nouvelle instance d'Attributes, liant des cles a leurs
     * valeurs
     * 
     * @param attributes
     *            un mapping cle/valeur
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = unmodifiableMap(new HashMap<String, String>(attributes));
    }

    /**
     * Verifie s'il n'y a aucune paire cle/valeur
     * 
     * @return True sssi aucune paire cle/valeur
     */
    public boolean isEmpty() {
        return (this.attributes.isEmpty() || this.attributes == null);
    }

    /**
     * Teste si l'instance contient la cle passe en parametre
     * 
     * @param key
     *            La cle a tester
     * @return True sssi la cle est presente
     */
    public boolean contains(String key) {
        return this.attributes.containsKey(key);
    }

    /**
     * Retour la valeur associee a la clef donnee, ou null si la clef n'existe
     * pas
     * 
     * @param key
     *            La cle a tester
     * @return La valeur assoociee OU null si cle non trouvee
     */
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * Retourne la valeur associee a la clef donnee, ou la valeur par defaut
     * donnee si aucune valeur ne lui est associee
     * 
     * @param key
     *            La cle a tester
     * @param defaultValue
     *            La valeur par defaut si la cle n'est pas presente
     * @return La valeur associee a la cle OU null si cle non trouvee
     */
    public String get(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * Retourne l'entier associe a la clef donnee, ou la valeur par defaut
     * donnee si aucune valeur ne lui est associee, ou si cette valeur n'est pas
     * un entier valide
     * 
     * @param key
     *            La cle a tester
     * @param defaultValue
     *            La valeur par defaut
     * @return La valeur associee a la cle OU null si cle non trouvee ou si
     *         nombre invalide
     */
    public int get(String key, int defaultValue) {
        if (this.attributes.containsKey(key)) {
            int retour;
            try {
                retour = Integer.parseInt(this.attributes.get(key));
            } catch (NumberFormatException e) {
                return defaultValue;
            }
            return retour;
        } else {
            return defaultValue;
        }
    }

    /**
     * Retourne une version filtree des attributs ne contenant que ceux dont le
     * nom figure dans l'ensemble passe en argument
     * 
     * @param keysToKeep
     *            L'ensemble a tester
     * @return Le nouvel Attributes trie
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Attributes.Builder b = new Attributes.Builder();

        for (String key : keysToKeep) {
            if (this.attributes.containsKey(key)) {
                b.put(key, this.attributes.get(key));
            }
        }

        return b.build();
    }

    /**
     * 
     * @author Hugo Hueber (246095)
     * @author Maxime Pisa (247650)
     *
     */
    public static final class Builder {
        HashMap<String, String> keys = new HashMap<String, String>();

        /**
         * Ajoute l'association (clef, valeur) donnée a l'ensemble d'attributs
         * en cours de construction Si la cle existe deja, la valeur associee
         * precedente est remplacee par la nouvelle valeur
         * 
         * @param key
         *            La cle a ajouter
         * @param value
         *            La valeur associee
         */
        public void put(String key, String value) {
            this.keys.put(key, value);
        }

        /**
         * Construit un ensemble d'attributs contenant les associations
         * clef/valeur ajoutees
         * 
         * @return
         */
        public Attributes build() {
            return new Attributes(keys);
        }
    }
}
