/*
 *	Author:      Pisa Maxime
 *	Date:        27 mai 2015
 */

package ch.epfl.imhof.geometry;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import ch.epfl.imhof.Map;

public class DuckWriter {
    private static String[] keysPolyline = { "bridge", "highway", "layer", "man_made", "railway",
            "tunnel", "waterway"};
    private static String[] keysPolygon = { "building", "landuse", "layer", "leisure", "natural",
        "waterway"};
    private static String[] keysToKeep = { "aeroway", "amenity", "building", "harbour",
        "historic", "landuse", "leisure", "man_made", "military",
        "natural", "office", "place", "power", "public_transport",
        "shop", "sport", "tourism", "water", "waterway", "wetland" };
    
    private DuckWriter(){
    }
    
    public static void write(Map m, String fileName) throws FileNotFoundException, UnsupportedEncodingException{
        
        PrintWriter writer = new PrintWriter(fileName + ".duck", "UTF-8");
        writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<start>");
        for (int i = 0; i < m.polyLines().size(); ++i) {
            writer.print("<l>");
            if (!m.polyLines().get(i).value().isClosed()) {
                writer.print("<o/>");
            }
            for (int j = 0; j < m.polyLines().get(i).value().points().size(); ++j) {
                writer.print("<p x=\"" + m.polyLines().get(i).value().points().get(j).x() + "\" ");
                writer.print("y=\"" + m.polyLines().get(i).value().points().get(j).y() + "\"/>");
            }
            for (int j = 0; j < keysPolyline.length; ++j) {
                if (m.polyLines().get(i).attributes().contains(keysPolyline[j])) {
                    writer.print("<a k=\"" + keysPolyline[j] + "\" v=\"");
                    writer.print(m.polyLines().get(i).attributes().get(keysPolyline[j]) + "\"/>");
                }
//                if (m.polyLines().get(i).attributes().contains(keysToKeep[j])) {
//                    writer.print("<a k=\"" + keysToKeep[j] + "\" v=\"");
//                    writer.print(m.polyLines().get(i).attributes().get(keysToKeep[j]) + "\"/>");
//                }
            }
            writer.println("</l>");
        }
        
        
        for (int i = 0; i < m.polygons().size(); ++i) {
            writer.print("<g>");
            for (int j = 0; j < m.polygons().get(i).value().shell().points().size(); ++j) {
                writer.print("<s x=\"" + m.polygons().get(i).value().shell().points().get(j).x() + "\" ");
                writer.print("y=\"" + m.polygons().get(i).value().shell().points().get(j).y() + "\"/>");
            }
            
            for (int j = 0; j < m.polygons().get(i).value().holes().size(); ++j) {
                writer.print("<h>");
                for (int k = 0; k < m.polygons().get(i).value().holes().get(j).points().size(); ++k) {
                    writer.print("<d x=\"" + m.polygons().get(i).value().holes().get(j).points().get(k).x() + "\" ");
                    writer.print("y=\"" + m.polygons().get(i).value().holes().get(j).points().get(k).y() + "\"/>");
                }
                writer.print("</h>");
            }
            
            for (int j = 0; j < keysPolygon.length; ++j) {
                
                if (m.polygons().get(i).attributes().contains(keysPolygon[j])) {
                    writer.print("<t k=\"" + keysPolygon[j] + "\" v=\"" + m.polygons().get(i).attributes().get(keysPolygon[j]) + "\"/>");
                }
//                if (m.polygons().get(i).attributes().contains(keysToKeep[j])) {
//                    writer.print("<t k=\"" + keysToKeep[j] + "\" v=\"" + m.polygons().get(i).attributes().get(keysToKeep[j]) + "\"/>");
//                }
            }
            writer.println("</g>");
        }
        writer.println("</start>");
        writer.close();
    }
}
