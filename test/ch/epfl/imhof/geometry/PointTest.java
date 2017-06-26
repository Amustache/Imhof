package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

public class PointTest {
    private static final double DELTA = 0.000001;

    @Test
    public void xGetterReturnsX() {
        for (double x = -100; x <= 100; x += 12.32)
            assertEquals(x, new Point(x, 0).x(), DELTA);
    }

    @Test
    public void yGetterReturnsY() {
        for (double y = -100; y <= 100; y += 12.32)
            assertEquals(y, new Point(0, y).y(), DELTA);
    }
    
    @Test
    public void changementDeRepereTest(){
        Function<Point, Point> blueToRed =
                Point.alignedCoordinateChange(new Point(1, -1),
                                              new Point(5, 4),
                                              new Point(-1.5, 1),
                                              new Point(0, 0));
            Point nouv = blueToRed.apply(new Point(0, 0));
            // retourne le point (3,2)
            assertEquals(3, nouv.x(), 0.000000000000000000000000000000000001);
            System.out.println(nouv.y());
            assertEquals(2, nouv.y(), 0.000000000000000000000000000000000001);
    }
}
