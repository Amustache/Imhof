package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Ligne formee d'une sequence de droites t.q. le second point de chaque segment
 * corresponde avec le premier point du segment suivant.
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public abstract class PolyLine {

    private final List<Point> pointsList;

    /**
     * @param points
     * @throws IllegalArgumentException
     *             sssi la liste est nulle ou vide
     * */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points.isEmpty() || points == null) {
            throw new IllegalArgumentException();
        } else {
            this.pointsList = unmodifiableList(new ArrayList<Point>(points));
        }
    }

    /**
     * @return True sssi la polyline est fermee
     */
    public abstract boolean isClosed();

    /**
     * @return La liste des points
     */
    public List<Point> points() {
        return unmodifiableList(new ArrayList<Point>(this.pointsList));
    }

    /**
     * @return Le premier point de la liste des points
     */
    public Point firstPoint() {
        return this.pointsList.get(0);
    }

    public static final class Builder {

        private List<Point> pointsList;
        
        public Builder() {
            this.pointsList = new ArrayList<Point>();
        }

        /**
         * @param newPoint
         * 
         *            Ajoute un nouveau point a la liste deja existante des
         *            points
         */
        public void addPoint(Point newPoint) {
            this.pointsList.add(newPoint);
        }

        /**
         * @return Retourne l'OpenPolyLine cree
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(pointsList);
        }

        /**
         * @return Retourne la ClosedPolyLine cree
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(pointsList);
        }
    }
}
