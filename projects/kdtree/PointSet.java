package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.LinkedList;
import java.util.List;

/**
 * @author huangkai
 */
public class PointSet {
    private final SET<Point2D> pointSet;

    /**
     * Construct an empty set of points.
     */
    public PointSet() {
        pointSet = new SET<>();
    }

    /**
     * @return whether the set is empty.
     */
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    /**
     * @return number of points in the set.
     */
    public int size() {
        return pointSet.size();
    }

    /**
     * Add the point to the set if it isn't already in the set.
     *
     * @param p is the given point.
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointSet.add(p);
    }

    /**
     * @param p is the given point.
     * @return whether the set contains p.
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointSet.contains(p);
    }

    /**
     * Draw all the points to standard draw.
     */
    public void draw() {
        pointSet.forEach(Point2D::draw);
    }

    /**
     * @param rect is the target rectangle.
     * @return all points that are inside the rectangle (or on the boundary) .
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> points = new LinkedList<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                points.add(p);
            }
        }
        return points;
    }

    /**
     * @param p is the given point.
     * @return a nearest neighbor in the set to point p.
     * </p>
     * null if the set is empty.
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point : pointSet) {
            double distance = point.distanceSquaredTo(p);
            if (distance <= minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }
        return nearest;
    }
}
