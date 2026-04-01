public class Point implements Comparable<Point> {
    private double x;
    private double y;
    private int quadrant;
    private double gx, gy;

    public Point(double x, double y, double gx, double gy) {
        this.x = x;
        this.y = y;
        this.gx = gx;
        this.gy = gy;
        calculateQuadrant();
        System.out.println(quadrant == 0 ? "err quadrant" : "quadrant " + quadrant);
    }

    @Override
    public int compareTo(Point p) {
        if (quadrant < p.getQuadrant()) return -1;
        if (quadrant > p.getQuadrant()) return 1;

        double crossProduct = (this.x - this.gx) * (p.getY() - this.gy) -
                              (this.y - this.gy) * (p.getX() - this.gx);

        if (crossProduct < 0) return 1;
        if (crossProduct > 0) return -1;
        return 0;
    }

    public void calculateQuadrant() {
        if (x == gx && y == gy) {
            quadrant = 0;
            return;
        }
        if ((x > gx) && (y >= gy)) quadrant = 1;
        else if ((x <= gx) && (y > gy)) quadrant = 2;
        else if ((x < gx) && (y <= gy)) quadrant = 3;
        else if ((x >= gx) && (y < gy)) quadrant = 4;
        else quadrant = 0;
    }

    public int getQuadrant() {
        return quadrant;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}