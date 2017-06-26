package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 *         Modele numerique du terrain
 */
public interface DigitalElevationModel extends AutoCloseable {
    /**
     * 
     * @param point
     *            Point en coordonnees WGS 84
     * @return Vecteur normal a la Terre en ce point
     */
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException;
}
