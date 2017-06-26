package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * L'interface correspondante aux deux projections
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public interface Projection {
    /**
     * Projette sur le plan le point recu en argument
     * 
     * @param point
     *            Un point en coordonnees spheriques (radians)
     * @return Le point correspondant en coordonnees cartesiennes
     */
    public Point project(PointGeo point);

    /**
     * "De-projette" le point du plan recu en argument
     * 
     * @param point
     *            Un point en coordonnees cartesiennes
     * @return Le point correspondant en coordonnees spheriques (radians)
     */
    public PointGeo inverse(Point point);
}
