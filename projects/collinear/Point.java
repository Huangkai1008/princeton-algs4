package collinear;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;
import java.util.List;

/**
 * @author huangkai
 */
public class Point implements Comparable<Point> {

    private static final int COORDINATE_LOWER_LIMIT = 0;
    private static final int COORDINATE_UPPER_LIMIT = 32767;

    /**
     * x coordinate of this point.
     */
    private final int x;

    /**
     * y-coordinate of this point.
     */
    private final int y;


    /**
     * Constructor of point.
     */
    public Point(int x, int y) {
        if (x < COORDINATE_LOWER_LIMIT || x > COORDINATE_UPPER_LIMIT || y < COORDINATE_LOWER_LIMIT
                || y > COORDINATE_UPPER_LIMIT) {
            throw new IllegalArgumentException();
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point. Formally, if the two points are
     * (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0). For completeness, the slope is
     * defined to be +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical; and Double.NEGATIVE_INFINITY if (x0,
     * y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }

        if (this.y == that.y) {
            return Double.POSITIVE_INFINITY;
        }

        return (that.y - this.y) * 1.0 / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
     * point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0
     * = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument point (x0 = x1 and y0 =
     * y1); a negative integer if this point is less than the argument point; and a positive integer
     * if this point is greater than the argument point
     */
    public int compareTo(Point that) {
        if (this.x == that.x && this.y == that.x) {
            return 0;
        }

        boolean less = this.y < that.y || (this.y == that.y && this.x < that.x);
        return less ? -1 : 1;
    }

    /**
     * Compares two points by the slope they make with this point. The slope is defined as in the
     * slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }

    public class SlopeComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            Double slope1 = slopeTo(o1);
            Double slope2 = slopeTo(o2);
            return slope1.compareTo(slope2);
        }
    }

    /**
     * Returns a string representation of this point. This method is provided for debugging; your
     * program shouldn't rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Point p3 = new Point(0, 4);
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p1.compareTo(p2));
        System.out.println(p2.compareTo(p3));
        System.out.println(p1.compareTo(p3));

        List<Point> pointList = List.of(p1, p2, p3);
        pointList.stream()
                .sorted(Point::compareTo)
                .forEach(System.out::println);
        pointList.stream()
                .sorted(p1.slopeOrder())
                .forEach(System.out::println);

        p1.draw();
        p2.draw();
        p1.drawTo(p2);
    }
}
