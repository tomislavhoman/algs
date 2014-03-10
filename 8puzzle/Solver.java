public class Solver {

    private Node solution = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        int N = initial.dimension();
        int[][] goalBlocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1) {
                    continue;
                }

                goalBlocks[i][j] = N * i + j + 1;
                goalBlocks[N - 1][N - 1] = 0;
            }
        }

        MinPQ<Node> queue = new MinPQ<Node>();

        queue.insert(new Node(null, 0, initial));
        queue.insert(new Node(null, 0, initial.twin(), true));

        Node current = null;
        do {
            if (queue.size() == 0) {
                break;
            }

            current = queue.delMin();

            for (Board neighbour : current.board.neighbors()) {
                if (current.prev != null
                        && current.prev.board.equals(neighbour)) {
                    continue;
                }

                queue.insert(new Node(current, current.moves + 1,
                        neighbour, current.isTwin));
            }

        } while (!current.board.isGoal());

        solution = current;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return !solution.isTwin;
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        if (solution.isTwin) {
            return -1;
        }
        
        return solution.moves;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        if (solution.isTwin) {
            return null;
        }

        Stack<Board> result = new Stack<Board>();
        result.push(solution.board);

        Node prev = solution.prev;
        while (prev != null) {
            result.push(prev.board);
            prev = prev.prev;
        }
        return result;
    }

    private class Node implements Comparable<Node> {
        private Node prev;
        private int moves;
        private Board board;
        private int manhattan = 0;
        private boolean isTwin;

        private Node(Node prev, int moves, Board board) {
            this(prev, moves, board, false);

        }

        private Node(Node prev, int moves, Board board, boolean isTwin) {
            this.prev = prev;
            this.moves = moves;
            this.board = board;
            this.manhattan = board.manhattan();
            this.isTwin = isTwin;
        }

        @Override
        public int compareTo(Node that) {
            return (this.manhattan + this.moves)
            - (that.moves + that.manhattan);
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();

        if (N < 2 || N >= 128) {
            throw new IllegalArgumentException();
        }

        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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
