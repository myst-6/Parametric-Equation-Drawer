package main;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.PathIterator;
import java.awt.font.FontRenderContext;

public abstract class Bezier extends Equation {
    public Bezier() {
        super(0d, 1d);
    }

    public static class Constant extends Bezier {
        Point point;

        public Constant(Point point) {
            this.point = point;
        }

        @Override
        public Point point(double t) {
            return point;
        }
    }

    public static class Linear extends Bezier {
        Point from;
        Point to;

        public Linear(Point from, Point to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Point point(double t) {
            return from.add(to.sub(from).scale(t)); // linear interpolation
        }
    }

    public static class Quadratic extends Bezier {
        Linear from;
        Linear to;

        public Quadratic(Point from, Point pivot, Point to) {
            this.from = new Linear(from, pivot);
            this.to = new Linear(pivot, to);
        }

        @Override
        public Point point(double t) {
            return new Linear(from.point(t), to.point(t)).point(t);
        }
    }

    public static class Cubic extends Bezier {
        Quadratic from;
        Quadratic to;

        public Cubic(Point from, Point pivot_1, Point pivot_2, Point to) {
            this.from = new Quadratic(from, pivot_1, pivot_2);
            this.to = new Quadratic(pivot_1, pivot_2, to);
        }

        @Override
        public Point point(double t) {
            return new Linear(from.point(t), to.point(t)).point(t);
        }
    }

    public static class Poly extends Bezier {
        ArrayList<Linear> connections = new ArrayList<>();

        public Poly(List<Point> points) {
            int len = points.size();

            if (len < 2) {
                throw new IllegalArgumentException("There must be at least 2 points in the bezier curve");
            }

            for (int i = 1; i < len; i++) {
                connections.add(new Linear(points.get(i - 1), points.get(i)));
            }
        }

        public Poly(Point[] points) {
            this(Arrays.asList(points));
        }

        @Override
        public Point point(double t) {
            ArrayList<Linear> connections = new ArrayList<>(this.connections);

            int len = connections.size();

            while (len-- > 1) {
                ArrayList<Linear> newConnections = new ArrayList<>();

                for (int i = 0; i < len; i++) {
                    Linear from = connections.get(i);
                    Linear to = connections.get(i + 1);

                    newConnections.add(new Linear(from.point(t), to.point(t)));
                }

                connections = newConnections;
            }

            return connections.get(0).point(t);
        }
    }

    public static Bezier fromChar(char text, Font font, Point offset, int stiffness) {
        FontRenderContext context = new FontRenderContext(null, false, false);
        TextLayout layout = new TextLayout(String.valueOf(text), font, context);
        Shape outline = layout.getOutline(null);

        ArrayList<Point> points = new ArrayList<Point>();
        double[] coords = new double[6];

        for (PathIterator pi = outline.getPathIterator(null); !pi.isDone(); pi.next()) {
            pi.currentSegment(coords);
            Point point = new Point(coords[0], -coords[1]).add(offset);
            for (int i = 0; i < stiffness; i++) {
                points.add(point);
            }
        }

        return new Bezier.Poly(points);
    }

    public static Equation fromString(String text, Font font, Point offset, int stiffness) {
        int len = text.length();
        Bezier[] beziers = new Bezier[len];

        for (int i = 0; i < len; i++) {
            beziers[i] = fromChar(text.charAt(i), font, offset, stiffness);
            offset = offset.add(new Point(beziers[i].points(0.1).maxX() - offset.x, 0));
        }

        return Equation.combine(beziers);
    }
}
