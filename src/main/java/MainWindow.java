import javax.swing.JFrame;
import java.awt.Dimension;

public class MainWindow
{
    private JFrame frame;
    Renderer gameRenderer;

    public MainWindow() {
        frame = new JFrame("Izsák A Zsivány");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.gameRenderer = new Renderer(800,800,frame);
        frame.getContentPane().add(gameRenderer);

        frame.setPreferredSize(new Dimension(800, 850));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
