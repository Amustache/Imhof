package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 *         Filtres
 *
 */
public final class Filters {

    /**
     * Non instanciable
     */
    private Filters() {
    }

    /**
     * @param str
     *            Nom de l'attribut
     * @return Un predicat qui n'est vrai que si la valeur attribuee à laquelle
     *         on l'applique possede un attribut portant ce nom, independemment
     *         de sa valeur
     */
    public static Predicate<Attributed<?>> tagged(String str) {
        return p -> p.hasAttribute(str);
    }

    /**
     * @param str
     *            Nom de l'attribut
     * @param strings
     *            Valeurs associées
     * @return Un predicat qui n'est vrai que si la valeur attribuee à laquelle
     *         on l'applique possede un attribut portant le nom donne et si de
     *         plus la valeur associee à cet attribut fait partie de celles
     *         donnees
     */
    public static Predicate<Attributed<?>> tagged(String str, String... strings) {

        return p -> {
            if (!p.hasAttribute(str)) {
                return false;
            }

            for (String stringa : strings) {
                if (p.attributeValue(str).equals(stringa)) {
                    return true;
                }
            }

            return false;
        };
    }

    /**
     * @param layer
     *            La couche concernee
     * @return Un predicat qui n'est vrai que lorsqu'on l'applique a une entite
     *         attribuee appartenant cette couche
     */
    public static Predicate<Attributed<?>> onLayer(int layer) {
        return p -> {
            return p.attributeValue("layer", 0) == layer;
        };
    }
}
