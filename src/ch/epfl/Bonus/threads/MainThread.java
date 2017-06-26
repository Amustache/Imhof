package ch.epfl.imhof.threads;

import java.awt.image.BufferedImage;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public final class MainThread extends Thread {
    private final String name = "Main";
    private Thread t;
    private final String pathOSMFile;
    private final String pathHGTFile;
    private final double lonBLRadian;
    private final double latBLRadian;
    private final double lonTRRadian;
    private final double latTRRadian;
    private final int dpi;
    private final String pathDestination;
    private final Projection p = new CH1903Projection();

    private static BufferedImage plan;
    private static BufferedImage relief;

    public MainThread(String[] args) {
        pathOSMFile = args[0]; // Chemin du fichier OSM
        pathHGTFile = args[1]; // Chemin du fichier HGT

        // Coordonnees bas-gauche
        lonBLRadian = Math.toRadians(Double.parseDouble(args[2]));
        latBLRadian = Math.toRadians(Double.parseDouble(args[3]));

        // Coordonnees haut-droit
        lonTRRadian = Math.toRadians(Double.parseDouble(args[4]));
        latTRRadian = Math.toRadians(Double.parseDouble(args[5]));

        // Resolution
        dpi = Integer.parseInt(args[6]);

        // Chemin du fichier genere
        pathDestination = args[7];
        System.out.println(this.name + ": born");
    }

    @Override
    public void start() {
        if (this.t == null) {
            this.t = new Thread(this, this.name);
            this.t.start();
            System.out.println(this.name + ": start");
        }
    }

    @Override
    public void run() {
        System.out.println(this.name + ": run");
        try {
            // Preparation de la toile
            Point bl = p.project(new PointGeo(lonBLRadian, latBLRadian));
            Point tr = p.project(new PointGeo(lonTRRadian, latTRRadian));

            // Generation de la hauteur et de la largeur
            int h = (int) Math.round((((dpi / 2.54d) * 1000d)
                    * (latTRRadian - latBLRadian) * Earth.RADIUS) / (25000d));
            int w = (int) Math.round((tr.x() - bl.x()) * h / (tr.y() - bl.y()));

            MapThread map = new MapThread(pathOSMFile, bl, tr, h, w, dpi, p);
            map.start();

            ReliefThread relief = new ReliefThread(pathHGTFile, bl, tr, w, h,
                    w, p);
            relief.start();
            
//            try {
//                map.join(); relief.join();
//            } catch (Exception e) {
//                System.out.println("Error:" + e);
//            }
//
//            DrawingThread drawing = new DrawingThread(MainThread.plan,
//                    MainThread.relief, pathDestination);
//            drawing.start();

            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(this.name + ": error (" + e + ")");
        }
    }

    public static void setPlan(BufferedImage plan) {
        MainThread.plan = plan;
    }

    public static void setRelief(BufferedImage relief) {
        MainThread.relief = relief;
    }

}
