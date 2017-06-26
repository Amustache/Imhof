package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static java.lang.Math.pow;

/**
 * La "projection" CH1903, bien qu'il ne s'agisse pas veritablement d'une
 * projection.
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class CH1903Projection implements Projection {

    /**
     * Projette sur le plan le point recu en argument
     * 
     * @param point
     *            Un point en coordonnees spheriques (radians)
     * @return Le point correspondant en coordonnees cartesiennes
     */
    @Override
    public Point project(PointGeo point) {
        double longitude = toDegrees(point.longitude());
        double latitude = toDegrees(point.latitude());
        double x = -1;
        double y = -1;

        longitude = (longitude * 3_600.0 - 26_782.5) / 10_000.0;
        latitude = (latitude * 3_600.0 - 169_028.66) / 10_000.0;

        x = 600_072.37 + (211_455.93 * longitude)
                - (10_938.51 * longitude * latitude)
                - (0.36 * longitude * pow(latitude, 2))
                - (44.54 * pow(longitude, 3));
        y = 200_147.07 + (308_807.95 * latitude)
                + (3_745.25 * pow(longitude, 2)) + (76.63 * pow(latitude, 2))
                - (194.56 * pow(longitude, 2) * latitude)
                + (119.79 * pow(latitude, 3));

        return new Point(x, y);
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
        double x = point.x();
        double y = point.y();
        double longitude = -1;
        double latitude = -1;

        x = (x - 600_000.0) / (1_000_000.0);
        y = (y - 200_000.0) / (1_000_000.0);

        longitude = 2.677_909_4 + (4.728_982 * x) + (0.791_484 * x * y)
                + (0.130_6 * x * pow(y, 2)) - (0.043_6 * pow(x, 3));
        latitude = 16.902_389_2 + (3.238_272 * y) - (0.270_978 * pow(x, 2))
                - (0.002_528 * pow(y, 2)) - (0.044_7 * pow(x, 2) * y)
                - (0.014_0 * pow(y, 3));

        longitude = toRadians(longitude * (100.0 / 36.0));
        latitude = toRadians(latitude * (100.0 / 36.0));

        return new PointGeo(longitude, latitude);
    }

}
