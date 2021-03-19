package gui;

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
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
//import javax.swing.ImageIcon;
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
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;
import java.util.Vector;
//import java.sql.Time;
//import java.time.Duration;
//import java.time.Instant;
//import java.awt.Font;
import entity.Attack;
import entity.Enemy;
import entity.Player;
import entity.Sprite;
import levelLayoutGeneration.Level;
import rooms.Tile;

public class Renderer extends JPanel {

    private final JFrame frame;
    private final int window_w;
    private final int window_h;
    private Timer newFrameTimer;
    //private Timer animationTimer;
    private final int FPS = 240;
    //private Image background;
    private Player player;
    private Enemy enemies[];
    private int player_width;
    private int player_height;
    long last_time = System.nanoTime();
    int delta_time = 0;
    long time;
    private Image attackImg;
    Vector<Attack> currentAttack = new Vector<Attack>();
    Graphics grphcs;
    private int tileHeight;
    private int tileWidth;
    private Level level;
    private Sprite tiles[][];
    private int n;
    private int m;
    private Image wallTexture;
    private Image floorTexture;
    private Image openDoorTexture;
    private Image closedDoorTexture;
    private Image shopDoorTexture;
    private Image itemDoorTexture;
    private Image bossDoorTexture;


    public Renderer(int height, int width, JFrame frame) {
        super();
        this.window_h = height;
        this.window_w = width;
        this.frame = frame;
        handleInputs();

        this.level = new Level(1);
        this.n = level.getStartingRoom().getRoom().getN(); //20magas - oszlop
        this.m = level.getStartingRoom().getRoom().getM(); //30széles - sorok száma
        tileHeight = height / this.n;
        tileWidth = width / this.m;
        player_width = tileWidth;
        player_height = tileHeight;
        this.tiles = new Sprite[this.n][this.m];

        init();

        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();

    }

