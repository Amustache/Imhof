/*
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 */

package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

public class Filters {

    /**
     * @param str
     * @return un prédicat qui n'est vrai que si la valeur attribuée à laquelle on l'applique
     *  possède un attribut portant ce nom, indépendemment de sa valeur
     */
    public Predicate<Attributed<?>> tagged(String str) {
        return p -> p.hasAttribute(str);
    }

    /**
     * @param str
     * @param strings
     * @return un prédicat qui n'est vrai que si la valeur attribuée à laquelle on l'applique
     *  possède un attribut portant le nom donné et si de plus la valeur associée à cet attribut
     *   fait partie de celles données
     */
    public Predicate<Attributed<?>> tagged(String str, String... strings) {

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
     * @return un prédicat qui n'est vrai que lorsqu'on l'applique à une entité attribuée
     *  appartenant cette couche
     */
    public Predicate<Attributed<?>> onLayer(int layer){
        return p -> {
            return Integer.parseInt(p.attributeValue("layer")) == layer;
        };
    }
}
