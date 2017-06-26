package ch.epfl.imhof.threads;

import java.awt.image.BufferedImage;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.Projection;

public final class MapThread extends Thread {
    private final String name = "Map";
    private Thread t;
    private final String pathOSMFile;
    private final Projection p;
    private final Point bl, tr;
    private final int h, w;
    private final int dpi;

    public MapThread(String pathOSMFile, Point bl, Point tr, int h, int w,
            int dpi, Projection p) {
        this.pathOSMFile = pathOSMFile;
        this.p = p;
        this.bl = bl;
        this.tr = tr;
        this.h = h;
        this.w = w;
        this.dpi = dpi;

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
            Map map = new OSMToGeoTransformer(p).transform(OSMMapReader
                    .readOSMFile(pathOSMFile, true));

            // Generation de la toile
            Java2DCanvas canvas = new Java2DCanvas(bl, tr, w, h, dpi,
                    Color.WHITE);

            // Dessin de la carte et stockage
            SwissPainter.painter().drawMap(map, canvas);
            BufferedImage plan = canvas.image();
            
            MainThread.setPlan(plan);
            
            Thread.sleep(1);
        } catch (Exception e) {
            System.out.println(this.name + ": error (" + e + ")");
        }
    }
}
