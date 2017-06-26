/*
 *	Author:      Pisa Maxime
 *	Date:        22 avr. 2015
 */

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
        Filters f = new Filters();
     // Le peintre et ses filtres
        Predicate<Attributed<?>> isLake =
            f.tagged("natural", "water");
        Painter lakesPainter =
            Painter.polygon(Color.BLUE).when(isLake);

        Predicate<Attributed<?>> isBuilding =
            f.tagged("building");
        Painter buildingsPainter =
            Painter.polygon(Color.BLACK).when(isBuilding);

        Painter painter = buildingsPainter.above(lakesPainter);

        Map map = new OSMToGeoTransformer(new CH1903Projection()).transform(OSMMapReader.readOSMFile("data/lausanne.osm.gz", true)); // Lue depuis lausanne.osm.gz

        // La toile
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);
        Java2DCanvas canvas =
            new Java2DCanvas(bl, tr, 800, 530, 72, Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);
        try {
            ImageIO.write(canvas.image(), "png", new File("loz.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
