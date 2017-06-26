package ch.epfl.imhof.threads;

import java.awt.image.BufferedImage;
import java.io.File;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;

public final class ReliefThread extends Thread {
    private final String name = "Relief";
    private Thread t;
    private final String pathHGTFile;
    private final Point bl, tr;
    private final int h, w, dpi;
    private final Projection p;

    public ReliefThread(String pathHGTFile, Point bl, Point tr, int w, int h,
            int dpi, Projection p) {
        this.pathHGTFile = pathHGTFile;
        this.bl = bl;
        this.tr = tr;
        this.h = h;
        this.w = w;
        this.dpi = dpi;
        this.p = p;
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
            // Generation du relief
            HGTDigitalElevationModel dem = new HGTDigitalElevationModel(
                    new File(pathHGTFile));
            ReliefShader rs = new ReliefShader(p, dem, new Vector3(-1, 1, 1));

            // Dessin du relief
            BufferedImage relief = rs.shadedRelief(bl, tr, w, h, dpi
                    * (0.17d / 2.54d));

            MainThread.setRelief(relief);

            Thread.sleep(1);
        } catch (Exception e) {
            System.out.println(this.name + ": error (" + e + ")");
        }
    }
}
