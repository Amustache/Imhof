package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floorMod;
import static java.lang.Math.abs;

/**
 * Represente une polyline fermee.
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class ClosedPolyLine extends PolyLine {

    /**
     * @param points
     *            La liste des points de la polyline
     */
    public ClosedPolyLine(List<Point> points) {
        super(new ArrayList<>(points));
    }

    /**
     * Une ClosedPolyline est toujours fermee
     * 
     * @return True
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * @return L'aire non signee (positive) de la polyline
     */
    public double area() {
        double signedArea = 0;

        // Calcul de l'aire signee
        for (int i = 0; i < this.points().size(); ++i) {
            signedArea += this.points().get(i).x()
                    * (this.points().get(genIndex(i + 1)).y() - this.points()
                            .get(genIndex(i - 1)).y());
        }

        // On retourne l'aire non signee
        return (1.0 / 2.0) * abs(signedArea);
    }

    /**
     * @param p
     *            Le point a tester
     * @return True sssi le point est inclus dans la polyline
     */
    public boolean containsPoint(Point p) {
        int index = 0;

        for (int i = 0; i < this.points().size(); ++i) {
            // Si le point est a gauche, on incremente l'indice
            if (this.points().get(i).y() <= p.y()) {
                if (this.points().get(genIndex(i + 1)).y() > p.y()
                        && isLeft(p, this.points().get(i),
                                this.points().get(genIndex(i + 1)))) {
                    ++index;
                }
            } else { // Sinon, on decremente l'indice
                if (this.points().get(genIndex(i + 1)).y() <= p.y()
                        && isLeft(p, this.points().get(genIndex(i + 1)), this
                                .points().get(i))) {
                    --index;
                }
            }
        }

        // Un point est a l'exterieur si l'indice est 0
        return (index != 0);
    }

    /**
     * @param p
     *            Le point a tester
     * @param start
     *            Le point de debut du segment de reference
     * @param end
     *            Le point de fin du segment de reference
     * @return True sssi le point se trouve a gauche du segment dans le sens
     *         antihoraire
     */
    private boolean isLeft(Point p, Point start, Point end) {
        return ((start.x() - p.x()) * (end.y() - p.y()) > (end.x() - p.x())
                * (start.y() - p.y()));
    }

    /**
     * @param index
     *            L'index a generaliser
     * @return L'index generalise
     */
    private int genIndex(int index) {
        return floorMod(index, this.points().size());
    }
}
