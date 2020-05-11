import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.LinkedList;

public class Board {

    private final int[][] tiles;
    private final int width;

    private final int[] blankPos;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        width = tiles.length;
        this.tiles = new int[width][width];
        for (int i = 0; i < width; i++)
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, width);


        blankPos = getBlankPos();
    }

    // string representation of this board
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(width).append("\n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                b.append(tiles[i][j]);
                if (j != width - 1) b.append(" ");
                else b.append("\n");
            }
        }
        return b.toString();
    }

    // board dimension n
    public int dimension() {
        return width;
    }

    // number of tiles out of place
    public int hamming() {
        int cnt = 0;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] - 1 == i * width + j) cnt++;
            }

        return width * width - cnt - 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

        int manhattanSum = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] == 0) continue;
                int goalY = (tiles[i][j] - 1) / width;
                int goalX = (tiles[i][j] - 1) % width;
                manhattanSum += Math.abs((i - goalY)) + Math.abs((j - goalX));
            }
        }
        return manhattanSum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }


    public boolean equals(Object y) {
        if (this == y) return true;
        if (!(y instanceof Board)) return false;
        Board board = (Board) y;
        return width == board.width &&
                Arrays.deepEquals(tiles, board.tiles);
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<>();
        int[] pos = getBlankPos();
        int x = pos[0], y = pos[1];

        if (x > 0) {
            neighbors.addFirst(exch(x, y, x - 1, y));
        }
        if (y > 0) {
            neighbors.addFirst(exch(x, y, x, y - 1));
        }
        if (y < width - 1) {
            neighbors.addFirst(exch(x, y, x, y + 1));
        }
        if (x < width - 1) {
            neighbors.addFirst(exch(x, y, x + 1, y));
        }
        return neighbors;

    }

    private Board exch(int ia, int ja, int ib, int jb) {
        int[][] newTiles = new int[width][width];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < width; j++)
                newTiles[i][j] = tiles[i][j];

        int tmp = newTiles[ia][ja];

        newTiles[ia][ja] = newTiles[ib][jb];
        newTiles[ib][jb] = tmp;

        return new Board(newTiles);
    }

    private int[] getBlankPos() {
        int x = 0, y = 0;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        return new int[]{x, y};
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int ia = 0, ib = 0, ja = 0, jb = 1;
        for (int i = 0; i < width - 1; i++)
            for (int j = 0; j < width - 1; j++)
                if (i != blankPos[0] && j != blankPos[1] && i + 1 != blankPos[0] && j + 1 != blankPos[1]) {
                    ia = i;
                    ja = j;
                    ib = i + 1;
                    jb = i + 1;
                }

        return exch(ia, ja, ib, jb);
    }


}