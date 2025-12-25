public class Velocity {
    // data members
    private double dx;
    private double dy;

    // constructor
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    // accessors
    public double getDx() {
        return this.dx;
    }

    public double getDy() {
        return this.dy;
    }

    // Take a point with position (x,y) and return a new point (x+dx, y+dy)
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + this.dx, p.getY() + this.dy);
    }

    // create a velocity from angle (0 = up) and speed
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double rad = Math.toRadians(angle);
        double dx = speed * Math.sin(rad);
        double dy = -speed * Math.cos(rad); // up is 0 degrees
        return new Velocity(dx, dy);
    }
}
