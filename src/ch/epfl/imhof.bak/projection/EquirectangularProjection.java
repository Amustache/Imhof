package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * La projection equirectangulaire
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public class EquirectangularProjection implements Projection {

    /**
     * Projette sur le plan le point recu en argument
     * 
     * @param point
     *            Un point en coordonnees spheriques (radians)
     * @return Le point correspondant en coordonnees cartesiennes
     */
    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }

    /**
     * "De-projette" le point du plan recu en argument
     * 
     * @param point
     *            Un point en coordonnees cartesiennes
     * @return Le point correspondant en coordonnees spheriques (radians)
     */
    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }

}
