package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Point en coordonnees carthesiennes
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class Point {
    private final double x;
    private final double y;

    /**
     * Constructeur
     * 
     * @param x
     *            La coordonnee sur l'axe des abscisses
     * @param y
     *            La coordonnee sur l'axe des ordonnees
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 
     * @return La coordonnee sur l'axe des abscisses
     */
    public double x() {
        return x;
    }

    /**
     * 
     * @return La coordonnee sur l'axe des ordonnees
     */
    public double y() {
        return y;
    }

    /**
     * Fabrique une fonction permettant de transformer un point selon le repere
     * A en un point selon le repere B, a partir de deux paires de coordonnes
     * 
     * @param p1inFirst
     *            Le point 1, dans les coordonnees du repere A
     * @param p1inSecond
     *            Le point 1, dans les coordonnees du repere B
     * @param p2inFirst
     *            Le point 2, dans les coordonnees du repere A
     * @param p2inSecond
     *            Le point 2, dans les coordonnees du repere B
     * @return La fonction affine transformant un point selon le repere A dans
     *         un point selon le repere B
     * @throws IllegalArgumentException
     *             Si les deux points sont situes sur une meme ligne horizontale
     *             ou verticale
     */
    public static Function<Point, Point> alignedCoordinateChange(
            Point p1inFirst, Point p1inSecond, Point p2inFirst, Point p2inSecond)
            throws IllegalArgumentException {
        if (p1inFirst.x() == p2inFirst.x() || p1inSecond.x() == p2inSecond.x())
            throw new IllegalArgumentException(
                    "Les deux points sont alignes verticalement");

        if (p1inFirst.y() == p2inFirst.y() || p1inSecond.y() == p2inSecond.y())
            throw new IllegalArgumentException(
                    "Les deux points sont alignes horizontalement");

        return point2BeAlive -> {
            double detX = 1.0 / (p1inFirst.x() - p2inFirst.x());
            double detY = 1.0 / (p1inFirst.y() - p2inFirst.y());

            double aX = detX
                    * (p1inSecond.x() - p2inSecond.x());
            double bX = detX
                    * ((-p2inFirst.x() * p1inSecond.x()) + (p1inFirst.x() * p2inSecond.x()));
            double aY = detY
                    * (p1inSecond.y() - p2inSecond.y());
            double bY = detY
                    * ((-p2inFirst.y() * p1inSecond.y()) + (p1inFirst.y() * p2inSecond.y()));

            double x = aX * point2BeAlive.x() + bX;
            double y = aY * point2BeAlive.y() + bY;
            return new Point(x, y);
        };
    }
}
