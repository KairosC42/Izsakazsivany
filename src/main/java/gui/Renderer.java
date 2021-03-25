package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
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
import levelLayoutGeneration.RoomNode;
import rooms.Tile;

public class Renderer extends JPanel {

    private final JFrame frame;
    private final int window_w;
    private final int window_h;
    private int tile_size =30;
    private Timer newFrameTimer;
    //private Timer animationTimer;
    private final int FPS = 240;
    //private Image background;

    private Player player;
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
    private RoomNode[][] roomMatrix;
    private RoomNode currentRoomNode;
    private LocalTime lastTransitionTime= LocalTime.now();
    private Sprite tiles[][];
    private int n;
    private int m;
    private Image wallTexture;
    private Image floorTexture;
    private Image openDoorTexture;
    private Image closedDoorTexture;
    private Image shopDoorOpenTexture;
    private Image shopDoorClosedTexture;
    private Image itemDoorOpenTexture;
    private Image itemDoorClosedTexture;
    private Image bossDoorOpenTexture;
    private Image bossDoorClosedTexture;

    //private Vector<Enemy> enemies = new Vector<Enemy>(); //ebbe töltődnek majd be az enemy-k a szoba/level betöltésénél.
    private Enemy enemies[];


    public Renderer(int height, int width, JFrame frame) {
        super();
        //this.window_h = height;
        //this.window_w = width;
        this.frame = frame;
        handleInputs();
        this.level = new Level(1);
        this.n = level.getStartingRoom().getRoom().getN();  //20magas - sorok száma
        this.m = level.getStartingRoom().getRoom().getM();//30széles - oszlopok száma
        roomMatrix=level.getRoomMatrix();
        currentRoomNode=level.getStartingRoom();

        System.out.println(n);
        System.out.println(m);

        this.window_h = tile_size*this.m;
        this.window_w = tile_size*this.n;
        System.out.println(window_w);
        System.out.println(window_h);

        tileHeight = tile_size;
        tileWidth = tile_size;
        player_width = tileWidth;
        player_height = tileHeight;
        this.tiles = new Sprite[this.n][this.m];

        init();

        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
        System.out.println(newFrameTimer.getDelay());

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
            player = new Player(450, 100, player_height, player_width, playerImages, this.window_h,this.window_w);
            attackImg = ImageIO.read(this.getClass().getClassLoader().getResource("attack.png"));

            wallTexture = ImageIO.read(this.getClass().getClassLoader().getResource("wall.png"));
            floorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("floor.png"));
            openDoorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("door_open.png"));
            closedDoorTexture = ImageIO.read(this.getClass().getClassLoader().getResource("door_closed.png"));
            shopDoorOpenTexture = ImageIO.read(this.getClass().getClassLoader().getResource("shop_door_open.png"));
            shopDoorClosedTexture = ImageIO.read(this.getClass().getClassLoader().getResource("shop_door_closed.png"));
            itemDoorOpenTexture = ImageIO.read(this.getClass().getClassLoader().getResource("item_door_open.png"));
            itemDoorClosedTexture = ImageIO.read(this.getClass().getClassLoader().getResource("item_door_closed.png"));
            bossDoorOpenTexture = ImageIO.read(this.getClass().getClassLoader().getResource("boss_door_open.png"));
            bossDoorClosedTexture = ImageIO.read(this.getClass().getClassLoader().getResource("boss_door_closed.png"));

            System.out.println(n);
            System.out.println(m);
            currentRoomNode.getRoom().printRoom();
            level.printLevel();

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    Image actImage;
                    Tile type;
                    //getstarting
                    switch (currentRoomNode.getRoom().getLayout()[j][i]) {
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
                        case BOSSDOOR_OPEN:
                            actImage = bossDoorOpenTexture;
                            type = Tile.BOSSDOOR_OPEN;
                            break;
                        case BOSSDOOR_CLOSED:
                            actImage = bossDoorClosedTexture;
                            type = Tile.BOSSDOOR_CLOSED;
                            break;
                        case ITEMDOOR_OPEN:
                            actImage = itemDoorOpenTexture;
                            type = Tile.ITEMDOOR_OPEN;
                            break;
                        case ITEMDOOR_CLOSED:
                            actImage = bossDoorClosedTexture;
                            type = Tile.ITEMDOOR_CLOSED;
                            break;
                        case SHOPDOOR_OPEN:
                            actImage = shopDoorOpenTexture;
                            type = Tile.SHOPDOOR_OPEN;
                            break;
                        case SHOPDOOR_CLOSED:
                            actImage = bossDoorClosedTexture;
                            type = Tile.SHOPDOOR_CLOSED;
                            break;
                        //todo trapdoor

                        default:
                            actImage = wallTexture;
                            type = Tile.WALL;

                    }
                    Sprite act = new Sprite(i*tileHeight,j * tileWidth, tileWidth, tileHeight, actImage);
                    act.setType(type);

                    tiles[j][i] = act;
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
        //grphcs.fillRect(0, 0, 900, 600);
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
                        //System.out.println("collided with WALL");
                        player.stepBack();

                    }
                    if(tiles[i][j].getType()==Tile.DOOR_OPEN)
                    {
                        //System.out.println("collided with DOOR_OPEN");
                        transition(i,j);
                    }
                    if(tiles[i][j].getType()==Tile.ITEMDOOR_OPEN)
                    {
                        //System.out.println("collided with ITEMDOOR_OPEN");
                        transition(i,j);
                    }
                    if(tiles[i][j].getType()==Tile.SHOPDOOR_OPEN)
                    {
                        //System.out.println("collided with SHOPDOOR_OPEN");
                        transition(i,j);
                    }
                    if(tiles[i][j].getType()==Tile.BOSSDOOR_OPEN)
                    {
                        //System.out.println("collided with BOSSDOOR_OPEN");
                        transition(i,j);
                    }
                    if((tiles[i][j].getType()==Tile.DOOR_CLOSED))
                    {
                        //System.out.println("collided with DOOR_CLOSED");
                        player.stepBack();
                    }
                    if((tiles[i][j].getType()==Tile.BOSSDOOR_CLOSED))
                    {
                        //System.out.println("collided with BOSSDOOR_CLOSED");
                        player.stepBack();
                    }
                    if((tiles[i][j].getType()==Tile.ITEMDOOR_CLOSED))
                    {
                        //System.out.println("collided with ITEMDOOR_CLOSED");
                        player.stepBack();
                    }
                    if((tiles[i][j].getType()==Tile.SHOPDOOR_CLOSED))
                    {
                        //System.out.println("collided with SHOPDOOR_CLOSED");
                        player.stepBack();
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

    private void transition(int x, int y)
    {

        long difference=1000000;
        try
        {
            SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(lastTransitionTime.toString());
            Date date2 = format.parse(LocalTime.now().toString());
            difference = date2.getTime() - date1.getTime();
            System.out.println(date1);
            System.out.println(date2);
            System.out.println(difference);
        }
        catch (Exception e)
        {
            difference=1000000;
        }


        if( difference>=200)
        {
            //System.out.println(x +" "+ y);
            if (x == 0)
            {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i - 1][currentRoomNode.getCoordinate().j];
                player.stepBackAfterLeveltransition(player.getX(), window_w / m * (m - 4));

            } else if (y == 0)
            {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i][currentRoomNode.getCoordinate().j - 1];
                player.stepBackAfterLeveltransition(window_h / n * (n - 2), player.getY());

            } else if (x == n - 1)
            {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i + 1][currentRoomNode.getCoordinate().j];
                player.stepBackAfterLeveltransition(player.getX(), window_w / m * 2);

            } else if (y == m - 1)
            {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i][currentRoomNode.getCoordinate().j + 1];
                player.stepBackAfterLeveltransition(window_h / n * 2, player.getY());

            }


            level.printLevelWithPlayerPos(currentRoomNode);


            for (int i = 0; i < m; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    Image actImage;
                    Tile type;
                    switch (currentRoomNode.getRoom().getLayout()[j][i])
                    {
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
                        case BOSSDOOR_OPEN:
                            actImage = bossDoorOpenTexture;
                            type = Tile.BOSSDOOR_OPEN;
                            break;
                        case BOSSDOOR_CLOSED:
                            actImage = bossDoorClosedTexture;
                            type = Tile.BOSSDOOR_CLOSED;
                            break;
                        case ITEMDOOR_OPEN:
                            actImage = itemDoorOpenTexture;
                            type = Tile.ITEMDOOR_OPEN;
                            break;
                        case ITEMDOOR_CLOSED:
                            actImage = bossDoorClosedTexture;
                            type = Tile.ITEMDOOR_CLOSED;
                            break;
                        case SHOPDOOR_OPEN:
                            actImage = shopDoorOpenTexture;
                            type = Tile.SHOPDOOR_OPEN;
                            break;
                        case SHOPDOOR_CLOSED:
                            actImage = bossDoorClosedTexture;
                            type = Tile.SHOPDOOR_CLOSED;
                            break;
                        //todo trapdoor

                        default:
                            actImage = wallTexture;
                            type = Tile.WALL;

                    }
                    Sprite act = new Sprite(i * tileHeight, j * tileWidth, tileWidth, tileHeight, actImage);
                    act.setType(type);

                    tiles[j][i] = act;
                }
            }
            lastTransitionTime=LocalTime.now();
        }

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

    public Dimension getWindowSize()
    {
        Dimension dim = new Dimension(window_h,window_w);
        return dim;
    }

    //Maguktól mozgó dolgokat kell ebben az osztályban kezelni, illetve ha a mozgó objecktek ütköznek valamivel, azt is itt.
    class NewFrameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            player.moveX();
            player.moveY();
            //System.out.println(player.getX());
            //System.out.println(player.getY());
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

