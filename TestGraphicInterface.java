import javax.swing.*;

public class TestGraphicInterface {
    public static void main(String[] args) {
        var frame = new GraphicInterface();
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
