/*
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 */


package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public class Main {
    
    /**
     * @param args
     * @throws IOException
     * @throws SAXException
     *          la methode principale du programme
     */
    public static void main(String[] args) throws IOException, SAXException {
        String pathOsmFile = args[0];
        String pathHgtFile = args[1];
        
        double lonBLRadian = Math.toRadians(Double.parseDouble(args[2]));
        double latBLRadian = Math.toRadians(Double.parseDouble(args[3]));
        
        double lonTRRadian = Math.toRadians(Double.parseDouble(args[4]));
        double latTRRadian = Math.toRadians(Double.parseDouble(args[5]));
        
        int dpi = Integer.parseInt(args[6]);
        String pathDestination = args[7];
        
        Projection p = new CH1903Projection();
        
        //*********************************************************************
        long tStart = System.currentTimeMillis();
        System.out.println("Debut:" + tStart);
        
        Map map = new OSMToGeoTransformer(p)
                .transform(OSMMapReader.readOSMFile(pathOsmFile, true)); // Lue depuis lausanne.osm.gz
 
        // La toile
        Point bl = p.project(new PointGeo(lonBLRadian, latBLRadian));
        Point tr = p.project(new PointGeo(lonTRRadian, latTRRadian));
//        
//        double lonBLRadian = Math.toRadians(7.2);
//        double latBLRadian = Math.toRadians(46.2);
//        double lonTRRadian = Math.toRadians(7.8);
//        double latTRRadian = Math.toRadians(46.8);

        
//        Point bl = p.project(new PointGeo(lonBLRadian, latBLRadian));
//        Point tr = p.project(new PointGeo(lonTRRadian, latTRRadian));
        
        
        
        //0.17f
        int h = (int) Math.round((((dpi/2.54d)*100d)*(latTRRadian-latBLRadian)*Earth.RADIUS)/(25000d));
        double w = (tr.x()-bl.x())*h/(tr.y()-bl.y());
        
        System.out.println(w + " " + h);
        
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, (int)Math.round(w), (int)Math.round(h), dpi, Color.WHITE);
        
        //dem
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(pathHgtFile));
        ReliefShader rs = new ReliefShader(p, dem, new Vector3(-1, 1, 1));
        
        BufferedImage relief = rs.shadedRelief(bl, tr, (int)Math.round(w), (int)Math.round(h), dpi*(0.17d/2.54d));
//        BufferedImage relief = rs.shadedRelief(bl, tr, 800, 800, 0);

        
        // Dessin de la carte et stockage dans un fichier
        SwissPainter.painter().drawMap(map, canvas);
        BufferedImage plan = canvas.image();

        //**************************************************************
        //test
        
        try {
            System.out.println("plan.png");
            ImageIO.write(plan, "png", new File("plan.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            System.out.println("relief.png");
            ImageIO.write(relief, "png", new File("relief.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < relief.getWidth(); i++) {
            for (int j = 0; j < relief.getHeight(); j++) {
                plan.setRGB(i, j, Color.mix(Color.rgb(relief.getRGB(i, j)), Color.rgb(plan.getRGB(i, j))).awtColor().getRGB());
            }
        }
        
        try {
            System.out.println("total.png");
            ImageIO.write(plan, "png", new File(pathDestination));
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        System.out.println("    Temps total :"
                + ((System.currentTimeMillis() - tStart) / 1000.0));
    }

}
