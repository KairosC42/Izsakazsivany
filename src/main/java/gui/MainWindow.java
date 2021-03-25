package gui;

import gui.Renderer;

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

        Dimension window_size = gameRenderer.getWindowSize();
        window_size.height+=38;
        window_size.width+=16;
        frame.setPreferredSize(window_size);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
