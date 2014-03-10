public class KdTree {

    private static final boolean ORIENTATION_VERTICAL = true;
    private static final boolean ORIENTATION_HORIZONTAL = false;

    private static class Node {
        // the point
        private Point2D p;
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
        // the left/bottom subtree
        private Node lb;
        // the right/top subtree
        private Node rt;

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    private Node root;
    private int size;
    private Point2D currentNearest;
    private double currentMinDistance;

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    // Should ne O(log(N))
    public void insert(Point2D p) {
        if (this.contains(p)) {
            return;
        }

        root = insert(root, root, p, ORIENTATION_VERTICAL);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return search(root, p, ORIENTATION_VERTICAL) != null;
    }

    // draw all of the points to standard draw
    public void draw() {
        drawBox();
        if (root == null) {
            return;
        }

        draw(root, ORIENTATION_VERTICAL);
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> result = new SET<Point2D>();
        if (root == null) {
            return result;
        }

        range(root, rect, result);
        return result;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if (root == null) {
            return null;
        }

        currentNearest = root.p;
        currentMinDistance = root.p.distanceTo(p);
        nearest(root, p, ORIENTATION_VERTICAL);
        return currentNearest;
    }

    private void nearest(Node current, Point2D query, boolean orientation) {

        if (current == null) {
            return;
        }

        if (current.rect.distanceTo(query) > currentMinDistance) {
            return;
        }

        double distance = current.p.distanceTo(query);
        if (Double.compare(distance, currentMinDistance) < 0) {
            currentNearest = current.p;
            currentMinDistance = distance;
        }

        int cmp;
        if (orientation == ORIENTATION_VERTICAL) {
            cmp = Double.compare(query.x(), current.p.x());
        } else {
            cmp = Double.compare(query.y(), current.p.y());
        }

        Node firstToSearch;
        Node otherTotSearch;
        if (cmp < 0) {
            firstToSearch = current.lb;
            otherTotSearch = current.rt;
        } else {
            firstToSearch = current.rt;
            otherTotSearch = current.lb;
        }

        nearest(firstToSearch, query, !orientation);
        nearest(otherTotSearch, query, !orientation);
    }

    private Node insert(Node parent, Node node, Point2D value, boolean orientation) {
        if (node == null) {
            size++;

            double xMin = 0.0;
            double xMax = 1.0;
            double yMin = 0.0;
            double yMax = 1.0;

            if (parent != null) {
                RectHV rect = parent.rect;
                xMin = rect.xmin();
                xMax = rect.xmax();
                yMin = rect.ymin();
                yMax = rect.ymax();
                if (orientation == ORIENTATION_VERTICAL) {
                    int cmp = Double.compare(value.y(), parent.p.y());
                    if (cmp >= 0) {
                        yMin = parent.p.y();
                    } else if (cmp < 0) {
                        yMax = parent.p.y();
                    }
                } else {
                    int cmp = Double.compare(value.x(), parent.p.x());
                    if (cmp >= 0) {
                        xMin = parent.p.x();
                    } else if (cmp < 0) {
                        xMax = parent.p.x();
                    }
                }
            }

            return new Node(value, new RectHV(xMin, yMin, xMax, yMax));
        }

        int cmp = compareUsingOrientation(value, node.p, orientation);
        if (cmp >= 0) {
            node.rt = insert(node, node.rt, value, !orientation);
        } else {
            node.lb = insert(node, node.lb, value, !orientation);
        }

        return node;
    }

    private Node search(Node node, Point2D value, boolean orientation) {
        if (node == null) {
            return null;
        }

        if (node.p.equals(value)) {
            return node;
        }

        int cmp = compareUsingOrientation(value, node.p, orientation);
        if (cmp >= 0) {
            return search(node.rt, value, !orientation);
        } else {
            return search(node.lb, value, !orientation);
        }
    }

    private int compareUsingOrientation(Point2D value1,
                                        Point2D value2, boolean orientation) {
        double value1D;
        double value2D;
        if (orientation == ORIENTATION_VERTICAL) {
            value1D = value1.x();
            value2D = value2.x();
        } else {
            value1D = value1.y();
            value2D = value2.y();
        }

        return Double.compare(value1D, value2D);
    }

    private void draw(Node current, boolean orientation) {
        if (current == null) {
            return;
        }

        draw(current.lb, !orientation);
        drawNode(current, orientation);
        draw(current.rt, !orientation);
    }

    private void drawNode(Node node, boolean orientation) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(node.p.x(), node.p.y());

        StdDraw.setPenRadius();
        if (orientation == ORIENTATION_VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
    }

    private void drawBox() {
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.line(0, 0, 1, 0);
        StdDraw.line(1, 0, 1, 1);
        StdDraw.line(1, 1, 0, 1);
        StdDraw.line(0, 1, 0, 0);
    }

    private void range(Node current, RectHV rect,
                       SET<Point2D> result) {
        if (current == null) {
            return;
        }

        if (rect.contains(current.p)) {
            result.add(current.p);
        }

        if (current.lb != null && current.lb.rect.intersects(rect)) {
            range(current.lb, rect, result);
        }

        if (current.rt != null && current.rt.rect.intersects(rect)) {
            range(current.rt, rect, result);
        }
    }

    public static void main(String[] args) {
//        if (args.length < 1) {
//            throw new IllegalArgumentException();
//        }
//
//        KdTree tree = new KdTree();
//        PointSET treeSet = new PointSET();
//
//        String inputFile = args[0];
//        In in = new In(inputFile);
//        double[] coordinates = in.readAllDoubles();
//        for (int i = 0; i < coordinates.length; i += 2) {
//            if (coordinates[i + 1] < coordinates.length) {
//                tree.insert(new Point2D(coordinates[i], coordinates[i + 1]));
//                treeSet.insert(new Point2D(coordinates[i], coordinates[i + 1]));
//            }
//        }

        //tree.draw();

//        boolean contains1 = tree.contains(new Point2D(0.81, 03));
//        boolean contains2 = tree.contains(new Point2D(0.206107, 0.095492));
//
//        Point2D reference = new Point2D(0.81, 0.3);
//        Point2D nearest1 = tree.nearest(reference);
//        double distance = reference.distanceTo(nearest1);

//        KdTree tree = new KdTree();
//        int size = tree.size();
//        tree.insert(new Point2D(0.5, 0.5));
//        size = tree.size();
//        tree.insert(new Point2D(0.6, 0.6));
//        size = tree.size();
//        tree.insert(new Point2D(0.5, 0.5));
//        tree.insert(new Point2D(0.5, 0.5));
//        tree.insert(new Point2D(0.5, 0.5));
//        tree.insert(new Point2D(0.5, 0.5));
//        size = tree.size();

//        Point2D reference = new Point2D(0.7, 0.52);
//        Point2D nearestKd = tree.nearest(reference);
//        Point2D nearestBrute = treeSet.nearest(reference);
    }
}