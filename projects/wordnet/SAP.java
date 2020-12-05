package wordnet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author huangkai
 */
public class SAP {
    private final Digraph g;
    private final int maxVertexId;
    private final Map<String, Result> cache;

    /**
     * Constructor takes a graph (not necessarily a DAG)
     */
    public SAP(Digraph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }

        this.g = new Digraph(g);
        this.maxVertexId = g.V();
        this.cache = new HashMap<>();
    }

    /**
     * @return length of the shortest ancestral path between `v` and `w`.
     * </p>
     * -1 if no such path.
     */
    public int length(int v, int w) {
        return result(v, w).length;
    }

    /**
     * Return a common ancestor of v and w that participates in the shortest ancestral path.
     * </p>
     * -1 if no such path.
     */
    public int ancestor(int v, int w) {
        return result(v, w).ancestor;
    }

    /**
     * Return length of the shortest ancestral path between any vertex in v and any vertex in w.
     * </p>
     * -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }

        detectVertex(v);
        detectVertex(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
        return sap(bfsV, bfsW).length;
    }

    /**
     * @return a common ancestor that participates in shortest ancestral path.
     * </p>
     * -1 if no such path.
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }

        detectVertex(v);
        detectVertex(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
        return sap(bfsV, bfsW).ancestor;
    }

    private Result sap(int v, int w) {
        if (v == w) {
            return new Result(0, v);
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
        return sap(bfsV, bfsW);
    }


    private Result sap(BreadthFirstDirectedPaths v, BreadthFirstDirectedPaths w) {
        int length = -1;
        int ancestor = -1;
        for (int i = 0; i < this.g.V(); i++) {
            if (v.hasPathTo(i) && w.hasPathTo(i)) {
                if (length < 0 || length > v.distTo(i) + w.distTo(i)) {
                    length = v.distTo(i) + w.distTo(i);
                    ancestor = i;
                }
            }
        }
        return new Result(length, ancestor);
    }

    private Result result(int v, int w) {
        detectVertex(v);
        detectVertex(w);

        String cacheKey = cacheKey(v, w);
        Result result = cache.get(cacheKey);
        if (result == null) {
            result = sap(v, w);
            cache.put(cacheKey, result);
        }
        return result;
    }

    private void detectVertex(int v) {
        if (v < 0 || v >= maxVertexId) {
            throw new IllegalArgumentException();
        }
    }

    private String cacheKey(int v, int w) {
        detectVertex(v);
        detectVertex(w);

        int min = Math.min(v, w);
        int max = Math.max(v, w);
        return min + ":" + max;
    }


    private static class Result {
        private final int length;
        private final int ancestor;

        Result(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }

    private void detectVertex(Iterable<Integer> v) {
        if (v == null) {
            throw new IllegalArgumentException();
        }

        for (Integer i : v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }

            this.detectVertex(i);
        }
    }

    private boolean hasValue(Iterable<Integer> v) {
        Iterator<Integer> it = v.iterator();
        return it.hasNext();
    }

}
