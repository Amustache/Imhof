package ch.epfl.imhof.painting;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Painter
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public interface Painter {
    /**
     * Dessine la Map
     * 
     * @param map
     *            Map a dessiner
     * @param canvas
     *            Le Canvas sur lequel dessiner
     */
    public void drawMap(Map map, Canvas canvas);

    /**
     * 
     * @param couleur
     *            Couleur de dessin
     * @return Peintre essinant l'interieur de tous les Polygons de la Map avec
     *         la couleur correspondante
     */
    public static Painter polygon(Color couleur) {
        return (m, c) -> {
            for (int i = 0; i < m.polygons().size(); ++i) {
                c.drawPolygon(m.polygons().get(i).value(), couleur);
            }
        };
    }

    /**
     * 
     * @param style
     *            Le style a appliquer
     * @return Peintre dessinant toutes les lignes de la carte avec le style a
     *         appliquer
     */
    public static Painter line(LineStyle style) {
        return (m, c) -> {
            for (int i = 0; i < m.polyLines().size(); ++i) {
                c.drawPolyLine(m.polyLines().get(i).value(), style);
            }
        };
    }

    /**
     * 
     * @param width
     *            Largeur du trait
     * @param color
     *            Couleur du trait
     * @param cap
     *            LineCap du trait
     * @param join
     *            LineJoin du trait
     * @param pattern
     *            Pattern du trait
     * @return Peintre dessinant toutes les lignes de la carte avec le style a
     *         appliquer
     */
    public static Painter line(float width, Color color, LineCap cap,
            LineJoin join, float... pattern) {
        return (m, c) -> {
            for (int i = 0; i < m.polyLines().size(); ++i) {
                c.drawPolyLine(m.polyLines().get(i).value(), new LineStyle(
                        width, color, cap, join, pattern));
            }
        };
    }

    /**
     * 
     * @param width
     *            Largeur du trait
     * @param color
     *            Couleur du trait
     * @return Peintre dessinant toutes les lignes de la carte avec la largeur
     *         de trait et la couleur, et des parametres par defaut
     */
    public static Painter line(float width, Color color) {
        return (m, c) -> {
            for (int i = 0; i < m.polyLines().size(); ++i) {
                c.drawPolyLine(m.polyLines().get(i).value(), new LineStyle(
                        width, color));
            }
        };
    }

    /**
     * 
     * @param style
     *            Le style a appliquer
     * @return Peintre dessinant les pourtours de l'enveloppe et des trous de
     *         tous les Polygon de la carte avec le style a appliquer
     */
    public static Painter outline(LineStyle style) {
        return (m, c) -> {
            for (int i = 0; i < m.polygons().size(); ++i) {
                    c.drawPolyLine(m.polygons().get(i).value().shell(), style);
                for (int j = 0; j < m.polygons().get(i).value().holes().size(); ++j) {
                    c.drawPolyLine(m.polygons().get(i).value().holes().get(j),
                            style);
                }

            }
        };
    }

    /**
     * 
     * @param style
     *            Le style a appliquer
     * @return Peintre dessinant les pourtours de l'enveloppe et des trous de
     *         tous les Polygon de la carte avec le style a appliquer
     */
    public static Painter outline(float width, Color color, LineCap cap,
            LineJoin join, float... pattern) {
        return (m, c) -> {
            for (int i = 0; i < m.polygons().size(); ++i) {
                    c.drawPolyLine(m.polygons().get(i).value().shell(),
                            new LineStyle(width, color, cap, join, pattern));
                for (int j = 0; j < m.polygons().get(i).value().holes().size(); ++j) {
                    c.drawPolyLine(m.polygons().get(i).value().holes().get(j),
                            new LineStyle(width, color, cap, join, pattern));
                }
            }
        };
    }

    /**
     * 
     * @param style
     *            Le style a appliquer
     * @return Peintre dessinant les pourtours de l'enveloppe et des trous de
     *         tous les Polygon de la carte avec la largeur de trait et la
     *         couleur, et des parametres par defaut
     */
    public static Painter outline(float width, Color color) {
        return (m, c) -> {
            for (int i = 0; i < m.polygons().size(); ++i) {
                c.drawPolyLine(m.polygons().get(i).value().shell(),
                        new LineStyle(width, color));
                for (int j = 0; j < m.polygons().get(i).value().holes().size(); ++j) {
                    c.drawPolyLine(m.polygons().get(i).value().holes().get(j),
                            new LineStyle(width, color));
                }
            }
        };
    }

    // *********************************

    /**
     * 
     * @param p
     *            Predicat
     * @return Peintre se comportant comme celui auquel on l'applique en ne
     *         considerant que les elements satisfaisant le predicat
     */
    public default Painter when(Predicate<Attributed<?>> p) {
        return (m, c) -> {
            // flots
            drawMap(new Map(m.polyLines().stream().filter(p)
                    .collect(Collectors.toList()), m.polygons().stream()
                    .filter(p).collect(Collectors.toList())), c);
        };
    }

    /**
     * 
     * @param paint
     *            Peintre
     * @return Peintre dessinant d'abord la carte produite par ce second peintre
     *         puis, par dessus, la carte produite par le premier peintre
     */
    public default Painter above(Painter paint) {
        return (m, c) -> {

            paint.drawMap(m, c);
            drawMap(m, c);

        };
    }

    /**
     * 
     * @return Peintre utilisant l'attribut layer attaché aux entités de la
     *         carte pour la dessiner par couches, c-à-d en dessinant d'abord
     *         tous les entités de la couche –5, puis celle de la couche –4, et
     *         ainsi de suite jusqu'à la couche +5
     */
    public default Painter layered() {
        return (m, c) -> {
            for (int i = -5; i < 6; ++i) {
                when(Filters.onLayer(i)).drawMap(m, c);
            }
        };
    }
}
