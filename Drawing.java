import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

public class Drawing extends Canvas {
    private boolean mouseStart = false, mouseSelect = false, solution = false, poly = false;
    private int[] xPoints, yPoints;
    private Color[] edgeColors;
    private int pointCount = -1, mouseX, mouseY, totalPoints;
    private double Mx;
    private double My;
    private JTextField output;
    private Algorithm a;
    private Point[] point;
    private int centerX, centerY;

    public Drawing() {
        setSize(500, 500);
        xPoints = new int[0];
        yPoints = new int[0];
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 500, 0);
        g.drawLine(0, 0, 0, 500);
        g.drawLine(500, 0, 500, 500);
        g.drawLine(0, 500, 500, 500);

        g.drawLine(calcX(-20), calcY(0), calcX(20), calcY(0));
        g.drawString("X", calcX(21), calcY(-1));
        g.drawLine(calcX(20) - 3, calcY(0) - 3, calcX(20), calcY(0));
        g.drawLine(calcX(20) - 3, calcY(0) + 3, calcX(20), calcY(0));

        g.drawLine(calcX(0), calcY(-20), calcX(0), calcY(20));
        g.drawString("Y", calcX(-1), calcY(21));
        g.drawLine(calcX(0) + 3, calcY(20) + 3, calcX(0), calcY(20));
        g.drawLine(calcX(0) - 3, calcY(20) + 3, calcX(0), calcY(20));

        if (poly) {
            for (int i = 0; i < xPoints.length; ++i)
                System.out.println(xPoints[i] + " " + yPoints[i]);

            for (int i = 1; i < xPoints.length; ++i)
                g.drawLine(calcX(xPoints[i - 1]), calcY(yPoints[i - 1]), calcX(xPoints[i]), calcY(yPoints[i]));

            g.drawLine(calcX(xPoints[0]), calcY(yPoints[0]), calcX(xPoints[xPoints.length - 1]), calcY(yPoints[xPoints.length - 1]));

            centerX = 250 + (int) (a.getGX() * 10.0);
            centerY = 250 - (int) (a.getGY() * 10.0);

            System.out.println(centerX + " center " + centerY);

            for (int i = 0; i < xPoints.length; ++i)
                g.drawLine(centerX, centerY, calcX(xPoints[i]), calcY(yPoints[i]));

            g.fillOval(centerX - 2, centerY - 2, 5, 4);
        }

        if (mouseSelect) {
            g.fillOval(mouseX - 2, mouseY - 2, 5, 4);
            point = a.findBinary(Mx, My);
            mouseStart = true;
            solution = true;
        }

        if (solution && point != null && point.length == 2 && point[0] != null && point[1] != null) {
            g.setColor(Color.blue);
            g.drawLine(calcX((int) (point[0].getX())), calcY((int) (point[0].getY())), calcX((int) (point[1].getX())), calcY((int) (point[1].getY())));
            g.drawLine(centerX, centerY, calcX((int) (point[0].getX())), calcY((int) (point[0].getY())));
            g.drawLine(centerX, centerY, calcX((int) (point[1].getX())), calcY((int) (point[1].getY())));

            double det = (point[1].getX() - point[0].getX()) * (My - point[0].getY()) -
                    (point[1].getY() - point[0].getY()) * (Mx - point[0].getX());

            System.out.println(det);

            if (det > 0)
                output.setText("Inside");
            else if (det < 0)
                output.setText("Outside");
            else
                output.setText("Frontier");
        }
    }

    public int calcX(int x) {
        return 250 + x * 10;
    }

    public int calcY(int y) {
        return 250 - y * 10;
    }

    @Override
    public boolean mouseDown(Event e, int x, int y) {
        if (mouseStart) {
            mouseX = x;
            mouseY = y;
            Mx = (mouseX - 250) / 10.0;
            My = (250 - mouseY) / 10.0;
            mouseSelect = true;
            mouseStart = false;
            repaint();
        }
        return true;
    }

    public void setN(int n) {
        totalPoints = n;
        edgeColors = new Color[n + 1];
    }

    public void setOutput(JTextField out) {
        output = out;
    }

    public void setAlgorithm(Algorithm a) {
        this.a = a;
        mouseStart = true;
        xPoints = a.getX();
        yPoints = a.getY();
        poly = true;
        a.display();
        repaint();
    }

    public int[] copyX(int[] source) {
        xPoints = new int[pointCount];
        for (int i = 0; i < pointCount; i++)
            xPoints[i] = source[i];
        return xPoints;
    }

    public int[] copyY(int[] source) {
        yPoints = new int[pointCount];
        for (int i = 0; i < pointCount; i++)
            yPoints[i] = source[i];
        return yPoints;
    }

    public void drawLine(int[] xInput, int[] yInput, int count) {
        pointCount = count + 1;
        xPoints = copyX(xInput);
        yPoints = copyY(yInput);
        repaint();
    }
}