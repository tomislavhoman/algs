public class PointSET {

    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point p to the set (if it is not already in the set)
    // Should ne O(log(N))
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain the point p?
    // Should ne O(log(N))
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points in the set that are inside the rectangle
    // Should ne O(N)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> result = new Queue<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                result.enqueue(point);
            }
        }

        return result;
    }

    // a nearest neighbor in the set to p; null if set is empty
    // Should ne O(N)
    public Point2D nearest(Point2D p) {
        if (points.isEmpty()) {
            return null;
        }

        Point2D nearest = points.min();
        double minDistance = p.distanceTo(nearest);
        for (Point2D point : points) {
            double distance = p.distanceTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }

        return nearest;
    }

    public static void main(String[] args) {
        StdOut.println("Setup");
    }
}