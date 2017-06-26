package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import static java.lang.Math.toDegrees;
import static java.lang.Math.sqrt;

/**
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 *         MNT stocke dans un fichier au format HGT
 *
 */
public class HGTDigitalElevationModel implements DigitalElevationModel {
    private final double swLon, swLat;
    private final double reso;
    private final int size;
    private ShortBuffer buffer;

    /**
     * 
     * @param file
     *            Le fichier a passer en parametre
     * @throws IllegalArgumentException
     *             Si le format du fichier est invalide
     * @throws IOException
     *             S'il y a une erreur I/O
     * @throws FileNotFoundException
     *             Si le fichier n'est pas trouve (duh)
     */
    public HGTDigitalElevationModel(File file) throws IllegalArgumentException,
            FileNotFoundException, IOException {
        String filename = file.getName();
        long length = file.length();

        this.size = (int) sqrt(length / 2);

        // Si le nom du fichier est invalide ou si la taille de ce dernier est
        // incorrecte
        if (!(filename.length() == 11)
                || !filename.substring(7, 11).equals(".hgt")
                || this.size * this.size != length * 0.5
                || (this.size - 1) % 2 != 0) {
            System.out
                    .println(this.size + "," + length + "," + (this.size - 1));
            throw new IllegalArgumentException(
                    "Le fichier n'est pas au format HGT");
        }

        // Si le fichier ne contient qu'un seul point
        if (this.size == 1) {
            this.reso = 0;
        } else {
            this.reso = Math.toRadians(1d / (size - 1));
        }

        // Si le fichier a la bonne latitude
        if (filename.charAt(0) != 'N' && filename.charAt(0) != 'S') {
            throw new IllegalArgumentException(
                    "Latitude du nom de fichier incorrecte");
        } else {
            try {
                swLat = filename.charAt(0) == 'N' ? Integer.parseInt(filename
                        .substring(1, 3)) : (-1)
                        * Integer.parseInt(filename.substring(1, 3));
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Latitude du nom de fichier incorrecte");
            }
        }

        // Si le fichier a la bonne longitude
        if (filename.charAt(3) != 'E' && filename.charAt(3) != 'W') {
            throw new IllegalArgumentException(
                    "Longitude du nom de fichier incorrecte");
        } else {
            try {
                swLon = filename.charAt(3) == 'E' ? Integer.parseInt(filename
                        .substring(4, 7)) : (-1)
                        * Integer.parseInt(filename.substring(4, 7));
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Longitude du nom de fichier incorrecte");
            }
        }

        // On stocke le stream dans un buffer puis on ferme le stream
        try (FileInputStream dataStream = new FileInputStream(file)) {
            this.buffer = dataStream.getChannel()
                    .map(MapMode.READ_ONLY, 0, length).asShortBuffer();
        }
    }

    /**
     * "Supprime" le buffer
     */
    @Override
    public void close() throws Exception {
        this.buffer = null;
    }

    /**
     * Retourne le vecteur normal à la surface de la Terre au point passe en
     * parametres
     * 
     * @param point
     *            Point en coordonnees WGS 84
     * @return Vecteur normal a la Terre en ce point
     */
    @Override
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException {
        double longitude = toDegrees(point.longitude());
        double latitude = toDegrees(point.latitude());

        // Si le point ne fait pas parti du fichier
        if (longitude < this.swLon || longitude > this.swLon + 1
                || latitude < this.swLat || latitude > this.swLat + 1) {
            throw new IllegalArgumentException(
                    "Le point ne fait pas parti du fichier");
        }

        double s = Earth.RADIUS * this.reso;

        // On prend une approximation du point le plus proche
        int lon = (int) Math.floor(this.size * (longitude - this.swLon)) - 1;
        int lat = (int) Math.floor(this.size - this.size
                * (latitude - this.swLat) - 1);

        // On calcule les quatre points correspondants
        double z11 = this.buffer.get(lon + this.size * (lat + 1));
        double z12 = this.buffer.get((lon + 1) + this.size * (lat + 1));
        double z21 = this.buffer.get((lon) + this.size * lat);
        double z22 = this.buffer.get((lon + 1) + this.size * lat);

        // Composantes
        double x = 0.5d * (z11 - z12 + z21 - z22) * s;
        double y = 0.5d * (z11 + z12 - z21 - z22) * s;

        return new Vector3(x, y, Math.pow(s, 2));
    }
}
