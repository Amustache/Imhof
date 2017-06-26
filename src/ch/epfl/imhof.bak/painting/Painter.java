/*
 *	Author:      Pisa Maxime
 *	Date:        20 avr. 2015
 */

package ch.epfl.imhof.painting;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.painting.LineStyle.lineCap;
import ch.epfl.imhof.painting.LineStyle.lineJoin;

public interface Painter {
    public void drawMap(Map map, Java2DCanvas canvas);
    
    public static Painter polygon(Color couleur){
        return (m, c) ->{
            for (int i = 0; i < m.polygons().size(); ++i) {
                c.drawPolygon(m.polygons().get(i).value(), couleur);
            }
        };
    }
    
    public static Painter line(float width, Color color, lineCap cap, lineJoin join, float[] pattern){
        return (m, c) -> {
            for (int i = 0; i < m.polyLines().size(); ++i) {
                c.drawPolyLine(m.polyLines().get(i).value(), new LineStyle(width, color, cap, join, pattern));
            }
        };
    }
    
    public static Painter line(float width, Color color){
        return (m, c) -> {
            for (int i = 0; i < m.polyLines().size(); ++i) {
                c.drawPolyLine(m.polyLines().get(i).value(), new LineStyle(width, color));
            }
        };
    }
    
    public static Painter outline(float width, Color color, lineCap cap, lineJoin join, float[] pattern){
        return (m, c) -> {
            for (int i = 0; i < m.polygons().size(); ++i) {
                for (int j = 0; j < m.polygons().get(i).value().holes().size(); ++j) {
                    c.drawPolyLine(m.polygons().get(i).value().holes().get(j), new LineStyle(width, color, cap, join, pattern));
                }
            }
        };
    }
    
    public static Painter outline(float width, Color color){
        return (m, c) -> {
            for (int i = 0; i < m.polygons().size(); ++i) {
                for (int j = 0; j < m.polygons().get(i).value().holes().size(); ++j) {
                    c.drawPolyLine(m.polygons().get(i).value().holes().get(j), new LineStyle(width, color));
                }
            }
        };
    }
    
    //*********************************
    
    public default Painter when(Predicate<Attributed<?>> p){
        return (m, c) -> {
            //flots
            drawMap( new Map( m.polyLines().stream().filter(p).collect(Collectors.toList()),
            m.polygons().stream().filter(p).collect(Collectors.toList())), c);
        };
    }
    
    public default Painter above(Painter paint){
        return (m, c) -> {
            paint.drawMap(m, c);
            this.drawMap(m, c);
        };
    }
    
    public default Painter layered() {
        return (m, c) -> {
            Filters f = new Filters();
            for (int i = -5; i < 6; ++i) {
                when(f.onLayer(i)).drawMap(m, c);
            }
        };
    }
}
