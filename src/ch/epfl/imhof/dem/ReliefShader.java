package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * classe creant une bufferedImage qui represente le terrain en 3d
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 */
public class ReliefShader {
    private Projection projection;
    private DigitalElevationModel dem;
    private Vector3 direction;

    /**
     * construit un objet ReliefShader
     * 
     * @param projection
     *            une projection quelconque (CH1903 dans notre cas)
     * @param dem
     *            un objet qui represente un modele d elevation
     * @param direction
     *            le vecteur utilise pour la creation des ombres
     */
    public ReliefShader(Projection projection, DigitalElevationModel dem,
            Vector3 direction) {
        this.projection = projection;
        this.dem = dem;
        this.direction = direction.normalized();
    }

    /**
     * @param bg
     *            le point bas-gauche en coordonee du plan
     * @param hd
     *            le point bas-gauche en coordonee du plan
     * @param width
     *            la largeur de l image
     * @param height
     *            la hauteur de l image
     * @param rayon
     *            le rayon de floutage
     * @return une image de type BufferedImage qui represente le modele du
     *         terrain
     */
    public BufferedImage shadedRelief(Point bg, Point hd, int width,
            int height, double rayon) {

        BufferedImage b = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Function<Point, Point> coordChange;

        if (rayon == 0) {
            coordChange = Point.alignedCoordinateChange(new Point(0, height),
                    bg, new Point(width, 0), hd);
            b = brutRelief(width, height, coordChange);
        } else {
            float[] r = getNoyau(rayon);
            int a = r.length / 2;
            // determiner tampon
            coordChange = Point.alignedCoordinateChange(new Point(a, height + a), bg, new Point(width + a, a), hd);

            b = brutRelief(width + 2 * a, height + 2 * a, coordChange);
            b = floute(b, r);
            b = b.getSubimage(a, a, width, height);
            System.out.println(b.getWidth() + " " + b.getHeight());
        }
        return b;

    }

    /**
     * @param tl
     * @param width
     * @param height
     * @param coordChange
     * @return le relief brut
     */
    private BufferedImage brutRelief(int width, int height,
            Function<Point, Point> coordChange) {

        BufferedImage b = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        System.out.println(width + "  " + height);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // System.out.println(i + " " + j);

                Vector3 normal = this.dem.normalAt(
                        this.projection.inverse(coordChange.apply(new Point(j,
                                i)))).normalized();

                double a = this.direction.scalarProduct(normal);

                b.setRGB(
                        j,
                        i,
                        Color.rgb((1d / 2d) * (a + 1d), (1d / 2d) * (a + 1d),
                                (1d / 2d) * (0.7d * a + 1d)).awtColor()
                                .getRGB());
            }
        }

        return b;
    }

    /**
     * @param rayon
     * @return le noyau de floutage
     */
    private float[] getNoyau(double rayon) {
        double phi = rayon / 3d;
        int n = (int) (2 * Math.ceil(rayon) + 1);
        float[] noyau = new float[n];
        n = (n - 1) / 2;
        float total = 0;
        for (int i = 0; i < noyau.length; ++i) {
            double x = i-n;
            noyau[i] = (float) Math.exp(-1f * Math.pow(x, 2) / (2f * Math.pow(phi, 2)));
            total += noyau[i];
        }
        for (int i = 0; i < noyau.length; i++) {
            noyau[i] = noyau[i] / total;
        }
        return noyau;
    }

    /**
     * @param image
     * @param noyau
     * @return une image floutée
     */
    private BufferedImage floute(BufferedImage image, float[] noyau) {
        
        Kernel k = new Kernel(1, noyau.length, noyau);
        ConvolveOp cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
        image = cop.filter(image, null);

        k = new Kernel(noyau.length, 1, noyau);
        cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
        return cop.filter(image, null);
    }
}
