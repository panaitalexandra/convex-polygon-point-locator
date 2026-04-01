import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphicInterface extends JFrame {
    private int n;
    private Drawing canvas;
    private JTextField nr, coord, xi, yi, out;
    private int[] x, y;
    private int counter = 0;
    private JButton add;
    private ButtonListener bl;
    private boolean completed = false;

    private final Color MAIN_BACKGROUND = new Color(243, 206, 241);
    private final Color PANEL_BACKGROUND = new Color(243, 221, 243);
    private final Color ACCENT_PINK = new Color(220, 64, 142);
    private final Color TEXT = new Color(0, 0, 0);
    private final Color BUTTON_COLOR = ACCENT_PINK;
    private final Font TEXT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);

    public GraphicInterface() {
        this.setLayout(new BorderLayout());
        this.setTitle("Polygon Input");
        this.setBackground(MAIN_BACKGROUND);

        bl = new ButtonListener();

        nr = new JTextField(2);
        xi = new JTextField(3);
        yi = new JTextField(3);
        out = new JTextField(10);
        out.setEditable(false);

        add = new JButton("Add Point");
        add.addActionListener(bl);

        canvas = new Drawing();
        canvas.setBackground(PANEL_BACKGROUND);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        controlPanel.setBackground(MAIN_BACKGROUND);

        controlPanel.add(createInputPanel("No. points:", nr));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel coordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        coordPanel.setBackground(MAIN_BACKGROUND);
        coordPanel.add(createStyledLabel("Coordinates:"));
        coordPanel.add(createStyledLabel("X:"));
        coordPanel.add(xi);
        coordPanel.add(createStyledLabel("Y:"));
        coordPanel.add(yi);
        controlPanel.add(coordPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(MAIN_BACKGROUND);
        buttonPanel.add(add);
        controlPanel.add(buttonPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        controlPanel.add(createInputPanel("Output:", out));
        this.add(controlPanel, BorderLayout.WEST);
        this.add(canvas, BorderLayout.CENTER);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(LABEL_FONT);
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setMaximumSize(field.getPreferredSize());
        field.setBackground(PANEL_BACKGROUND);
        field.setForeground(ACCENT_PINK);
        field.setCaretColor(TEXT);
        field.setFont(TEXT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_PINK.darker(), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(LABEL_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BUTTON_COLOR.darker(), 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(BUTTON_COLOR.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        return button;
    }

    private JPanel createInputPanel(String labelText, JComponent field) {
        JPanel fieldWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldWrapper.setBackground(MAIN_BACKGROUND);
        fieldWrapper.add(field);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(MAIN_BACKGROUND);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setBackground(MAIN_BACKGROUND);
        labelPanel.add(createStyledLabel(labelText));

        p.add(labelPanel);
        p.add(fieldWrapper);

        return p;
    }

    public class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == add) {
                if (!completed) {
                    try {
                        n = Integer.parseInt(nr.getText());
                        if (n < 3)
                            throw new NumberFormatException("Number of points must be at least 3");
                        completed = true;
                        x = new int[n];
                        y = new int[n];
                        GraphicInterface.this.canvas.setN(n);
                        nr.setEnabled(false);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number of points! (Min 3)", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (completed) {
                    int tempX = readX();
                    int tempY = readY();

                    if (tempX == -Integer.MAX_VALUE || tempY == -Integer.MAX_VALUE) {
                        xi.setText("");
                        yi.setText("");
                        return;
                    }

                    x[counter] = tempX;
                    y[counter] = tempY;
                    xi.setText("");
                    yi.setText("");

                    GraphicInterface.this.canvas.drawLine(x, y, counter);
                    counter++;
                    System.out.println("Points entered: " + counter + "/" + n);

                    if (counter == n) {
                        double sumX = 0, sumY = 0;
                        for (int val : x) sumX += val;
                        for (int val : y) sumY += val;

                        Algorithm a = new Algorithm(sumX / n, sumY / n);
                        for (int i = 0; i < x.length; ++i)
                            a.addPoint(x[i], y[i]);
                        GraphicInterface.this.canvas.setAlgorithm(a);
                        GraphicInterface.this.add.setEnabled(false);
                        GraphicInterface.this.canvas.setOutput(out);
                    }
                }
            }
        }

        private int readX() {
            try {
                int val = Integer.parseInt(xi.getText());
                if (val < -20 || val > +20)
                    throw new NumberFormatException("X coordinate must be between -20 and 20.");
                return val;
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for the X coordinate! (-20 to 20)", "Input Error", JOptionPane.ERROR_MESSAGE);
                return -Integer.MAX_VALUE;
            }
        }

        private int readY() {
            try {
                int val = Integer.parseInt(yi.getText());
                if (val < -20 || val > +20)
                    throw new NumberFormatException("Y coordinate must be between -20 and 20");
                return val;
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for the Y coordinate! (-20 to 20)", "Input Error", JOptionPane.ERROR_MESSAGE);
                return -Integer.MAX_VALUE;
            }
        }
    }
}