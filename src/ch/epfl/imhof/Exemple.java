package ch.epfl.imhof;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Filters;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;

public class Exemple {

    public static void main(String[] args) throws IOException, SAXException {
        long tStart = System.currentTimeMillis();
        System.out.println("Debut:" + tStart);

        // Le peintre et ses filtres
        Predicate<Attributed<?>> isLake = Filters.tagged("natural", "water");

        System.out.println("Filtre1:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        Painter lakesPainter = Painter.polygon(Color.BLUE).when(isLake);

        System.out.println("Filtre2:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        Predicate<Attributed<?>> isBuilding = Filters.tagged("building");

        System.out.println("Filtre3:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        Painter buildingsPainter = Painter.polygon(Color.BLACK)
                .when(isBuilding);

        System.out.println("Painter1:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        Painter painter = buildingsPainter.above(lakesPainter);

        System.out.println("Painter2:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        Map map = new OSMToGeoTransformer(new CH1903Projection())
                .transform(OSMMapReader.readOSMFile("data/lausanne.osm.gz",
                        true)); // Lue depuis lausanne.osm.gz

        System.out.println("Map transformee:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        // La toile
        Point bl = new Point(532510, 150590);

        System.out.println("Point1:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        Point tr = new Point(539570, 155260);

        System.out.println("Point2:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 1600, 1060, 150,
                Color.WHITE);

        System.out.println("Canvas:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);

        System.out.println("Map dessinee:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        try {
            ImageIO.write(canvas.image(), "png", new File("lausanne.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Fichier produit:"
                + ((System.currentTimeMillis() - tStart) / 1000.0));

        System.out.println("    Temps total :"
                + ((System.currentTimeMillis() - tStart) / 1000.0));
    }

}
