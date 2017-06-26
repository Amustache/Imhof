package ch.epfl.imhof.painting;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

public class Java2DCanvas implements Canvas {
    private final Function<Point, Point> coordChange;
    BufferedImage image;
    Graphics2D ctx;

    public Java2DCanvas(Point BL, Point TR, int width, int height, int dpi,
            Color bgColor) {
        
        this.coordChange = Point.alignedCoordinateChange(BL, new Point(height,
                0), TR, new Point(0, width));

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ctx = image.createGraphics();
        
        ctx.scale(dpi/72d, dpi/72d);
        
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.setBackground(bgColor);
    }

    @Override
    public void drawPolyLine(PolyLine polyligne, LineStyle style) {

        BasicStroke bs = new BasicStroke(style.width(), style.cap().ordinal(),
                style.join().ordinal(), 10.0f, style.pattern(), 0f);
        ctx.setStroke(bs);
        ctx.setColor(style.color().awtColor());

        Path2D.Double p2d = listToPath(polyligne.points());

        if (polyligne.isClosed()) {
            p2d.closePath();
        }

        ctx.draw(p2d);

        // int[] x = new int[polyligne.points().size()];
        // int[] y = new int[polyligne.points().size()];
        //
        // for (int i = 0; i < polyligne.points().size(); ++i) {
        // x[i] = (int) this.coordChange.apply(polyligne.points().get(i)).x();
        // y[i] = (int) this.coordChange.apply(polyligne.points().get(i)).y();
        // }
        //
        // ctx.drawPolyline(x, y, polyligne.points().size());

    }

    @Override
    public void drawPolygon(Polygon polygon,
            ch.epfl.imhof.painting.Color couleur) {

        ctx.setColor(couleur.awtColor());
        
        Path2D.Double p2d = listToPath(polygon.shell().points());

        p2d.closePath();

        Area area = new Area(p2d);
        
        Path2D.Double p2dtmp;
        for (int i = 0; i < polygon.holes().size(); ++i) {
            p2dtmp = listToPath(polygon.shell().points());
            p2dtmp.closePath();
            area.subtract(new Area(p2dtmp));
        }
        
        ctx.draw(area);
    }

    public BufferedImage image() {
        return image;
    }
    
    private Path2D.Double listToPath(List<Point> list){
        Path2D.Double p2d = new Path2D.Double();

        p2d.moveTo(this.coordChange.apply(list.get(0)).x(),
                this.coordChange.apply(list.get(0)).y());
        for (int i = 1; i < list.size(); ++i) {
            p2d.lineTo(this.coordChange.apply(list.get(i)).x(),
                    this.coordChange.apply(list.get(i)).y());
        }
        
        return p2d;
    }
}
