import java.util.Arrays;

public class Fast {

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException();
        }

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        String fileName = args[0];
        In in = new In(fileName);
        int n = in.readInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();

            points[i] = new Point(x, y);
            points[i].draw();
        }

        if (points.length < 4) {
            return;
        }

        /*
         Pre order them lexicographically so I won't
         have to order them when finding collinear in the row
         */
        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {
            Point thePoint = points[i];
            Point[] auxPoints = Arrays.copyOf(points, points.length);
            Arrays.sort(auxPoints, thePoint.SLOPE_ORDER);

            // A little automata
            int fromIndex = 1;
            /* 1st (zeroth array element) is always the point
               so it isn't considered
             */
            double lastSlope = thePoint.slopeTo(auxPoints[1]);
            // Got first memoed so we start with second
            for (int j = 2; j < auxPoints.length; j++) {

                Point currentPoint = auxPoints[j];
                double currentSlope = thePoint.slopeTo(currentPoint);
                if (currentSlope != lastSlope) {
                    // How many in the row, don't consider j
                    testAndPrint(fromIndex, j, thePoint, auxPoints, false);

                    fromIndex = j;
                    lastSlope = currentSlope;
                }
            }
            testAndPrint(fromIndex, auxPoints.length - 1,
                    thePoint, auxPoints, true);
        }
    }

    private static void testAndPrint(int fromIndex, int toIndex,
                                     Point thePoint, Point[] points,
                                     boolean includeLast) {
        int collinearInARow = toIndex - fromIndex;
        int toIndexTmp = toIndex;
        if (includeLast) {
            collinearInARow++;
            toIndexTmp++;
        }
        //  If more than 3 order them for printing out
        if (collinearInARow >= 3
                && thePoint.compareTo(points[fromIndex]) < 0) {

            StdOut.print(thePoint + " -> ");
            for (int i = fromIndex; i < toIndexTmp; i++) {
                if (i < toIndexTmp - 1) {
                    StdOut.print(points[i] + " -> ");
                } else {
                    StdOut.print(points[i]);
                }
            }
            StdOut.println();
            thePoint.drawTo(points[toIndexTmp - 1]);
        }
    }
}