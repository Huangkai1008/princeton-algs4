package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;


/**
 * @author huangkai
 */
public class KdTree {
    private static final double X_MIN = 0.0;
    private static final double X_MAX = 1.0;
    private static final double Y_MIN = 0.0;
    private static final double Y_MAX = 1.0;

    /**
     * @author huangkai
     */
    private static class Node {
        /**
         * Point of the node.
         */
        private final Point2D p;

        /**
         * Left subtree.
         */
        private Node left;

        /**
         * Right subtree.
         */
        private Node right;

        /**
         * The query rectangle.
         */
        private final RectHV rect;

        private Node(Point2D p) {
            this(p, new RectHV(X_MIN, Y_MIN, X_MAX, Y_MAX));
        }

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    /**
     * The root node of KdTree.
     */
    private Node root;

    /**
     * The size of KdTree.
     */
    private int size;


    /**
     * Construct an empty KdTree.
     */
    public KdTree() {
    }

    /**
     * @return whether the tree is empty.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * @return number of points in the tree.
     */
    public int size() {
        return this.size;
    }

    /**
     * Add the point to the tree if it isn't already in the tree.
     *
     * @param p is the given point.
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        this.root = insert(root, p, new RectHV(X_MIN, Y_MIN, X_MAX, Y_MAX), true);
        this.size++;
    }

    private Node insert(Node node, Point2D p, RectHV rect, boolean compareByX) {
        if (node == null) {
            return new Node(p, rect);
        }

        int cmp = compareTo(p, node, compareByX);
        if (cmp == 0) {
            size--;
            return node;
        }


        if (compareByX) {
            if (cmp < 0) {
                RectHV rectHV = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                node.left = insert(node.left, p, rectHV, false);
            } else {
                RectHV rectHV = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                node.right = insert(node.right, p, rectHV, false);
            }
        } else {
            if (cmp < 0) {
                RectHV rectHV = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                node.left = insert(node.left, p, rectHV, true);
            } else {
                RectHV rectHV = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
                node.right = insert(node.right, p, rectHV, true);
            }
        }
        return node;
    }

    /**
     * @param p is the given point.
     * @return whether the tree contains p.
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return this.contains(root, p, true);
    }

    /**
     * @param node is the tree root.
     * @param p    is the given point.
     * @return whether the tree rooted in the node has the given point.
     */
    private boolean contains(Node node, Point2D p, boolean compareByX) {
        if (node == null) {
            return false;
        }

        int cmp = compareTo(p, node, compareByX);
        if (cmp == 0) {
            return true;
        } else if (cmp > 0) {
            return this.contains(node.left, p, !compareByX);
        } else {
            return this.contains(node.right, p, !compareByX);
        }
    }

    /**
     * Draw all the points to standard draw.
     */
    public void draw() {
        StdDraw.clear();
        this.preorderDraw(root, true);
    }

    private void preorderDraw(Node node, boolean compareByX) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        node.p.draw();

        if (compareByX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        this.preorderDraw(node.left, !compareByX);
        this.preorderDraw(node.right, !compareByX);
    }

    /**
     * @param rect is the target rectangle.
     * @return all points that are inside the rectangle (or on the boundary) .
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> points = new ArrayList<>();
        range(root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, List<Point2D> points) {
        if (node == null) {
            return;
        }

        if (rect.contains(node.p)) {
            points.add(node.p);
        }

        if (rect.intersects(node.rect)) {
            range(node.left, rect, points);
            range(node.right, rect, points);
        }
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

        return nearest(root, p, root.p, true);
    }

    private Point2D nearest(Node node, Point2D p, Point2D c, boolean compareByX) {
        Point2D nearest = c;
        if (node == null) {
            return nearest;
        }

        if (node.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = node.p;
        }

        // If the current rectangle is closer to p than the closest point,
        // find closer point from its subtrees.
        if (node.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            Node near, far;
            if ((compareByX && (p.x() < node.p.x())) || (!compareByX && (p.y() < node.p.y()))) {
                near = node.left;
                far = node.right;
            } else {
                near = node.right;
                far = node.left;
            }
            nearest = nearest(near, p, nearest, !compareByX);
            nearest = nearest(far, p, nearest, !compareByX);
        }
        return nearest;
    }

    private int compareTo(Point2D p, Node node, boolean compareByX) {
        if (node == null)
            throw new NullPointerException();

        if (compareByX) {
            int compare = Double.compare(p.x(), node.p.x());
            return compare == 0 ? Double.compare(p.y(), node.p.y()) : compare;
        } else {
            int compare = Double.compare(p.y(), node.p.y());
            return compare == 0 ? Double.compare(p.x(), node.p.x()) : compare;
        }
    }
}
