import java.util.*;

public class Algorithm {
    private Set<Point> list;
    private double gx, gy;
    private Point[] array;

    public Algorithm(double gx, double gy) {
        list = new TreeSet<>();
        this.gx = gx;
        this.gy = gy;
    }

    public void addPoint(int x, int y) {
        Point p = new Point(x, y, gx, gy);
        list.add(p);
    }

    public void display() {
        Iterator<Point> i = list.iterator();
        while (i.hasNext()) {
            Point p1 = i.next();
            System.out.println(p1.getX() + " " + p1.getY());
        }
    }

    public Point[] findBinary(double x, double y) {
        if (list.size() < 2) {
            System.out.println("Error: Polygon does not have enough points");
            return null;
        }

        Point searchP = new Point(x, y, gx, gy);
        int n = list.size();

        array = new Point[n + 2];
        Iterator<Point> i = list.iterator();
        int k = 1;
        while (i.hasNext()) array[k++] = i.next();

        array[0] = array[n];
        array[n + 1] = array[1];

        int left = 1;
        int right = n;
        int index1 = -1, index2 = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comp = searchP.compareTo(array[mid]);

            if (comp < 0) {
                right = mid - 1;
            } else if (comp > 0) {
                left = mid + 1;
            } else {
                index1 = mid;
                index2 = mid;
                break;
            }
        }

        if (index1 == -1) {
            index1 = right;
            index2 = left;
        }

        if (index1 < 0) index1 = n;
        if (index2 > n) index2 = 1;

        Point[] rez = new Point[2];

        try {
            rez[0] = array[index1];
            rez[1] = array[index2];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Binary search index error: " + index1 + ", " + index2);
            return null;
        }

        return rez;
    }

    public int[] getX() {
        int nr = list.size();
        Iterator<Point> i = list.iterator();
        int[] xv = new int[nr];
        int k = 0;
        while (i.hasNext())
            xv[k++] = (int) (i.next().getX());
        return xv;
    }

    public int[] getY() {
        int nr = list.size();
        Iterator<Point> i = list.iterator();
        int[] yv = new int[nr];
        int k = 0;
        while (i.hasNext())
            yv[k++] = (int) (i.next().getY());
        return yv;
    }

    public double getGX() {
        return gx;
    }

    public double getGY() {
        return gy;
    }
}