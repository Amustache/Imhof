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

        // Coordonnee x du point 1 dans les reperes A et B
        double x1 = p1inFirst.x(), x1p = p1inSecond.x();
        // Coordonnee x du point 2 dans les reperes A et B
        double x2 = p2inFirst.x(), x2p = p2inSecond.x();
        // Coordonnee y du point 1 dans les reperes A et B
        double y1 = p1inFirst.y(), y1p = p1inSecond.y();
        // Coordonnee y du point 2 dans les reperes A et B
        double y2 = p2inFirst.y(), y2p = p2inSecond.y();

        // On realise une transformation matricielle du point
        return point2BeAlive -> {
            double detX = 1.0 / (x1 - x2);
            double detY = 1.0 / (y1 - y2);

            double aX = detX * (x1p - x2p);
            double bX = detX * ((-x2 * x1p) + (x1 * x2p));
            double aY = detY * (y1p - y2p);
            double bY = detY * ((-y2 * y1p) + (y1 * y2p));

            double x = aX * point2BeAlive.x() + bX;
            double y = aY * point2BeAlive.y() + bY;
            return new Point(x, y);
        };
    }
}
