package eightpuzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

/**
 * @author huangkai
 */
public class Solver {
    /**
     * Search Node.
     */
    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private SearchNode prev;
        private final int moves;
        private final boolean isTwin;
        private final int manhattan;
        private final int priority;

        public SearchNode(Board board, boolean isTwin) {
            this.board = board;
            this.isTwin = isTwin;
            this.moves = 0;
            this.manhattan = board.manhattan();
            this.priority = manhattan + moves;
        }

        public SearchNode(Board board, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            this.isTwin = prev.isTwin;
            this.moves = prev.moves + 1;
            this.manhattan = board.manhattan();
            this.priority = manhattan + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.priority == that.priority) return this.manhattan - that.manhattan;
            return this.priority - that.priority;
        }
    }

    private final boolean isSolvable;
    private int moves;
    private final Iterable<Board> solution;

    /**
     * Constructor of solver.
     * <p>
     * Find a solution to the initial board (using the A* algorithm).
     *
     * @param initial board.
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        SearchNode searchNode = aStar(initial);

        isSolvable = searchNode != null && !searchNode.isTwin;
        if (isSolvable) {
            LinkedList<Board> solution = new LinkedList<>();
            SearchNode cur = searchNode;
            while (cur != null) {
                solution.addFirst(cur.board);
                cur = cur.prev;
            }
            this.solution = solution;
            this.moves = solution.size() - 1;
        } else {
            this.solution = null;
        }
    }

    private static SearchNode aStar(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, false));
        pq.insert(new SearchNode(initial.twin(), true));
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            Board board = cur.board;
            if (board.isGoal()) {
                return cur;
            }

            for (Board b : board.neighbors()) {
                // Don’t enqueue a neighbor if its board is the same as the board of
                // the previous search node in the game tree.
                if (cur.prev == null || !b.equals(cur.prev.board)) {
                    pq.insert(new SearchNode(b, cur));
                }
            }
        }
        return null;
    }

    /**
     * @return Whether the initial board is solvable.
     */
    public boolean isSolvable() {
        return isSolvable;
    }

    /**
     * @return Min number of moves to solve initial board.
     * `–1` if unsolvable.
     */
    public int moves() {
        if (isSolvable) {
            return moves;
        } else {
            return -1;
        }
    }

    /**
     * @return sequence of boards in the shortest solution
     * `Null` if unsolvable
     */
    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {

        // Create initial board from file.
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // Solve the puzzle.
        Solver solver = new Solver(initial);

        // Print solution to standard output.
        if (!solver.isSolvable()) StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
