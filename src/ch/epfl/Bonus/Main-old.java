package ch.epfl.imhof;

import java.awt.Desktop;
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
import ch.epfl.imhof.threads.MainThread;

/*
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 * Programme principal pour générer une carte !
 */
public final class Main {

    /**
     * Methode principale du programme
     * 
     * @param args
     *            Dans l'ordre : fichier OSM compresse; fichier HGT; longitude
     *            point bas-gauche; latitude point bas-gauche; longitude point
     *            haut-droite; latitude point haut-droite; resolution; nom du
     *            fichier a generer
     * @throws IOException
     *             Si fichier invalide
     * @throws SAXException
     *             Erreur dans la generation de map
     */
    public static final void main(String[] args) throws IOException,
            SAXException {
        long start = System.currentTimeMillis();
        /*String pathOSMFile = args[0]; // Chemin du fichier OSM
        String pathHGTFile = args[1]; // Chemin du fichier HGT

        // Coordonnees bas-gauche
        double lonBLRadian = Math.toRadians(Double.parseDouble(args[2]));
        double latBLRadian = Math.toRadians(Double.parseDouble(args[3]));

        // Coordonnees haut-droit
        double lonTRRadian = Math.toRadians(Double.parseDouble(args[4]));
        double latTRRadian = Math.toRadians(Double.parseDouble(args[5]));

        // Resolution
        int dpi = Integer.parseInt(args[6]);

        */// Chemin du fichier genere
        String pathDestination = args[7];/*

        Projection p = new CH1903Projection();

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Fin des déclarations");

        // *********************************************************************

        // Generation de la map
        Map map = new OSMToGeoTransformer(p).transform(OSMMapReader
                .readOSMFile(pathOSMFile, true)); // Lue depuis lausanne.osm.gz

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Map générée");

        // Preparation de la toile
        Point bl = p.project(new PointGeo(lonBLRadian, latBLRadian));
        Point tr = p.project(new PointGeo(lonTRRadian, latTRRadian));

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Toile préparée");

        // Generation de la hauteur et de la largeur
        int h = (int) Math.round((((dpi / 2.54d) * 1000d)
                * (latTRRadian - latBLRadian) * Earth.RADIUS) / (25000d));
        double w = (tr.x() - bl.x()) * h / (tr.y() - bl.y());

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": H/L générés");

        // Generation de la toile
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, (int) Math.round(w),
                Math.round(h), dpi, Color.WHITE);

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Toile générée");

        // Generation du relief
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(
                pathHGTFile));
        ReliefShader rs = new ReliefShader(p, dem, new Vector3(-1, 1, 1));

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Relief généré");

        // Dessin du relief
        BufferedImage relief = rs.shadedRelief(bl, tr, (int) Math.round(w),
                Math.round(h), dpi * (0.17d / 2.54d));

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Relief dessiné");

        // Dessin de la carte et stockage
        SwissPainter.painter().drawMap(map, canvas);
        BufferedImage plan = canvas.image();

        for (int i = 0; i < relief.getWidth(); i++) {
            for (int j = 0; j < relief.getHeight(); j++) {
                plan.setRGB(
                        i,
                        j,
                        Color.mix(Color.rgb(relief.getRGB(i, j)),
                                Color.rgb(plan.getRGB(i, j))).awtColor()
                                .getRGB());
            }
        }

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Carte dessinée et stockée");

        // Enfin.
        try {
            ImageIO.write(plan, "png", new File(pathDestination));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(((System.currentTimeMillis() - start) / 1000.0)
                + ": Fichier généré");*/
        
        MainThread main = new MainThread(args);
        main.start();

//        Desktop dp = Desktop.getDesktop();
//        dp.open(new File(pathDestination));
    }

}
