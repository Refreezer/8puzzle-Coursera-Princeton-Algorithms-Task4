import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {

    private static class BoardNode implements Comparable<BoardNode> {
        private final int moves;
        private final Board board;
        private final BoardNode prevNode;

        private final int manhattan;

        BoardNode(Board board) {
            prevNode = null;
            moves = 0;
            this.board = board;
            manhattan = board.manhattan() + moves;
        }

        BoardNode(BoardNode prev, Board board) {
            this.prevNode = prev;
            this.board = board;
            this.moves = prev.moves + 1;
            manhattan = board.manhattan() + moves;

        }


        public int getManhattan() {
            return manhattan;
        }

        @Override
        public int compareTo(BoardNode o) {
            return new BoardNodeComparator().compare(this, o);
        }

        public static Comparator<BoardNode> getComparator() {
            return new BoardNodeComparator();
        }


        private static class BoardNodeComparator implements Comparator<BoardNode> {

            @Override
            public int compare(BoardNode o1, BoardNode o2) {
                return Integer.compare(o1.getManhattan(), o2.getManhattan());
            }
        }
    }

    private final BoardNode initialNode;
    private int moves = -1;
    private Iterable<Board> solution = new LinkedList<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        initialNode = new BoardNode(initial);

        solution = solve();
        if (solution == null) return;

        for (Board board : solution)
            moves++;

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null || initialNode.board.isGoal();
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solution;
    }

    private Iterable<Board> solve() {
        BoardNode twinNode = new BoardNode(initialNode.board.twin());

        MinPQ<BoardNode> queue = new MinPQ<>(BoardNode.getComparator());
        MinPQ<BoardNode> twinQueue = new MinPQ<>(BoardNode.getComparator());


        BoardNode searchNode = initialNode;

        queue.insert(searchNode);
        twinQueue.insert(twinNode);

        while (!searchNode.board.isGoal() && !twinNode.board.isGoal()) {

            searchNode = queue.delMin();

            for (Board neighbor : searchNode.board.neighbors()) {
                if (searchNode.prevNode == null || !neighbor.equals(searchNode.prevNode.board))
                    queue.insert(new BoardNode(searchNode, neighbor));
            }

            twinNode = twinQueue.delMin();

            for (Board neighbor : twinNode.board.neighbors()) {
                if (twinNode.prevNode == null || !neighbor.equals(twinNode.prevNode.board))
                    twinQueue.insert(new BoardNode(twinNode, neighbor));
            }


        }

        if (twinNode.board.isGoal()) {
            return null;
        }

        LinkedList<Board> result = new LinkedList<>();
        while (searchNode != null) {
            result.addFirst(searchNode.board);
            searchNode = searchNode.prevNode;
        }
        return result;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle

        Solver solver = new Solver(initial);


        // print solution to standard output

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}