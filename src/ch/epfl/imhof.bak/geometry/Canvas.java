package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;

public abstract class Canvas {
    abstract void drawPolyLine(PolyLine polyligne, LineStyle style);

    abstract void drawPolygon(PolyLine polyligne, Color couleur);
}
