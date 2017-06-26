package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Polyline ouverte
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class OpenPolyLine extends PolyLine {
    /**
     * Constructeur d'une OpenPolyLine
     * 
     * @param points
     *            La liste des points
     * 
     */
    public OpenPolyLine(List<Point> points) {
        super(new ArrayList<>(points));
    }

    /*
     * Une OpenPolyLine n'est jamais fermee
     * 
     * @return False
     */
    @Override
    public boolean isClosed() {
        return false;
    }
}
