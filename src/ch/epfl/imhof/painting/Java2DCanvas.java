package ch.epfl.imhof.painting;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 *         Toile 2D
 *
 */
public class Java2DCanvas implements Canvas {
    private final Function<Point, Point> coordChange;
    private final BufferedImage image;
    private final Graphics2D ctx;
    private final int dpi;

    /**
     * Dessine les primitives qu'on lui demande de dessiner dans une image
     * discrete.
     * 
     * @param BL
     *            Coordonnees en bas a gauche
     * @param TR
     *            Coordonnees en haut a droite
     * @param width
     *            Largeur en pixels
     * @param height
     *            Hauteur en pixels
     * @param dpi
     *            Resolution en points par pouce
     * @param color
     *            Couleur de fond
     */
    public Java2DCanvas(Point BL, Point TR, int width, int height, int dpi,
            ch.epfl.imhof.painting.Color color) throws IllegalArgumentException {
        if (width < 0 || height < 0 || dpi < 0)
            throw new IllegalArgumentException();
        


        this.dpi = dpi;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ctx = image.createGraphics();
        
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.setBackground(color.awtColor());
        ctx.setColor(color.awtColor());
        ctx.fillRect(0, 0, width, height);
        

        
        ctx.scale(dpi / 72d, dpi / 72d);



        

        this.coordChange = Point.alignedCoordinateChange(BL, new Point(0, height*72f/(dpi)),
                TR, new Point(width*72f/(dpi), 0));
        //ctx.translate(0, height);

        
    }

    /**
     * Dessine la PolyLine
     * 
     * @param polyligne
     *            PolyLine a dessiner
     * @param style
     *            Style de la PolyLine
     */
    @Override
    public void drawPolyLine(PolyLine polyligne, LineStyle style) {
        ctx.setColor(style.color().awtColor());

        BasicStroke bs;
        
        if (style.pattern().length == 0) {
            bs = new BasicStroke(style.width(), style.cap().ordinal(), style.join().ordinal(), 10f);
        } else {
            bs = new BasicStroke(style.width(), style.cap().ordinal(),
                    style.join().ordinal(), 10.0f, style.pattern(), 0f);
        }
        ctx.setStroke(bs);

        Path2D.Double p2d = listToPath(polyligne.points());

        if (polyligne.isClosed()) {
            p2d.closePath();
        }
        
        
//      ctx.scale(dpi / 72d, dpi / 72d);
        ctx.draw(p2d);
//      ctx.scale(72d*dpi, 72d*dpi);
        
        
    }

    /**
     * Dessine le Polygon
     * 
     * @param polygon
     *            Polygon a dessiner
     * @param couleur
     *            Style du Polygon
     */
    @Override
    public void drawPolygon(Polygon polygon,
            ch.epfl.imhof.painting.Color couleur) {

        ctx.setColor(couleur.awtColor());

        Path2D.Double p2d = listToPath(polygon.shell().points());

        p2d.closePath();

        Area area = new Area(p2d);

        Path2D.Double p2dtmp;
        for (int i = 0; i < polygon.holes().size(); ++i) {
            p2dtmp = listToPath(polygon.holes().get(i).points());
            p2dtmp.closePath();
            area.subtract(new Area(p2dtmp));
        }

        ctx.fill(area);
    }

    /**
     * 
     * @return L'image dessinee
     */
    public BufferedImage image() {
        return this.image;
    }

    private Path2D.Double listToPath(List<Point> list) {
        Path2D.Double p2d = new Path2D.Double();
        


        p2d.moveTo(this.coordChange.apply(list.get(0)).x(), this.coordChange
                .apply(list.get(0)).y());
        Point p;
        for (int i = 1; i < list.size(); ++i) {
            p = this.coordChange.apply(list.get(i));
            p2d.lineTo(p.x(),
                    p.y());
        }

        return p2d;
    }
}
