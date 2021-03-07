import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;

public class Renderer extends JPanel {

    private JFrame frame;
    private int window_w;
    private int window_h;
    private Timer newFrameTimer;
    private final int FPS = 240;
    private Image background = new ImageIcon("../resources/background.jpg").getImage();

    public Renderer(int height, int width, JFrame frame) {
        super();
        this.window_h = height;
        this.window_w = width;
        this.frame = frame;

        //restart();
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
    }


    //Minden element kirajzolására szolgáló függvény
    @Override
    protected void paintComponent(Graphics grphcs) {
        grphcs.setColor(Color.black);
        super.paintComponent(grphcs);
    }

    //Maguktól mozgó dolgokat kell ebben az osztályban kezelni, illetve ha a mozgó objecktek ütköznek valamivel, azt is itt.
    class NewFrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            repaint();
        }
    }
}

