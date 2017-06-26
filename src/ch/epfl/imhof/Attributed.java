package ch.epfl.imhof;

/**
 * Permet d'attribuer des parametres a un objet afin de decrire son identite
 * (lac, route, etc.).
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 * @param <T>
 *            Le type a utiliser
 */
public final class Attributed<T> {

    private final T value;
    private final Attributes attributes;

    /**
     * @param value_ L'objet
     * @param attributes_ Les parametres
     *            Construit un objet attibut
     */
    public Attributed(T value_, Attributes attributes_) {
        this.value = value_;
        this.attributes = attributes_;
    }

    /**
     * @return Retourne la valeur a laquelle les attributs sont attaches
     */
    public T value() {
        return this.value;
    }

    /**
     * @return Retourne les attributs attaches a la valeur
     */
    public Attributes attributes() {
        return this.attributes;
    }

    /**
     * @param attributeName
     *            Le nom de l'attribut
     * @return Retourne vrai si et seulement si les attributs incluent celui
     *         dont le nom est passe en argument. Correspond a la methode
     *         contains de Attributes
     */
    public boolean hasAttribute(String attributeName) {
        return this.attributes.contains(attributeName);
    }

    /**
     * @param attributeName
     *            Le nom de l'attribut
     * @return Retourne la valeur associee a l'attribut donne, ou null si
     *         celui-ci n'existe pas
     */
    public String attributeValue(String attributeName) {
        return this.attributes.get(attributeName);
    }

    /**
     * @param attributeName
     *            Le nom de l'attribut
     * @param defaultValue
     *            La valeur par defaut
     * @return Retourne la valeur associee à l'attribut donne, ou la valeur par
     *         defaut donnee si celui-ci n'existe pas
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return this.attributes.get(attributeName, defaultValue);
    }

    /**
     * @param attributeName
     *            Le nom de l'attribut
     * @param defaultValue
     *            La valeur par defaut
     * @return retourne la valeur entiere associee à l'attribut donne, ou la
     *         valeur par defaut si celui-ci n'existe pas ou si la valeur qui
     *         lui est associee n'est pas un entier valide
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return this.attributes.get(attributeName, defaultValue);
    }
}
