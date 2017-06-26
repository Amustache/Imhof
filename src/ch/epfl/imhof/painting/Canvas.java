package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 *         Interface de Toile
 */
public interface Canvas {
    /**
     * Dessine la PolyLine
     * 
     * @param polyligne
     *            PolyLine a dessiner
     * @param style
     *            Style de la PolyLine
     */
    abstract void drawPolyLine(PolyLine polyligne, LineStyle style);

    /**
     * Dessine le Polygon
     * 
     * @param polygon
     *            Polygon a dessiner
     * @param couleur
     *            Style du Polygon
     */
    abstract void drawPolygon(Polygon polygon, Color couleur);
}
