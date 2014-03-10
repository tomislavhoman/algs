import java.util.Arrays;

public class Brute {


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

//        for (int i = 0; i < points.length; i++) {
//            StdOut.println(points[i].toString());
//        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {

                        Point pointI = points[i];
                        Point pointJ = points[j];
                        Point pointK = points[k];
                        Point pointL = points[l];

                        double slopeIj = pointI.slopeTo(pointJ);
                        double slopeIk = pointI.slopeTo(pointK);
                        double slopeIl = pointI.slopeTo(pointL);

                        // They are on the same segment
                        if (slopeIj == slopeIk && slopeIk == slopeIl) {
                            Point[] orderedPoints = new Point[]
                                    {pointI, pointJ, pointK, pointL};
                            Arrays.sort(orderedPoints);
                            for (int m = 0; m < orderedPoints.length; m++) {
                                if (m < orderedPoints.length - 1) {
                                    StdOut.print(orderedPoints[m] + " -> ");
                                } else {
                                    StdOut.print(orderedPoints[m]);
                                }
                            }
                            StdOut.println();
                            orderedPoints[0].drawTo(orderedPoints[3]);
                        }
                    }
                }
            }
        }
    }
}