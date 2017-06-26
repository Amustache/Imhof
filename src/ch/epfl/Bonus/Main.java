//
//package ch.epfl.imhof;
//
//import java.awt.Desktop;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
//import org.xml.sax.SAXException;
//
//import ch.epfl.imhof.dem.Earth;
//import ch.epfl.imhof.dem.HGTDigitalElevationModel;
//import ch.epfl.imhof.dem.ReliefShader;
//import ch.epfl.imhof.geometry.Point;
//import ch.epfl.imhof.osm.OSMMapReader;
//import ch.epfl.imhof.osm.OSMToGeoTransformer;
//import ch.epfl.imhof.painting.Color;
//import ch.epfl.imhof.painting.Java2DCanvas;
//import ch.epfl.imhof.projection.CH1903Projection;
//import ch.epfl.imhof.projection.Projection;
//import ch.epfl.imhof.threads.MainThread;
//
///*
// * @author Hugo Hueber (246095)
// * @author Maxime Pisa (247650)
// * 
// * Programme principal pour générer une carte !
// */
//public final class Main {
//
//    /**
//     * Methode principale du programme
//     * 
//     * @param args
//     *            Dans l'ordre : fichier OSM compresse; fichier HGT; longitude
//     *            point bas-gauche; latitude point bas-gauche; longitude point
//     *            haut-droite; latitude point haut-droite; resolution; nom du
//     *            fichier a generer
//     * @throws IOException
//     *             Si fichier invalide
//     * @throws SAXException
//     *             Erreur dans la generation de map
//     */
//    public static final void main(String[] args) throws IOException,
//            SAXException {
//        long start = System.currentTimeMillis();
//        /*String pathOSMFile = args[0]; // Chemin du fichier OSM
//        String pathHGTFile = args[1]; // Chemin du fichier HGT
//
//        // Coordonnees bas-gauche
//        double lonBLRadian = Math.toRadians(Double.parseDouble(args[2]));
//        double latBLRadian = Math.toRadians(Double.parseDouble(args[3]));
//
//        // Coordonnees haut-droit
//        double lonTRRadian = Math.toRadians(Double.parseDouble(args[4]));
//        double latTRRadian = Math.toRadians(Double.parseDouble(args[5]));
//
//        // Resolution
//        int dpi = Integer.parseInt(args[6]);
//
//        */// Chemin du fichier genere
//        String pathDestination = args[7];/*
//
//        Projection p = new CH1903Projection();
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Fin des déclarations");
//
//        // *********************************************************************
//
//        // Generation de la map
//        Map map = new OSMToGeoTransformer(p).transform(OSMMapReader
//                .readOSMFile(pathOSMFile, true)); // Lue depuis lausanne.osm.gz
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Map générée");
//
//        // Preparation de la toile
//        Point bl = p.project(new PointGeo(lonBLRadian, latBLRadian));
//        Point tr = p.project(new PointGeo(lonTRRadian, latTRRadian));
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Toile préparée");
//
//        // Generation de la hauteur et de la largeur
//        int h = (int) Math.round((((dpi / 2.54d) * 1000d)
//                * (latTRRadian - latBLRadian) * Earth.RADIUS) / (25000d));
//        double w = (tr.x() - bl.x()) * h / (tr.y() - bl.y());
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": H/L générés");
//
//        // Generation de la toile
//        Java2DCanvas canvas = new Java2DCanvas(bl, tr, (int) Math.round(w),
//                Math.round(h), dpi, Color.WHITE);
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Toile générée");
//
//        // Generation du relief
//        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(
//                pathHGTFile));
//        ReliefShader rs = new ReliefShader(p, dem, new Vector3(-1, 1, 1));
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Relief généré");
//
//        // Dessin du relief
//        BufferedImage relief = rs.shadedRelief(bl, tr, (int) Math.round(w),
//                Math.round(h), dpi * (0.17d / 2.54d));
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Relief dessiné");
//
//        // Dessin de la carte et stockage
//        SwissPainter.painter().drawMap(map, canvas);
//        BufferedImage plan = canvas.image();
//
//        for (int i = 0; i < relief.getWidth(); i++) {
//            for (int j = 0; j < relief.getHeight(); j++) {
//                plan.setRGB(
//                        i,
//                        j,
//                        Color.mix(Color.rgb(relief.getRGB(i, j)),
//                                Color.rgb(plan.getRGB(i, j))).awtColor()
//                                .getRGB());
//            }
//        }
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Carte dessinée et stockée");
//
//        // Enfin.
//        try {
//            ImageIO.write(plan, "png", new File(pathDestination));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
//                + ": Fichier généré");*/
//        
//        MainThread main = new MainThread(args);
//        main.start();
//
////        Desktop dp = Desktop.getDesktop();
////        dp.open(new File(pathDestination));
//    }
//
//}
//

