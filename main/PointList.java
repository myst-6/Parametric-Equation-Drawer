package main;

import java.util.Iterator;

// simple singly linked list class that has O(1) of add/min/max operations
public class PointList implements Iterable<Point> {
    private class Node {
        public Point value;
        public Node next;

        public Node(Point value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private Point min = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
    private Point max = new Point(Double.MIN_VALUE, Double.MIN_VALUE);
    private Node head = null;
    private Node tail = null;
    private int size = 0;

    public double minX() {
        return min.x;
    }

    public double minY() {
        return min.y;
    }

    public double maxX() {
        return max.x;
    }

    public double maxY() {
        return max.y;
    }

    public int size() {
        return size;
    }

    public void add(Point value) {
        if (head == null) {
            head = new Node(value, null);
            tail = head;
        } else {
            tail.next = new Node(value, null);
            tail = tail.next;
        }

        if (value.x < min.x)
            min.x = value.x;
        if (value.y < min.y)
            min.y = value.y;

        if (value.x > max.x)
            max.x = value.x;
        if (value.y > max.y)
            max.y = value.y;

        size++;
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<Point>() {

            private Node curr = head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public Point next() {
                Point value = curr.value;
                curr = curr.next;
                return value;
            }

        };
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("[");

        Iterator<Point> it = iterator();

        while (true) {
            string.append(String.valueOf(it.next()));

            if (it.hasNext()) {
                string.append(",");
            } else {
                break;
            }
        }

        string.append("]");

        return String.valueOf(string);
    }

    /*
     * public PointList(Point[] points) { for (Point point : points) { add(point); }
     * }
     * 
     * public PointList() { this(new Point[] {}); }
     */
}