    public void handleInputs() {
        //input kezelések
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelX() == 0) {
                    player.setVelY(-player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "released up");
        this.getActionMap().put("released up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelY(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelY() == 0) {
                    player.setVelX(-player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "released left");
        this.getActionMap().put("released left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelX(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelX() == 0) {
                    player.setVelY(player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "released down");
        this.getActionMap().put("released down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelY(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelY() == 0) {
                    player.setVelX(player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "released right");
        this.getActionMap().put("released right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelX(0);
            }
        });


        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "pressed space");
        this.getActionMap().put("pressed space", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Attack attack = new Attack(player.getX(), player.getY(), 10, 50, attackImg, player, enemies, player.getDirection(), player.getRange());
                currentAttack.add(attack);
            }
        });
    }

    //Kezdő állapotban lévő elemenk létrehozása.
    public void init() {
        try {
            //player képeinek betöltése
            Image playerImages[] = getImages(300, 450, 100, 150,
                    4, 4, 100, 50, "player.png");
            player = new Player(450, 100, player_height, player_width, playerImages);
            attackImg = ImageIO.read(this.getClass().getClassLoader().getResource("attack.png"));

            wallTexture = ImageIO.read(this.getClass().getClassLoader().getResource("wall.png"));
            floorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("floor.png"));
            openDoorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("door_open.png"));
            closedDoorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("door_closed.png"));
            shopDoorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("shop_door.png"));
            itemDoorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("item_door.png"));
            bossDoorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("boss_door.png"));

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    Image actImage;
                    Tile type;
                    //getstarting
                    switch (level.getStartingRoom().getRoom().getLayout()[i][j]) {
                        case WALL:
                            actImage = wallTexture;
                            type = Tile.WALL;
                            break;
                        case FLOOR:
                            actImage = floorTexture;
                            type = Tile.FLOOR;
                            break;
                        case DOOR_OPEN:
                            actImage = openDoorTexture;
                            type = Tile.DOOR_OPEN;
                            break;
                        case DOOR_CLOSED:
                            actImage = closedDoorTexture;
                            type = Tile.DOOR_CLOSED;
                            break;
                        case BOSSDOOR:
                            actImage = bossDoorTexture;
                            type = Tile.BOSSDOOR;
                            break;
                        case ITEMDOOR:
                            actImage = itemDoorTexture;
                            type = Tile.ITEMDOOR;
                            break;
                        case SHOPDOOR:
                            actImage = shopDoorTexture;
                            type = Tile.SHOPDOOR;
                            break;
                        //todo trapdoor

                        default:
                            actImage = wallTexture;
                            type = Tile.WALL;

                    }
                    Sprite act = new Sprite(i * tileHeight, j * tileWidth, tileHeight, tileWidth, actImage);
                    act.setType(type);

                    tiles[i][j] = act;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void nextRoom()
    {
        //toDo
    }

    //egy képmátrix felbontására szolgáló függvény
    /*
    width - egy képrészlet szélessége
    height - egy képrészlet magassága
    width_margin - két képrészlet közti távolság egy sorban
    width_height - két képrészlet közti távolság egy sorban
    starter-height - a legfelső oszlop távolsága a nagy kép szélétől
    starter-width - a legbaloldalabb lévő oszlop távolsága a nagy kép szélétől
    fileName- a nagy kép amit fel akarunk bontani
    images[] - a tömb amiben szeretnénk tárolni a képeket.
     */
    public Image[] getImages(int width, int height, int width_margin,
                             int height_margin, int rows, int cols, int starter_height, int starter_width, String fileName)
            throws IOException {

        // packagek előtt:  BufferedImage bigImg = ImageIO.read(this.getClass().getResource(fileName));
        BufferedImage bigImg = ImageIO.read(this.getClass().getClassLoader().getResource(fileName));

        Image images[] = new Image[rows * cols];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                images[(i * cols) + j] = bigImg.getSubimage(
                        starter_width + (j * (width + width_margin)),
                        starter_height + (i * (height + height_margin)),
                        width,
                        height
                );
            }
        }
        return images;
    }

    //Minden element kirajzolására szolgáló függvény
    @Override
    protected void paintComponent(Graphics grphcs)
    {
        grphcs.setColor(Color.darkGray);
        super.paintComponent(grphcs);
        grphcs.fillRect(0, 0, 900, 600);
        drawRoom(grphcs);
        player.draw(grphcs);
        for (Attack att : currentAttack)
        {
            att.draw(grphcs);
        }

        collide();

    }

    public void collide()
    {
        for(int i = 0; i <n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                if(tiles[i][j].collides(player))
                {
                    //case-l szebb lehet ez
                    if(tiles[i][j].getType()==Tile.WALL)
                    {
                        //stepback()
                    }
                    if(tiles[i][j].getType()==Tile.DOOR_OPEN)
                    {
                        //transition()
                    }
                    if((tiles[i][j].getType()==Tile.DOOR_CLOSED))
                    {
                        //stepback()
                    }
                    if((tiles[i][j].getType()==Tile.BOSSDOOR))
                    {
                        //stepback()
                    }
                    if((tiles[i][j].getType()==Tile.ITEMDOOR))
                    {
                        //stepback()
                    }
                    if((tiles[i][j].getType()==Tile.SHOPDOOR))
                    {
                        //stepback()
                    }
                    if((tiles[i][j].getType()==Tile.TRAPDOOR_OPEN))
                    {
                        //next_level()
                    }
                }
            }
        }
        //enemy collision
        /*
        végig megyünk az enemyk listáján és megnézzük, hogy nekiment-e a player
         */
    }

    public void drawRoom(Graphics grphcs)
    {
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                tiles[i][j].draw(grphcs);
            }
        }
    }

    //Maguktól mozgó dolgokat kell ebben az osztályban kezelni, illetve ha a mozgó objecktek ütköznek valamivel, azt is itt.
    class NewFrameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            player.moveX();
            player.moveY();
            if(currentAttack.size()>0)
            {
                for (Attack attack : currentAttack)
                {
                    attack.cast();
                }
            }

            clearAttacks();
            //TODO animilás
            /*animate(delta_time);
            time = System.nanoTime();
            delta_time = (int) ((time - last_time) / 1000000);
            last_time = time;
             */


            repaint();
        }

        void clearAttacks()
        {
            for(int i = 0; i < currentAttack.size(); i++)
            {
                if(currentAttack.get(i).isEnded() == true)
                {
                    currentAttack.remove(currentAttack.get(i));
                }
            }
        }
    }
}

