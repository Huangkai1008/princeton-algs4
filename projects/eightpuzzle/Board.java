package eightpuzzle;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author huangkai
 */
public class Board {

    /**
     * The dimension of the board.
     * <p>
     * If the board creates from an n-by-n array of tiles,
     * The dimension is `n`.
     */
    private final int dimension;

    /**
     * Tiles of board.
     */
    private final int[][] tiles;

    /**
     * Number of tiles out of place.
     */
    private final int hamming;

    /**
     * Sum of Manhattan distances between tiles and goal.
     */
    private final int manhattan;

    /**
     * The index of blank square.
     */
    private int blankIndex;

    /**
     * Create a board from an n-by-n array of tiles,
     * where tiles[row][col] = tile at (row, col)
     *
     * @param tiles of board.
     */
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new NullPointerException();
        }

        this.dimension = tiles.length;
        this.tiles = new int[dimension][dimension];
        int hamming = 0;
        int manhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];

                if (this.tiles[i][j] == 0) {
                    this.blankIndex = index(i, j);
                    continue;
                }

                if (outOfPlace(i, j, this.tiles[i][j])) {
                    hamming++;
                }
                manhattan += manhattenDistance(i, j, tiles[i][j]);
            }
        }
        this.hamming = hamming;
        this.manhattan = manhattan;
    }

    /**
     * The dimension of the board.
     */
    public int dimension() {
        return this.dimension;
    }

    /**
     * @return number of tiles out of place
     */
    public int hamming() {
        return this.hamming;
    }

    /**
     * @return sum of Manhattan distances between tiles and goal.
     */
    public int manhattan() {
        return this.manhattan;
    }

    /**
     * @return whether this board is the goal board.
     */
    public boolean isGoal() {
        return this.hamming == 0 && this.manhattan == 0;
    }

    /**
     * @return whether this board equals y.
     */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (!(y instanceof Board)) {
            return false;
        }

        Board board = (Board) y;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    /**
     * @return all neighboring boards.
     */
    public Iterable<Board> neighbors() {
        LinkedList<Board> boards = new LinkedList<>();
        int blankRow = this.blankIndex / this.dimension;
        int blankCol = this.blankIndex % this.dimension;
        int[][] directions = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        for (int[] direction : directions) {
            int row = blankRow + direction[0];
            int col = blankCol + direction[1];
            if (inBoard(row, col)) {
                boards.add(neighbor(row, col, blankRow, blankCol));
            }
        }
        return boards;
    }

    /**
     * @return A board that obtained by exchanging any pair of tiles.
     */
    public Board twin() {
        for (int index = 0; index < dimension * dimension - 1; index++) {
            int row = index / dimension;
            int col = index % dimension;
            int twinRow = (index + 1) / dimension;
            int twinCol = (index + 1) % dimension;
            if (tiles[row][col] != 0 && tiles[twinRow][twinCol] != 0) {
                return neighbor(row, col, twinRow, twinCol);
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append("\n");
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                sb.append(String.format("%2d ", tiles[row][col]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int[][] tiles1 = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board1 = new Board(tiles1);
        System.out.println(board1);
        System.out.println(board1.twin());
        for (Board b : board1.neighbors()) {
            System.out.println(b);
            System.out.println(b.isGoal());
        }

        int[][] tiles2 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board2 = new Board(tiles2);
        System.out.println(board2);
        System.out.println(board2.manhattan());
        System.out.println(board2.hamming());
    }

    /**
     * @param row base index of row
     * @param col base index of col
     * @return whether the tile with the given row in this board.
     */
    private boolean inBoard(int row, int col) {
        return row >= 0 && row < this.dimension && col >= 0 && col < this.dimension;
    }

    /**
     * @param row base index of row
     * @param col base index of col
     * @return the 1D index
     */
    private int index(int row, int col) {
        if (row < 0 || row >= dimension || col < 0 || col >= dimension) {
            throw new IllegalArgumentException("The given site (row, col) must in the n-by-n tiles.");
        }

        return row * dimension + col;
    }

    /**
     * @param row   base index of row
     * @param col   base index of col
     * @param value of the tile.
     * @return whether the tile in the wrong position now.
     */
    private boolean outOfPlace(int row, int col, int value) {
        // Because the tile labeled from 1.
        return index(row, col) + 1 != value;
    }

    /**
     * @param row   base index of row
     * @param col   base index of col
     * @param value of the tile.
     * @return the manhatten distance of the tile.
     */
    private int manhattenDistance(int row, int col, int value) {
        int targetRow = (value - 1) / this.dimension;
        int targetCol = (value - 1) % this.dimension;
        int vertical = Math.abs(row - targetRow);
        int horizontal = Math.abs(col - targetCol);
        return vertical + horizontal;
    }

    private Board neighbor(int row, int col, int blankRow, int blankCol) {
        int[][] tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(this.tiles[i], 0, tiles[i], 0, dimension);
        }
        int tmp = tiles[row][col];
        tiles[row][col] = tiles[blankRow][blankCol];
        tiles[blankRow][blankCol] = tmp;
        return new Board(tiles);
    }
}
