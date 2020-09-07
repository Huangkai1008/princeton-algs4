package collinear;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author huangkai
 */
public class BruteCollinearPoints {
    private static final int MINIMUM_LENGTH = 4;
    private final LinkedList<LineSegment> collinearLineSegments = new LinkedList<>();

    /**
     * Constructor of BruteCollinearPoints.
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("The points can't be null.");
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Every point can't be null.");
            }
        }

        int length = points.length;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Points can't repeat.");
                }
            }
        }

        if (length < MINIMUM_LENGTH) {
            return;
        }

        Point[] sortedPoints = Arrays.copyOf(points, length);
        Arrays.parallelSort(sortedPoints);

        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    for (int l = k + 1; l < length; l++) {
                        double s1 = sortedPoints[i].slopeTo(sortedPoints[j]);
                        double s2 = sortedPoints[i].slopeTo(sortedPoints[k]);
                        double s3 = sortedPoints[i].slopeTo(sortedPoints[l]);
                        if (s1 == s2 && s1 == s3) {
                            collinearLineSegments
                                    .add(new LineSegment(sortedPoints[i], sortedPoints[l]));
                        }
                    }
                }
            }
        }
    }

    /**
     * @return the number of segments.
     */
    public int numberOfSegments() {
        return collinearLineSegments.size();
    }

    /**
     * @return the line segments.
     */
    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[numberOfSegments()];
        int index = 0;
        for (LineSegment lineSegment : collinearLineSegments) {
            lineSegments[index++] = lineSegment;
        }
        return lineSegments;
    }
}
