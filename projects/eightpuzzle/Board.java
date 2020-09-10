package eightpuzzle;

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
    private int dimension;

    /**
     * Tiles of board.
     */
    private int[][] tiles;

    /**
     * Number of tiles out of place.
     */
    private final int hamming;

    /**
     * Sum of Manhattan distances between tiles and goal.
     */
    public int manhattan;


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
                if (outOfPlace(i, j, this.tiles[i][j])) {
                    hamming ++;
                }
            }
        }
        this.hamming = hamming;
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
     * @param row base index of row
     * @param col base index of col
     * @return the 1D index
     */
    private int index(int row, int col) {
        if (row <= 0 || row > dimension || col <= 0 || col > dimension) {
            throw new IllegalArgumentException("The given site (row, col) must in the n-by-n tiles.");
        }

        return row * dimension + col;
    }

    /**
     * @param row base index of row
     * @param col base index of col
     * @param value of the tile.
     * @return whether the tile in the wrong position now.
     */
    private boolean outOfPlace(int row, int col, int value) {
        // Because the tile labeled from 1.
        return index(row, col) + 1 == value;
    }
}
