package ch.epfl.imhof.geometry;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.PointGeo;
import static java.lang.Math.toRadians;
import static ch.epfl.imhof.osm.OSMRelation.Member;

/**
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 *         Reader de OSMMap
 */

public final class DuckReader {

    /**
     * Lis un fichier xml et retourne une OSMMap
     */
    private DuckReader() {
    }

    /**
     * Lis un fichier XML et retourne une OSMMap
     * 
     * @param fileName
     *            L'emplacement du fichier
     * @param unGZip
     *            Vrai=Fichier GZIP
     * @return L'OSMMap correspondante au fichier passe en parametre
     * @throws IOException
     *             Si le fichier n'arrive pas a etre lu
     * @throws SAXException
     *             Si le fichier est corrompu
     */
    public static Map readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {

        try (InputStream stream = unGZip ? new GZIPInputStream(
                new FileInputStream(fileName)) : new FileInputStream(fileName)) {

            Map.Builder mapBuilder = new Map.Builder();

            XMLReader reader = XMLReaderFactory.createXMLReader();

            reader.setContentHandler(new DefaultHandler() {
                // Declaration
                // Point.Builder nodeBuilder;
                // OSMWay.Builder wayBuilder;
                // OSMRelation.Builder relationBuilder;

                PolyLine.Builder lineBuilder;
                PolyLine.Builder holesBuilder;
                HashMap<String, String> attributes;
                Boolean o = false;
                
                ArrayList<ClosedPolyLine> holes = new ArrayList<>();

                String state = "";

                /**
                 * Analyse la balise d'ouverture
                 */
                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                    case "l":
                        state = "polyline";

                        lineBuilder = new PolyLine.Builder();
                        attributes = new HashMap<String, String>();

                        break;

                    case "o":

                        o = true;

                        break;

                    case "p":

                        lineBuilder.addPoint(new Point(Double.parseDouble(atts
                                .getValue("x")), Double.parseDouble(atts
                                .getValue("y"))));

                        break;
                    case "a":

                        attributes.put(atts.getValue("k"), atts.getValue("v"));

                        break;

                    case "g":
                        
                        lineBuilder = new PolyLine.Builder();
                        
                        attributes = new HashMap<String, String>();
                        holes = new ArrayList<>(); 
                        
                        break;
                        
                    case "s":
                        
                        lineBuilder.addPoint(new Point(Double.parseDouble(atts
                                .getValue("x")), Double.parseDouble(atts
                                .getValue("y"))));                        
                        break;
                        
                    case "h":
                        
                        holesBuilder = new PolyLine.Builder();
                        
                        break;
                    
                    case "d":
                        
                        holesBuilder.addPoint(new Point(Double.parseDouble(atts
                                .getValue("x")), Double.parseDouble(atts
                                .getValue("y"))));
                        
                        break;
                        
                    case "t":
                        
                        attributes.put(atts.getValue("k"), atts.getValue("v"));
                        
                        break;

                    default:
                        break;
                    }
                }

                // ******************************************************************************************************************************************************

                /**
                 * Analyse la balise de fermeture
                 */
                @Override
                public void endElement(String uri, String lName, String qName) {
                    switch (qName) {
                    case "l":
                        if (o) {
                            mapBuilder.addPolyLine(new Attributed<PolyLine>(
                                    lineBuilder.buildOpen(),
                                    new ch.epfl.imhof.Attributes(attributes)));
                            o = false;
                        } else {
                            mapBuilder.addPolyLine(new Attributed<PolyLine>(
                                    lineBuilder.buildClosed(),
                                    new ch.epfl.imhof.Attributes(attributes)));

                        }

                        break;
                            
                    case "h":
                        
                        holes.add(holesBuilder.buildClosed());

                        break;
                    case "g":

                        mapBuilder.addPolygon(new Attributed<Polygon>(new Polygon(lineBuilder.buildClosed(), holes), new ch.epfl.imhof.Attributes(attributes)));

                        break;
                    default:
                        break;
                    }
                }
            });

            reader.parse(new InputSource(stream));

            return mapBuilder.build();
        }
    }

}
