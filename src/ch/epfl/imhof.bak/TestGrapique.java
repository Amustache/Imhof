/*
 *	Author:      Pisa Maxime
 *	Date:        25 mars 2015
 */

package ch.epfl.imhof;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.xml.sax.SAXException;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
//import ch.epfl.imhof.osm.OSMToGeo_2;
import ch.epfl.imhof.projection.CH1903Projection;
public class TestGrapique extends JPanel {
    //on est trop des pros, ouais, on met des nombres au hasard, ouais.
    private static final long serialVersionUID = 4123341284384378524L;
    public static Map map;
    public static Map map2;
    public static Map map3;
    public Thread t1;
    private AffineTransform tx1;
    public static JFrame jf;
    public TestGrapique(){
        this.setSize(1000,700);
        this.setVisible(true);
        this.t1 = new Thread(new Loop());
        this.t1.start();
    }
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        tx1 = g2d.getTransform();
        tx1.translate(250,250);
        tx1.scale(0.01, -0.01);
        
        //Endroit à Editer pour la position de la caméra
        tx1.translate(-628000,-172000);
//        tx1.translate(0,0);
        g2d.transform(tx1);
//        renderMap(g,this.map);
//        renderMap(g,this.map2);
        renderMap(g,this.map3);
        
        g2d.transform(old);
    }
    
    public void renderMap(Graphics g, Map map){
        //PolyTest test = new PolyTest();
        java.awt.Polygon jp = new java.awt.Polygon();
        for(Attributed<Polygon> p:map.polygons()) {
            
            for(Point pt : p.value().shell().points())
            {
                jp.addPoint((int)pt.x(), (int)pt.y());
            }
            g.setColor(Color.LIGHT_GRAY);
            
            if (p.hasAttribute("natural")) {
                
                switch(p.attributeValue("natural"))
                {
                    case "sand" : g.setColor(new Color(0xF4E9C4)); break;
                    case "fell" : g.setColor(new Color(0xEDE4DC)); break;
                    case "health" : g.setColor(new Color(0xD6D99F)); break;
                    case "scree" : g.setColor(new Color(0xEDE4DC)); break;
                    case "scrub" : g.setColor(new Color(0xA7DDA7)); break;
                    case "water" : g.setColor(Color.BLUE); break;
                    case "wood" : g.setColor(Color.GREEN); break;  
                    case "grassland" : g.setColor(new Color(0xA0BD87)); break;
                    case "beach" : g.setColor(new Color(0xFFF2B7)); break;
                }               
                
            }else if (p.hasAttribute("building")){
                g.setColor(Color.black); 
            }else if (p.hasAttribute("leisure")){
                
                switch(p.attributeValue("leisure"))
                {
                    case "park" : g.setColor(new Color(0xCDF7C9)); break;
                    case "pitch" : g.setColor(new Color(0x8AD3AF)); break;
                    case "sports_center" : g.setColor(new Color(0xCDF7C9)); break;
                    case "statium" : g.setColor(new Color(0xCDF7C9)); break;
                } 
                
            }else if (p.hasAttribute("landuse")){
                
                switch(p.attributeValue("landuse"))
                {
                    case "meadow" : g.setColor(new Color(0xCFECA8)); break;
                    case "recreation_ground" : g.setColor(new Color(0xCFEDA5)); break;
                    case "orchard" : g.setColor(new Color(0x9BD88B)); break;
                    case "industrial" : g.setColor(new Color(0xDFD1D6)); break;
                    case "grass" : g.setColor(new Color(0xACAC88)); break;
                    case "commercial" : g.setColor(new Color(0xF0C8C7)); break;
                    case "forest" : g.setColor(new Color(0x9FD082)); break;  
                    case "farmland" : g.setColor(new Color(0xECDDC8)); break;
                    case "greenfield" : g.setColor(new Color(0xB6B690)); break;
                    case "farmyard" : g.setColor(new Color(0xEFD8B9)); break;
                    case "residential" : g.setColor(new Color(0xE1E1E1)); break;  
                    case "retail" : g.setColor(new Color(0xF0D9D9)); break;
                    case "village_green" : g.setColor(new Color(0xCFEDA5)); break;
                    case "vineyard" : g.setColor(new Color(0xB3E2A8)); break;
                }   
            }else if (p.hasAttribute("layer")){
                g.setColor(Color.pink); 
            }else if (p.hasAttribute("waterway")){
                g.setColor(Color.blue); 
            }else {
               g.setColor(Color.white); 
            }
//            test.setPolyLine(p.value().shell());
//            test.renderPolyLine(g);
            
//            for(PolyLine p2: p.value().holes()){
//                test.setPolyLine(p2);
//                test.renderPolyLine(g);
//            }
            g.fillPolygon(jp);
            jp.reset();
        }  
    }
    class Loop implements Runnable {
        @Override
        public void run() {
            while(true){
            // do stuff
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
            }
        }
    }
    public static void main(String[] args) throws IOException, SAXException {
//        map = new OSMToGeoTransformer(new CH1903Projection()).transform(OSMMapReader.readOSMFile("data/lausanne.osm.gz", true));
//        map2 = new OSMToGeoTransformer(new CH1903Projection()).transform(OSMMapReader.readOSMFile("data/berne.osm.gz", true));
        map3 = new OSMToGeoTransformer(new CH1903Projection()).transform(OSMMapReader.readOSMFile("data/berne.osm.gz", true));
        
        jf = new JFrame();
        TestGrapique pf = new TestGrapique();
        jf.add(pf);
        jf.setSize(630, 650);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
