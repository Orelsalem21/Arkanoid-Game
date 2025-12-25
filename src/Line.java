public class Line {
    private static final double EPS = 1e-9;

    private Point start;
    private Point end;

    // constructors
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    // Return the length of the line
    public double length() {
        return this.start.distance(this.end);
    }

    // Returns the middle point of the line
    public Point middle() {
        double mx = (this.start.getX() + this.end.getX()) / 2.0;
        double my = (this.start.getY() + this.end.getY()) / 2.0;
        return new Point(mx, my);
    }

    // Returns the start point of the line
    public Point start() {
        return new Point(this.start);
    }

    // Returns the end point of the line
    public Point end() {
        return new Point(this.end);
    }

    // Returns true if the lines intersect, false otherwise
    public boolean isIntersecting(Line other) {
        return this.intersectionWith(other) != null;
    }

    // Returns the closest intersection point to the start of the line
    // with the given rectangle. Returns null if no intersection exists.
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        java.util.List<Point> points = rect.intersectionPoints(this);

        if (points.isEmpty()) {
            return null;
        }

        Point closest = points.get(0);
        double minDistance = this.start().distance(closest);

        for (Point p : points) {
            double d = this.start().distance(p);
            if (d < minDistance) {
                minDistance = d;
                closest = p;
            }
        }

        return closest;
    }

    private static boolean nearlyEqual(double a, double b) {
        return Math.abs(a - b) <= EPS;
    }

    private static boolean inRange(double v, double a, double b) {
        double min = Math.min(a, b) - EPS;
        double max = Math.max(a, b) + EPS;
        return v >= min && v <= max;
    }

    // Returns the intersection point if the lines intersect,
    // and null otherwise.
    public Point intersectionWith(Line other) {
        // endpoint intersections (with EPS-safe Point.equals after update below)
        if (this.start.equals(other.start())) return this.start;
        if (this.start.equals(other.end()))   return this.start;
        if (this.end.equals(other.start()))   return this.end;
        if (this.end.equals(other.end()))     return this.end;

        double x1 = this.start.getX(), y1 = this.start.getY();
        double x2 = this.end.getX(), y2 = this.end.getY();
        double x3 = other.start().getX(), y3 = other.start().getY();
        double x4 = other.end().getX(), y4 = other.end().getY();

        boolean thisVertical = nearlyEqual(x2, x1);
        boolean otherVertical = nearlyEqual(x4, x3);

        // both vertical -> either parallel or overlapping; for this assignment we return null
        if (thisVertical && otherVertical) {
            return null;
        }

        double m1, b1, m2, b2;

        if (!thisVertical) {
            m1 = (y2 - y1) / (x2 - x1);
            b1 = y1 - m1 * x1;
        } else {
            m1 = Double.POSITIVE_INFINITY;
            b1 = Double.NaN;
        }

        if (!otherVertical) {
            m2 = (y4 - y3) / (x4 - x3);
            b2 = y3 - m2 * x3;
        } else {
            m2 = Double.POSITIVE_INFINITY;
            b2 = Double.NaN;
        }

        double ix, iy;

        if (thisVertical) {
            ix = x1;
            iy = m2 * ix + b2;
        } else if (otherVertical) {
            ix = x3;
            iy = m1 * ix + b1;
        } else {
            if (nearlyEqual(m1, m2)) {
                return null; // parallel (or same line) - ignore for now
            }
            ix = (b2 - b1) / (m1 - m2);
            iy = m1 * ix + b1;
        }

        // bounds check with EPS (this is the critical fix for corner hits)
        if (!inRange(ix, x1, x2) || !inRange(ix, x3, x4) ||
                !inRange(iy, y1, y2) || !inRange(iy, y3, y4)) {
            return null;
        }

        return new Point(ix, iy);
    }

    // equals -- return true if the lines are equal, false otherwise
    public boolean equals(Line other) {
        boolean sameOrder = this.start.equals(other.start()) && this.end.equals(other.end());
        boolean swapped = this.start.equals(other.end()) && this.end.equals(other.start());
        return sameOrder || swapped;
    }
}
