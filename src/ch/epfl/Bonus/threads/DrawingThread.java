package ch.epfl.imhof.threads;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.epfl.imhof.painting.Color;

public final class DrawingThread extends Thread {
    private final String name = "Drawing";
    private Thread t;
    private final BufferedImage plan, relief;
    private final String pathDestination;

    public DrawingThread(BufferedImage plan, BufferedImage relief,
            String pathDestination) {
        this.plan = plan;
        this.relief = relief;
        this.pathDestination = pathDestination;
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
            for (int i = 0; i < this.relief.getWidth(); i++) {
                for (int j = 0; j < this.relief.getHeight(); j++) {
                    this.plan.setRGB(
                            i,
                            j,
                            Color.mix(Color.rgb(this.relief.getRGB(i, j)),
                                    Color.rgb(this.plan.getRGB(i, j)))
                                    .awtColor().getRGB());
                }
            }

            try {
                ImageIO.write(this.plan, "png", new File(this.pathDestination));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(this.name + ": error (" + e + ")");
        }
    }
}
