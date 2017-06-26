package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.emptyList;

/**
 * Represente un polygone a trous
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class Polygon {
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * @param shell
     *            Le contour du polygone, represente par une polyline fermee
     * @param holes
     *            La liste des polylines composant les trous du polygone
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = unmodifiableList(new ArrayList<>(holes));

        // TODO Add holes verifications
    }

    /**
     * @param shell
     *            Le contour du polygone, represente par une polyline fermee
     */
    public Polygon(ClosedPolyLine shell) {
        this(shell, emptyList());
    }

    /**
     * @return Le contour du polygone
     */
    public ClosedPolyLine shell() {
        return this.shell;
    }

    /**
     * @return La liste des trous du polygone
     */
    public List<ClosedPolyLine> holes() {
        return this.holes;
    }
}
