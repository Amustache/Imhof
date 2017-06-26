package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public abstract class RoadPainterGenerator {
    // Non instanciale
    private RoadPainterGenerator() {
    }

    /**
     * 
     * @param specs
     *            Specifications de routes
     * @return Peintre correspondant
     */
    public static Painter painterForRoads(RoadSpec... specs) {
        Painter painter = (m, c) -> {
        };

        for (RoadSpec roadSpec : specs) {
            painter = painter.above(Painter.line(roadSpec.innerWidth,
                    roadSpec.innerColor, LineCap.ROUND, LineJoin.ROUND).when(
                    Filters.tagged("bridge").and(roadSpec.filter)));
        }

        for (RoadSpec roadSpec : specs) {
            painter = painter.above(Painter.line(
                    roadSpec.innerWidth + 2 * roadSpec.outerWidth,
                    roadSpec.outerColor, LineCap.BUTT, LineJoin.ROUND,
                    new float[0]).when(
                    Filters.tagged("bridge").and(roadSpec.filter)));
        }

        for (RoadSpec roadSpec : specs) {
            painter = painter.above(Painter.line(roadSpec.innerWidth,
                    roadSpec.innerColor, LineCap.ROUND, LineJoin.ROUND).when(
                    Filters.tagged("bridge")
                            .negate()
                            .and(Filters.tagged("tunnel").negate()
                                    .and(roadSpec.filter))));
        }

        for (RoadSpec roadSpec : specs) {
            painter = painter.above(Painter.line(
                    roadSpec.innerWidth + 2 * roadSpec.outerWidth,
                    roadSpec.outerColor, LineCap.ROUND, LineJoin.ROUND,
                    new float[0]).when(
                    Filters.tagged("bridge")
                            .negate()
                            .and(Filters.tagged("tunnel").negate()
                                    .and(roadSpec.filter))));
        }

        for (RoadSpec roadSpec : specs) {
            painter = painter.above(Painter.line(roadSpec.innerWidth / 2,
                    roadSpec.outerColor, LineCap.BUTT, LineJoin.ROUND,
                    roadSpec.innerWidth * 2, roadSpec.innerWidth * 2).when(
                    Filters.tagged("tunnel").and(roadSpec.filter)));
        }

        return painter.layered();
    }

    /**
     * Decrit le dessin d'un type de route donne
     */
    public static class RoadSpec {
        private final Predicate<Attributed<?>> filter;
        private final float innerWidth, outerWidth;
        private final Color innerColor, outerColor;

        /**
         * Genere une specification de route
         * 
         * @param filter
         *            Filtres
         * @param innerWidth
         *            Largeur de trait interne
         * @param innerColor
         *            Couleur de trait interne
         * @param outerWidth
         *            Largeur de trait externe
         * @param outerColor
         *            Couleur de trait externe
         */
        public RoadSpec(Predicate<Attributed<?>> filter, float innerWidth,
                Color innerColor, float outerWidth, Color outerColor) {
            this.filter = filter;
            this.innerWidth = innerWidth;
            this.innerColor = innerColor;
            this.outerWidth = outerWidth;
            this.outerColor = outerColor;
        }
    }
}
