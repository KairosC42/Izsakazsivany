import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
//import java.util.Scanner;
//import java.io.*;
//import javax.swing.*;
//import java.awt.EventQueue;
import javax.swing.JFrame;
//import javax.swing.JLabel;
//import java.util.Random;
import java.awt.Color;
//import java.awt.Font;

public class Renderer extends JPanel
{

    private final JFrame frame;
    private final int window_w;
    private final int window_h;
    private Timer newFrameTimer;
    private final int FPS = 240;
    private Image background;
    private Player player;
    private final int player_width = 40;
    private final int player_height = 40;

    public Renderer(int height, int width, JFrame frame)
    {
        super();
        this.window_h = height;
        this.window_w = width;
        this.frame = frame;

        //input kezelések
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                player.setVelY(-player.getMoveSpeed());
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "released up");
        this.getActionMap().put("released up", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                player.setVelY(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                player.setVelX(-player.getMoveSpeed());
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "released left");
        this.getActionMap().put("released left", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                player.setVelX(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelY(player.getMoveSpeed());
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "released down");
        this.getActionMap().put("released down", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                player.setVelY(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                player.setVelX(player.getMoveSpeed());
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "released right");
        this.getActionMap().put("released right", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                player.setVelX(0);
            }
        });


        init();
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
    }

    //Kezdő állapotban lévő elemenk létrehozása.
    public void init()
    {
        //File playerPic = new File( "/player.png" );
        Image playerImage = new ImageIcon(this.getClass().getResource("playerFront.png")).getImage();
        player = new Player(450,100,player_width, player_height, playerImage);
    }




    //Minden element kirajzolására szolgáló függvény
    @Override
    protected void paintComponent(Graphics grphcs)
    {
        grphcs.setColor(Color.darkGray);
        super.paintComponent(grphcs);
        grphcs.fillRect(0,0,900,600);
        player.draw(grphcs);
    }



    //Maguktól mozgó dolgokat kell ebben az osztályban kezelni, illetve ha a mozgó objecktek ütköznek valamivel, azt is itt.
    class NewFrameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            player.moveX();
            player.moveY();
            repaint();
        }
    }
}

