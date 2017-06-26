package ch.epfl.imhof;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

/**
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 *         Representation d'un vecteur a trois composantes
 */
public final class Vector3 {
    private final double x, y, z;

    /**
     * Constructeur de vecteur a trois composantes
     * 
     * @param x
     *            Premiere composante
     * @param y
     *            Deuxieme composante
     * @param z
     *            Troisieme composante
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 
     * @return La norme du vecteur
     */
    public double norm() {
        return sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
    }

    /**
     * 
     * @return Ce vecteur, normalise
     */
    public Vector3 normalized() {
        return new Vector3(this.x / this.norm(), this.y / this.norm(), this.z
                / this.norm());
    }

    /**
     * 
     * @param that
     *            Un autre vecteur
     * @return Le produit scalaire entre ce vecteur et le vecteur passe en
     *         argument
     */
    public double scalarProduct(Vector3 that) {
        return this.x * that.x + this.y * that.y + this.z * that.z;
    }

    /**
     * 
     * @param first
     *            Premier vecteur
     * @param second
     *            Deuxieme vecteur
     * @return Produit vectoriel de deux vecteurs
     */
    public static Vector3 crossProduct(Vector3 first, Vector3 second) {
        double x = first.y * second.z - first.z * second.y;
        double y = first.z * second.x - first.x * second.z;
        double z = first.x * second.y - first.y * second.x;
        return new Vector3(x, y, z);
    }
}
