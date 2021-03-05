import javax.swing.JFrame;
import java.awt.Dimension;

public class MainWindow
{
    private JFrame frame;


    public MainWindow() {
        frame = new JFrame("Izsák A Zsivány");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setPreferredSize(new Dimension(800, 850));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

    }
}
