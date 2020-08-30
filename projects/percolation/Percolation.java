package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // The uf with virtual top site and virtual bottom site
    private final WeightedQuickUnionUF uf;

    // The uf with virtual top site
    private final WeightedQuickUnionUF backwashUf;

    // The width of the grid
    private final int width;

    // The sites of grid, true represents `opened`, false represents `blocked`
    private final boolean[][] sites;

    // The number of open sites
    private int openedCount = 0;

    // The index of virtual top site
    private final int topIndex;

    // The index of virtual bottom site
    private final int bottomIndex;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("`n` must be positive");

        uf = new WeightedQuickUnionUF(n * n + 2);
        backwashUf = new WeightedQuickUnionUF(n * n + 1);
        width = n;
        sites = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sites[i][j] = false;
            }
        }
        topIndex = 0;
        bottomIndex = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int index = getIndex(row, col);
        sites[row - 1][col - 1] = true;
        openedCount++;

        if (row == 1) {
            uf.union(index, topIndex);
            uf.union(index, topIndex);
        }

        if (row == width) {
            uf.union(index, bottomIndex);
        }

        // union the current site with it's left, right, top, bottom site
        // if neighbour not exists, skip this
        union(row, col, row - 1, col);
        union(row, col, row + 1, col);
        union(row, col, row, col - 1);
        union(row, col, row, col + 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return sites[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = getIndex(row, col);
        return backwashUf.find(index) == backwashUf.find(topIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(topIndex) == uf.find(bottomIndex);
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        StdOut.println(percolation.percolates());
        percolation.open(1, 3);
        percolation.open(2, 3);
        percolation.open(3, 3);
        percolation.open(3, 1);
        StdOut.println(percolation.isFull(1, 3));
        StdOut.println(percolation.numberOfOpenSites());
        StdOut.println(percolation.percolates());
    }

    /**
     * @param row base-1 index of row
     * @param col base-1 index of col
     * @return the 1D index
     */
    private int getIndex(int row, int col) {
        if (row <= 0 || row > width || col <= 0 || col > width) {
            throw new IndexOutOfBoundsException("The given site (row, col) must in the n-by-n grid");
        }

        return (row - 1) * width + col;
    }

    private void union(int p_row, int p_col, int q_row, int q_col) {
        if (q_row <= 0 || q_row > width || q_col <= 0 || q_col > width)
            return;

        int pIndex = getIndex(p_row, p_col);
        int qIndex = getIndex(q_row, q_col);
        if (isOpen(q_row, q_col)) {
            uf.union(pIndex, qIndex);
            backwashUf.union(pIndex, qIndex);
        }
    }
}