/*
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 */


package ch.epfl.imhof;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.DuckReader;
import ch.epfl.imhof.geometry.DuckWriter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public class Main {
    
    private static String[] argument = new String[8];
    
    private static final int TEXT_WIDTH = 200;
    private static final Projection p = new CH1903Projection();

    
    private static void doduck(int a){
        Map map = null;
        switch (a) {
        case 0:
            //lausanne
            try {
                map = new OSMToGeoTransformer(p)
                        .transform(OSMMapReader.readOSMFile("lausanne.osm.gz", true));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SAXException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // Lue depuis lausanne.osm.gz
            
            try {
                DuckWriter.write(map, "lausanne");
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                        
            break;
        case 1:
            //bern
            try {
                map = new OSMToGeoTransformer(p)
                        .transform(OSMMapReader.readOSMFile("bern.osm.gz", true));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SAXException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // Lue depuis lausanne.osm.gz
            
            try {
                DuckWriter.write(map, "bern");
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case 2:
            //interlaken
            try {
                map = new OSMToGeoTransformer(p)
                        .transform(OSMMapReader.readOSMFile("interlaken.osm.gz", true));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SAXException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // Lue depuis lausanne.osm.gz
            
            try {
                DuckWriter.write(map, "interlaken");
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
            
        case 3:
            System.out.println("hey");
            //default
            try {
                map = new OSMToGeoTransformer(p)
                        .transform(OSMMapReader.readOSMFile(argument[0], true));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SAXException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // Lue depuis lausanne.osm.gz
            
            try {
                DuckWriter.write(map, argument[0]);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        }
    }
    
    
private static void dorunlol(String args[], boolean duck){
        
        String pathOsmFile = args[0];
        String pathHgtFile = args[1];
        
        double lonBLRadian = Math.toRadians(Double.parseDouble(args[2]));
        double latBLRadian = Math.toRadians(Double.parseDouble(args[3]));
        
        double lonTRRadian = Math.toRadians(Double.parseDouble(args[4]));
        double latTRRadian = Math.toRadians(Double.parseDouble(args[5]));
        
        int dpi = Integer.parseInt(args[6]);
        String pathDestination = args[7];
                
        
        //*********************************************************************
        long tStart = System.currentTimeMillis();
        System.out.println("Debut:" + tStart);
        
        Map map = null;
        if (!duck) {
            try {
                map = new OSMToGeoTransformer(p)
                        .transform(OSMMapReader.readOSMFile(pathOsmFile, true));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SAXException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // Lue depuis lausanne.osm.gz
        } else {
            try {
                map = DuckReader.readOSMFile(pathOsmFile, false);
            } catch (IOException | SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
 
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
        HGTDigitalElevationModel dem = null;
        try {
            dem = new HGTDigitalElevationModel(new File(pathHgtFile));
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ReliefShader rs = new ReliefShader(p, dem, new Vector3(-1, 1, 1));
        
        BufferedImage relief = rs.shadedRelief(bl, tr, (int)Math.round(w), Math.round(h), dpi*(0.17d/2.54d));
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

    
    private static void createUI(){
        JFrame w = new JFrame("Carte Java");
        
        

        // Panneau Lausanne
        JLabel lausL = new JLabel("Lausanne :", SwingConstants.CENTER);
        JButton lausB1 = new JButton("Run lausanne.osm.gz");
        lausB1.addActionListener(e -> dorunlol(new String[]{"lausanne.osm.gz", "N46E006.hgt", "6.5594", "46.5032", "6.6508", "46.5459",
                "300", "lausanne.png"}, false));
        JButton lausB2 = new JButton("Run lausanne.duck");
        lausB2.addActionListener(e -> dorunlol(new String[]{"lausanne.duck", "N46E006.hgt", "6.5594", "46.5032", "6.6508", "46.5459",
                "300", "lausanne.png"}, true));
        JButton lausB3 = new JButton("Creer Lausanne.duck");
        lausB3.addActionListener(e -> doduck(0));
        

        JPanel lausP = new JPanel(new FlowLayout());
        lausP.add(lausL);
        lausP.add(lausB1);
        lausP.add(lausB2);
        lausP.add(lausB3);
        

        // Panneau Bern
        JLabel bernL = new JLabel("Bern :", SwingConstants.CENTER);
        JButton bernB1 = new JButton("Run berne.osm.gz");
        bernB1.addActionListener(e -> dorunlol(new String[]{"berne.osm.gz", "N46E007.hgt", "7.3912", "46.9322", "7.4841", "46.9742",
                "300", "berne.png"}, false));
        JButton bernB2 = new JButton("Run berne.duck");
        bernB2.addActionListener(e -> dorunlol(new String[]{"berne.duck", "N46E007.hgt", "7.3912", "46.9322", "7.4841", "46.9742",
                "300", "berne.png"}, true));
        JButton bernB3 = new JButton("Creer berne.duck");
        bernB3.addActionListener(e -> doduck(1));

        JPanel bernP = new JPanel(new FlowLayout());
        bernP.add(bernL);
        bernP.add(bernB1);
        bernP.add(bernB2);
        bernP.add(bernB3);

        
        // Panneau Interlaken
        JLabel intL = new JLabel("Interlaken :", SwingConstants.CENTER);
        JButton intB1 = new JButton("Run interlaken.osm.gz");
        intB1.addActionListener(e -> dorunlol(new String[]{"interlaken.osm.gz", "N46E007.hgt", "7.8122", "46.6645", "7.9049", "46.7061",
                "300", "interlaken.png"}, false));
        JButton intB2 = new JButton("Run interlaken.duck");
        intB2.addActionListener(e -> dorunlol(new String[]{"interlaken.duck", "N46E007.hgt", "7.8122", "46.6645", "7.9049", "46.7061",
                "300", "interlaken.png"}, true));
        JButton intB3 = new JButton("Creer interlaken.duck");
        intB3.addActionListener(e -> doduck(2));

        JPanel intP = new JPanel(new FlowLayout());
        intP.add(intL);
        intP.add(intB1);
        intP.add(intB2);
        intP.add(intB3);
        

        

//        // Aligment des étiquettes
//        int lWidth = Math.max(osmL.getPreferredSize().width, hgtL.getPreferredSize().width);
//        osmL.setPreferredSize(new Dimension(lWidth, osmL.getPreferredSize().height));
//        hgtL.setPreferredSize(new Dimension(lWidth, hgtL.getPreferredSize().height));
        
        JLabel defL = new JLabel("Runconfig par defaut :", SwingConstants.CENTER);
        JButton defBut = new JButton("Run runconfig");
        defBut.addActionListener(e -> dorunlol(argument, false));
        JButton defBut2 = new JButton("Run runconfig.duck");
        defBut2.addActionListener(e -> dorunlol(new String[]{argument[0]+".duck", argument[1], argument[2], argument[3], argument[4], argument[5],
                argument[6], argument[7]}, true));
        JButton defButDuck = new JButton("Creer runconfig.duck");
        defButDuck.addActionListener(e -> doduck(3));
        
        JPanel defP = new JPanel(new FlowLayout());
        defP.add(defL);
        defP.add(defBut);
        defP.add(defBut2);
        defP.add(defButDuck);
        
        
        // Panneau principal perso
        JPanel mainP = new JPanel();
        
        mainP.setLayout(new BoxLayout(mainP, 3));
        
        mainP.add(lausP);
        mainP.add(bernP);
        mainP.add(intP);
        mainP.add(defP);
        
//        mainP.add(lausL);
//        mainP.add(lausB1);
//        mainP.add(lausB2);
//        mainP.add(lausB3);
//        
//        mainP.add(bernL);
//        mainP.add(bernB1);
//        mainP.add(bernB2);
//        mainP.add(bernB3);
//        
//        mainP.add(intL);
//        mainP.add(intB1);
//        mainP.add(intB2);
//        mainP.add(intB3);
        
        w.setContentPane(mainP);
        w.pack();
        w.setVisible(true);
        w.setLocationRelativeTo(null);
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * @param args
     * @throws IOException
     * @throws SAXException
     *          la methode principale du programme
     */
    public static void main(String[] args) throws IOException, SAXException {
        
        argument = args;
        
        SwingUtilities.invokeLater(() -> createUI());
        
//        String pathOsmFile = args[0];
//        String pathHgtFile = args[1];
//        
//        double lonBLRadian = Math.toRadians(Double.parseDouble(args[2]));
//        double latBLRadian = Math.toRadians(Double.parseDouble(args[3]));
//        
//        double lonTRRadian = Math.toRadians(Double.parseDouble(args[4]));
//        double latTRRadian = Math.toRadians(Double.parseDouble(args[5]));
//        
//        int dpi = Integer.parseInt(args[6]);
//        String pathDestination = args[7];
//        
//        Projection p = new CH1903Projection();
//        
//        //*********************************************************************
//        long tStart = System.currentTimeMillis();
//        System.out.println("Debut:" + tStart);
//        
//        Map map = new OSMToGeoTransformer(p)
//                .transform(OSMMapReader.readOSMFile(pathOsmFile, true)); // Lue depuis lausanne.osm.gz
// 
//        // La toile
//        Point bl = p.project(new PointGeo(lonBLRadian, latBLRadian));
//        Point tr = p.project(new PointGeo(lonTRRadian, latTRRadian));
////        
////        double lonBLRadian = Math.toRadians(7.2);
////        double latBLRadian = Math.toRadians(46.2);
////        double lonTRRadian = Math.toRadians(7.8);
////        double latTRRadian = Math.toRadians(46.8);
//
//        
////        Point bl = p.project(new PointGeo(lonBLRadian, latBLRadian));
////        Point tr = p.project(new PointGeo(lonTRRadian, latTRRadian));
//        
//        
//        
//        //0.17f
//        int h = (int) Math.round((((dpi/2.54d)*100d)*(latTRRadian-latBLRadian)*Earth.RADIUS)/(25000d));
//        double w = (tr.x()-bl.x())*h/(tr.y()-bl.y());
//        
//        System.out.println(w + " " + h);
//        
//        Java2DCanvas canvas = new Java2DCanvas(bl, tr, (int)Math.round(w), (int)Math.round(h), dpi, Color.WHITE);
//        
//        //dem
//        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(pathHgtFile));
//        ReliefShader rs = new ReliefShader(p, dem, new Vector3(-1, 1, 1));
//        
//        BufferedImage relief = rs.shadedRelief(bl, tr, (int)Math.round(w), Math.round(h), dpi*(0.17d/2.54d));
////        BufferedImage relief = rs.shadedRelief(bl, tr, 800, 800, 0);
//
//        
//        // Dessin de la carte et stockage dans un fichier
//        SwissPainter.painter().drawMap(map, canvas);
//        BufferedImage plan = canvas.image();
//
//        //**************************************************************
//        //test
//        
//        try {
//            System.out.println("plan.png");
//            ImageIO.write(plan, "png", new File("plan.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        try {
//            System.out.println("relief.png");
//            ImageIO.write(relief, "png", new File("relief.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        for (int i = 0; i < relief.getWidth(); i++) {
//            for (int j = 0; j < relief.getHeight(); j++) {
//                plan.setRGB(i, j, Color.mix(Color.rgb(relief.getRGB(i, j)), Color.rgb(plan.getRGB(i, j))).awtColor().getRGB());
//            }
//        }
//        
//        try {
//            System.out.println("total.png");
//            ImageIO.write(plan, "png", new File(pathDestination));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
// 
//        System.out.println("    Temps total :"
//                + ((System.currentTimeMillis() - tStart) / 1000.0));
    }
    
    

}
