import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

    private int x;
    private int y;

    // construct the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draw this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draw the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // is this point lexicographically smaller than that point?
    @Override
    public int compareTo(Point that) {
        if (this.y == that.y) {
            return this.x - that.x;
        }

        return this.y - that.y;
    }

    // the slope between this point and that point
    public double slopeTo(Point that) {

        double slope = 0.0;

        // Degenerate case, treat it as negative infinity
        if (this.x == that.x && this.y == that.y) {
            slope = Double.NEGATIVE_INFINITY;
        }
        // Vertical line, treat it like positive infinity
        else if (this.x == that.x) {
            slope = Double.POSITIVE_INFINITY;
        }
        // Horizontal line, treat it as 0
        else if (this.y == that.y) {
            slope = 0.0;
        }
        // Normal case
        else {
            slope = (double) (that.y - this.y) / (double) (that.x - this.x);
        }

        return slope;
    }

    private class SlopeOrder implements Comparator<Point> {

        @Override
        public int compare(Point p1, Point p2) {
            double slope1 = slopeTo(p1);
            double slope2 = slopeTo(p2);

            return Double.compare(slope1, slope2);
        }
    }

    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        Point point1 = new Point(5000, 5000);
        Point point2 = new Point(10000, 5000);
        Point point3 = new Point(10000, 10000);
        Point point4 = new Point(5000, 10000);
        Point point5 = new Point(1000, 10000);
        Point point6 = new Point(1000, 1000);

//        StdOut.println(point1.toString());
//        StdOut.println(point2.toString());
//        point1.draw();
//        point2.draw();
        point1.drawTo(point2);
        point1.drawTo(point3);
        point1.drawTo(point4);
        point1.drawTo(point5);
        point1.drawTo(point6);
//        StdOut.println(point1.compareTo(point2));
        StdOut.println(point1.slopeTo(point2));
        StdOut.println(point1.slopeTo(point3));
        StdOut.println(point1.slopeTo(point4));
        StdOut.println(point1.slopeTo(point5));
        StdOut.println(point1.slopeTo(point6));
    }
}