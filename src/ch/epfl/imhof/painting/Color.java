package ch.epfl.imhof.painting;

/**
 * Couleur RGB
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class Color {
    private final double red, green, blue;

    /**
     * Construit une couleur RGB
     * 
     * @param r
     *            Nuance de rouge entre 0 et 1 inclus
     * @param g
     *            Nuance de vert entre 0 et 1 inclus
     * @param b
     *            Nuance de bleu entre 0 et 1 inclus
     * @throws IllegalArgumentException
     *             Si les parametres sont en dehors de la plage 0 et 1 inclus
     */
    private Color(double r, double g, double b) throws IllegalArgumentException {
        if (r < 0 || r > 1)
            throw new IllegalArgumentException("Mauvais parametre rouge : " + r);
        if (g < 0 || g > 1)
            throw new IllegalArgumentException("Mauvais parametre vert : " + g);
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("Mauvais parametre bleu : " + b);

        this.red = r;
        this.green = g;
        this.blue = b;
    }

    /**
     * Retourne une couleur rouge pure
     */
    public final static Color RED = new Color(1, 0, 0);

    /**
     * Retourne une couleur verte pure
     */
    public final static Color GREEN = new Color(0, 1, 0);

    /**
     * Retourne une couleur bleue pure
     */
    public final static Color BLUE = new Color(0, 0, 1);

    /**
     * Retourne une couleur noire pure
     */
    public final static Color BLACK = new Color(0, 0, 0);

    /**
     * Retourne une couleur blanche pure
     */
    public final static Color WHITE = new Color(1, 1, 1);

    /**
     * Retourne une couleur grise
     * 
     * @param g
     *            Entre 0 et 1 inclus
     * @return Couleur grise de nuance g
     * @throws IllegalArgumentException
     *             Si le parametre est en dehors de la plage 0 et 1 inclus
     */
    public static Color gray(double g) throws IllegalArgumentException {
        return new Color(g, g, g);
    }

    /**
     * Retourune une couleur RGB
     * 
     * @param r
     *            Nuance de rouge entre 0 et 1 inclus
     * @param g
     *            Nuance de vert entre 0 et 1 inclus
     * @param b
     *            Nuance de bleu entre 0 et 1 inclus
     * @return Couleur correspondante aux parametres
     * @throws IllegalArgumentException
     *             Si les parametres sont en dehors de la plage 0 et 1 inclus
     */
    public static Color rgb(double r, double g, double b)
            throws IllegalArgumentException {
        return new Color(r, g, b);
    }

    /**
     * Retourne une couleur RGB depuis une nuance decimale
     * 
     * @param color
     *            La couleur entre 0 et 255
     * @return Couleur corresdpondante au parametre
     * @throws IllegalArgumentException
     *             Si les composantes sont en dehors de la plage 0 et 1 inclus
     */
    public static Color rgb(int color) throws IllegalArgumentException {
        double r = (double) (((color >> 16) & 0xFF) / 255.0);
        double g = (double) (((color >> 8) & 0xFF) / 255.0);
        double b = (double) (((color) & 0xFF) / 255.0);

        return new Color(r, g, b);
    }

    /**
     * Couleur composee d'un melange de deux autres couleurs
     * 
     * @param first
     *            La premiere couleur
     * @param second
     *            La seconde couleur
     * @return La troisieme couleur, melangee
     */
    public static Color mix(Color first, Color second) {
        double r = first.red() * second.red(), g = first.green()
                * second.green(), b = first.blue() * second.blue();

        return new Color(r, g, b);
    }

    /**
     * Retourune une couleur melange de cette couleur et de la couleur passee en
     * parametre
     * 
     * @param that
     *            Couleur passee en parametre
     * @return Nouvelle couleur melangee
     */
    public Color mixWith(Color that) {
        return mix(this, that);
    }

    /**
     * Transforme une Color en une AWTColor
     * 
     * @param that
     *            La couleur a transformer
     * @return L'equivalence en AWTColor
     */
    public static java.awt.Color toAWTColor(Color that) {
        return new java.awt.Color((float) that.red(), (float) that.green(),
                (float) that.blue());
    }

    /**
     * 
     * @return L'equivalence en AWTColor de cette Color
     */
    public java.awt.Color awtColor() {
        return toAWTColor(this);
    }

    /**
     * 
     * @return La nuance de rouge
     */
    public double red() {
        return this.red;
    }

    /**
     * 
     * @return La nuance de vert
     */
    public double green() {
        return this.green;
    }

    /**
     * 
     * @return La nuance de bleu
     */
    public double blue() {
        return this.blue;
    }
}