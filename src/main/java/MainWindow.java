import javax.swing.JFrame;
import java.awt.Dimension;

public class MainWindow
{
    private JFrame frame;
    Renderer gameRenderer;

    public MainWindow() {
        frame = new JFrame("Izsák A Zsivány");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.gameRenderer = new Renderer(900,600,frame);
        frame.getContentPane().add(gameRenderer);

        frame.setPreferredSize(new Dimension(900, 650));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
