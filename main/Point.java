package main;

public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    public Point scale(double factor) {
        return new Point(x * factor, y * factor);
    }

    public Point sub(Point other) {
        return new Point(x - other.x, y - other.y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
