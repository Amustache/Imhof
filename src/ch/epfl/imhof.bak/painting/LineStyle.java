package ch.epfl.imhof.painting;

/**
 * Style de contour
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class LineStyle {
    /**
     * Les differents types de terminaison de ligne
     *
     */
    public enum lineCap {
        BUTT, ROUND, SQUARE;
    }

    /**
     * Les differents types de coin de ligne
     *
     */
    public enum lineJoin {
        MITER, ROUND, BEVEL;
    }

    private final float width;
    public final Color color;
    private final lineCap cap;
    private final lineJoin join;
    private final float[] pattern;

    private final static float[] EMPTY_PATTERN = new float[0];

    /**
     * Cree un nouveau style de ligne avec les arguments passes en parametre
     * 
     * @param width
     *            Largeur de ligne
     * @param color
     *            Couleur de ligne
     * @param cap
     *            Type de terminaison
     * @param join
     *            Type de coin
     * @param pattern
     *            Dashing pattern
     * @throws IllegalArgumentException
     *             Si largeur du trait est negative OU si l'un des elements du
     *             pattern est negatif
     */
    public LineStyle(float width, Color color, lineCap cap, lineJoin join,
            float[] pattern) throws IllegalArgumentException {
        if (width < 0)
            throw new IllegalArgumentException("Largeur du trait negative : "
                    + width);
        for (int i = 0; i < pattern.length; ++i) {
            if (pattern[i] < 0)
                throw new IllegalArgumentException(
                        "L'un des elements du pattern est negatif : [" + i
                                + "]=" + pattern[i]);
        }

        this.width = width;
        this.color = color;
        this.cap = cap;
        this.join = join;
        this.pattern = pattern;
    }

    /**
     * Cree un style de ligne de base
     * 
     * @param width
     *            La largeur du trait
     * @param color
     *            La couleur du trait
     */
    public LineStyle(float width, Color color) {
        this(width, color, lineCap.BUTT, lineJoin.MITER, EMPTY_PATTERN);
    }

    /**
     * 
     * @return Largeur du trait
     */
    public float width() {
        return this.width;
    }

    /**
     * 
     * @return Couleur du trait
     */
    public Color color() {
        return this.color;
    }

    /**
     * 
     * @return Type de terminaison du trait
     */
    public lineCap cap() {
        return this.cap;
    }

    /**
     * 
     * @return Type de coin du trait
     */
    public lineJoin join() {
        return this.join;
    }

    /**
     * 
     * @return Type de pattern du trait
     */
    public float[] pattern() {
        float[] retour = new float[this.pattern.length];

        for (int i = 0; i < this.pattern.length; ++i) {
            retour[i] = this.pattern[i];
        }

        return retour;
    }

    /**
     * Cree un nouveau style de ligne identique a this, mais avec une largeur
     * differente
     * 
     * @param width
     *            La largeur du trait
     * @return Style de ligne
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(width, this.color(), this.cap(), this.join(),
                this.pattern());
    }

    /**
     * Cree un nouveau style de ligne identique a this, mais avec une couleur
     * differente
     * 
     * @param color
     *            La couleur du trait
     * @return Style de ligne
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(this.width(), color, this.cap(), this.join(),
                this.pattern());
    }

    /**
     * Cree un nouveau style de ligne identique a this, mais avec une
     * terminaison differente
     * 
     * @param cap
     *            La terminaison du trait
     * @return Style de ligne
     */
    public LineStyle withCap(lineCap cap) {
        return new LineStyle(this.width(), this.color(), cap, this.join(),
                this.pattern());
    }

    /**
     * Cree un nouveau style de ligne identique a this, mais avec un coin
     * different
     * 
     * @param join
     *            Le coin du trait
     * @return Style de ligne
     */
    public LineStyle withJoin(lineJoin join) {
        return new LineStyle(this.width(), this.color(), this.cap(), join,
                this.pattern());
    }

    /**
     * Cree un nouveau style de ligne identique a this, mais avec un pattern
     * different
     * 
     * @param pattern
     *            Le pattern du trait
     * @return Style de ligne
     */
    public LineStyle withPattern(float[] pattern) {
        return new LineStyle(this.width(), this.color(), this.cap(),
                this.join(), pattern);
    }
}
