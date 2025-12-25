public class Rectangle {
    // Fields
    private Point upperLeft;
    private double width;
    private double height;

    /**
     * Create a new rectangle with location and width/height.
     *
     * @param upperLeft the upper-left point of the rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }

    /**
     * Return a (possibly empty) List of intersection points
     * with the specified line.
     *
     * @param line the line to check intersections with
     * @return a list of intersection points (may be empty)
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        java.util.List<Point> points = new java.util.ArrayList<Point>();

        double xLeft = this.upperLeft.getX();
        double yTop = this.upperLeft.getY();
        double xRight = xLeft + this.width;
        double yBottom = yTop + this.height;

        // Create the 4 edges of the rectangle
        Point upperRight = new Point(xRight, yTop);
        Point lowerLeft = new Point(xLeft, yBottom);
        Point lowerRight = new Point(xRight, yBottom);

        Line top = new Line(this.upperLeft, upperRight);
        Line bottom = new Line(lowerLeft, lowerRight);
        Line left = new Line(this.upperLeft, lowerLeft);
        Line right = new Line(upperRight, lowerRight);

        // Check intersections with each edge
        addIntersectionPoint(line, top, points);
        addIntersectionPoint(line, bottom, points);
        addIntersectionPoint(line, left, points);
        addIntersectionPoint(line, right, points);

        return points;
    }

    /**
     * Helper method: add intersection point between line and edge,
     * avoiding duplicates.
     */
    private void addIntersectionPoint(Line line, Line edge, java.util.List<Point> points) {
        Point p = line.intersectionWith(edge);
        if (p != null) {
            boolean exists = false;
            for (Point existing : points) {
                if (existing.equals(p)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                points.add(p);
            }
        }
    }

    /**
     * Return the width of the rectangle.
     *
     * @return the width
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Return the height of the rectangle.
     *
     * @return the height
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Returns the upper-left point of the rectangle.
     *
     * @return the upper-left point
     */
    public Point getUpperLeft() {
        return this.upperLeft;
    }
}
