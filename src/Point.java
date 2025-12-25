public class Point {
    private static final double EPS = 1e-9;

    // data members
    private double x;
    private double y;

    // constructor
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Copy constructor - creates a new Point identical to 'other'
    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    // distance -- return the distance of this point to the other point
    public double distance(Point other) {
        double dx = this.x - other.getX();
        double dy = this.y - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // equals -- return true if the points are equal, false otherwise
    public boolean equals(Point other) {
        if (other == null) {
            return false;
        }
        return Math.abs(this.x - other.getX()) <= EPS && Math.abs(this.y - other.getY()) <= EPS;
    }

    // Return the x and y values of this point
    public double getX() { return x; }
    public double getY() { return y; }
}
