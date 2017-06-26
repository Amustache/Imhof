package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    abstract void drawPolyLine(PolyLine polyligne, LineStyle style);

    abstract void drawPolygon(Polygon polygon, Color couleur);
}
