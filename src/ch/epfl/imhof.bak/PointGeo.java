package ch.epfl.imhof;

import static java.lang.Math.PI;

/**
 * Point via coordonnees spheriques.
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * */
public final class PointGeo {
    private final double longitude;
    private final double latitude;

    /**
     * Constructeur d'une instance de PointGeo
     * 
     * @param longitude
     *            La longitude en radians
     * @param latitude
     *            La latitude en radians
     * @throws IllegalArgumentException
     *             Si la longitude n'est pas comprise entre -PI et Pi, ou si la
     *             latitude n'est pas comprise entre -PI/2 et PI/2
     */
    public PointGeo(double longitude, double latitude)
            throws IllegalArgumentException {

        if (longitude < -PI || longitude > PI) {
            throw new IllegalArgumentException(
                    "Longitude invalide (devrait etre comprise entre -PI/2 et PI/2).");
        } else {
            this.longitude = longitude;
        }

        if (latitude < -(PI / 2) || latitude > (PI / 2)) {
            throw new IllegalArgumentException(
                    "Latitude invalide (devrait etre comprise entre -PI/2 et PI/2).");
        } else {
            this.latitude = latitude;
        }
    }

    /**
     * 
     * @return La longitude du PointGeo
     */
    public double longitude() {
        return this.longitude;
    }

    /**
     * 
     * @return La latitude du PointGeo
     */
    public double latitude() {
        return this.latitude;
    }
}
