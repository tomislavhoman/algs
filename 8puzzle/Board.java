import java.util.Arrays;

public class Board {

    private char[][] blocks;
    private int N;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = copyToChar(blocks);
        this.N = blocks[0].length;
    }

    // board dimension N
    public int dimension() {
        return this.N;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1) {
                    continue;
                }

                if (blocks[i][j] != N * i + j + 1) {
                    hamming++;
                }
            }
        }

        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;

        for (int x = 1; x <= N * N - 1; x++) {
            int correctI = (x - 1) / N;
            int correctJ = (x - 1) % N;
            boolean found = false;
            for (int i = 0; i < N && !found; i++) {
                for (int j = 0; j < N && !found; j++) {
                    if (blocks[i][j] == x) {
                        found = true;
                        manhattan += Math.abs(i - correctI);
                        manhattan += Math.abs(j - correctJ);
                    }
                }
            }
        }

        return manhattan;
    }

    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1) {
                    continue;
                }

                if (blocks[i][j] != N * i + j + 1) {
                    return false;
                }
            }
        }

        return blocks[N - 1][N - 1] == 0;
    }

    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[][] twin = copyToInt(blocks);

        // Find a row that hasn't empty block
        boolean found = true;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                found = found && twin[i][j] != 0;
            }
            if (found) {
                swap(twin, i, 0, i, 1);
                return new Board(twin);
            }
            found = true;
        }

        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;

        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int x = 0;
        int y = 0;
        Queue<Board> queue = new Queue<Board>();

        boolean found = false;
        for (int i = 0; i < N && !found; i++) {
            for (int j = 0; j < N && !found; j++) {
                if (blocks[i][j] == 0) {
                    found = true;
                    x = i;
                    y = j;
                    break;
                }
            }
        }

        short[][] deltas = {{0, -1}, {0, +1}, {-1, 0}, {+1, 0}};
        for (short[] d : deltas) {
            int x1 = x + d[0];
            int y1 = y + d[1];
            if (inBounds(x1, y1)) {
                int[][] neighbour = copyToInt(blocks);
                swap(neighbour, x, y, x1, y1);
                queue.enqueue(new Board(neighbour));
            }
        }

        return queue;
    }

    // string representation of the board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", (int) blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private static int[][] copyToInt(char[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][original.length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                result[i][j] = original[i][j];
            }
        }

        return result;
    }

    private static char[][] copyToChar(int[][] original) {
        if (original == null) {
            return null;
        }

        final char[][] result = new char[original.length][original.length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                result[i][j] = (char) original[i][j];
            }
        }

        return result;
    }

    private void swap(int[][] array, int i1, int j1, int i2, int j2) {
        // Careful, I don't check bounds
        int tmp = array[i1][j1];
        array[i1][j1] = array[i2][j2];
        array[i2][j2] = tmp;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

//    public static void main(String[] args) {
//        In in = new In(args[0]);
//        int N = in.readInt();
//
//        if (N < 2 || N >= 128) {
//            throw new IllegalArgumentException();
//        }
//
//        int[][] blocks = new int[N][N];
//        for (int i = 0; i < N; i++)
//            for (int j = 0; j < N; j++)
//                blocks[i][j] = in.readInt();
//        Board initial = new Board(blocks);
//        int manhattan = initial.manhattan();
//        int hamming = initial.hamming();
//    }
}