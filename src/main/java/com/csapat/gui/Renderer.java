package com.csapat.gui;

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
import javax.swing.*;
//import javax.swing.ImageIcon;
//import java.util.Scanner;
//import java.io.*;
//import javax.swing.*;
//import java.awt.EventQueue;
//import javax.swing.JLabel;
//import java.util.Random;
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;
import java.util.Vector;
//import java.sql.Time;
//import java.time.Duration;
//import java.time.Instant;
//import java.awt.Font;
import com.csapat.entity.*;
import com.csapat.levelLayoutGeneration.Level;
import com.csapat.levelLayoutGeneration.RoomNode;
import com.csapat.rooms.ItemRoom;
import com.csapat.rooms.RoomType;
import com.csapat.rooms.Shop;
import com.csapat.rooms.Tile;


public class Renderer extends JPanel
{
    Item selectedItem;

    boolean isAdded=false;
    private JFrame frame;
    private int window_w;
    private int window_h;
    private int tile_size =30;
    private Timer newFrameTimer;
    //private Timer animationTimer;
    private final int FPS = 120;
    //private Image background;
    private Player player;
    private int player_width = 40;
    private int player_height = 40;
    long last_time = System.nanoTime();
    int delta_time = 0;
    long time;
    private Image attackImg;
    Vector<Item> items;
    Vector<Attack> currentAttacks = new Vector<Attack>();
    Graphics grphcs;
    JLabel purchaseHint;

    private Vector<JLabel> itemStatLabels;

    private int tileHeight;
    private int tileWidth;
    private Level level;


    private Sprite tiles[][];
    private int n;
    private int m;
    private int levelDepth = 1;
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
    private Image hearthTexture;
    private Image trapDoorOpenTexture;
    private Image trapDoorClosedTexture;
    private Image enemyTexture;
    private Image[] playerImages;

    private Vector<Sprite> enemies = new Vector<>();
    // ebbe töltődnek majd be az enemy-k a szoba/level betöltésénél.


    private RoomNode[][] roomMatrix;
    private RoomNode currentRoomNode;
    private LocalTime lastTransitionTime= LocalTime.now();

    private Sprite hearthSprite;

    private Boolean collide_timer_down=true;
    private java.util.Timer collide_with_enemy;

    private Boolean attack_timer_down=true;
    private java.util.Timer attack_timer;




    public Renderer(int height, int width, JFrame frame) {
        super();
        this.window_h = height;
        this.window_w = width;
        this.frame = frame;

        purchaseHint= new JLabel();


        handleInputs();
        this.level = new Level(levelDepth);
        this.n = level.getStartingRoom().getRoom().getN();  //20magas - sorok száma
        this.m = level.getStartingRoom().getRoom().getM();//30széles - oszlopok száma
        roomMatrix=level.getRoomMatrix();


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

        currentRoomNode=level.getStartingRoom();
        initGraphics();

        player = new Player(450,100, player_height, player_width,playerImages,this.window_h,this.window_w);
        //levelDepth + 2
        //testing for only 1 enemy

        for (int i = 0; i < 1; ++i)
        {
            enemies.add(new Enemy(200 + 50 * i, 400 + 50 * i, 50, 50, enemyTexture,10));
        }

        initTiles();

        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();

    }

    public void newLevel()
    {
        newFrameTimer.stop();
        //timer is stopped here, restarted later, to avoid any weird behaviour while a new floor is generated and loaded
        this.level=new Level(++levelDepth);
        //the rest of the things set in the constructor are still valid here

        this.player.setX(n/2*tileHeight);
        this.player.setY(m/2*tileWidth);
        currentRoomNode=level.getStartingRoom();
        roomMatrix=level.getRoomMatrix();
        //player starts in the middle of the room
        initTiles();
        // calling init() is a temporary solution, as that also resets textures, realistically i only want the starting room's textures and layout
        repaint();
        newFrameTimer.start();
    }

    public void handleInputs()
    {
        //input kezelések
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if(player.getVelX() == 0)
                {
                    player.setVelY(-player.getMoveSpeed());
                }
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
                if(player.getVelY() == 0)
                {
                    player.setVelX(-player.getMoveSpeed());
                }
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
            public void actionPerformed(ActionEvent ae)
            {
                if(player.getVelX() == 0)
                {
                    player.setVelY(player.getMoveSpeed());
                }
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
        this.getActionMap().put("pressed right", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if(player.getVelY() == 0)
                {
                    player.setVelX(player.getMoveSpeed());
                }
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


        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "pressed space");
        this.getActionMap().put("pressed space", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(attack_timer_down)
                {
                    attack_timer_down = false;
                    Attack attack = new Attack(player.getX(), player.getY(), 10,50, attackImg , player, enemies, player.getDirection(), player.getRange());
                    currentAttacks.add(attack);
                    attack_timer = new java.util.Timer();
                    attack_timer.schedule(new attackTask(), (int)(1000/player.getAttackSpeed()));
                }
            }
        });


        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, false), "pressed b");
        this.getActionMap().put("pressed b", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(selectedItem !=null)
                if(player.getMoney()>= selectedItem.getPrice())
                {
                    player.buyItem(selectedItem);
                    remove(itemStatLabels.get(items.indexOf(selectedItem)));
                    itemStatLabels.remove(itemStatLabels.get(items.indexOf(selectedItem)));
                    items.remove(selectedItem);
                    if(currentRoomNode.getRoomType()==RoomType.SHOP)
                    {
                        ((Shop)currentRoomNode.getRoom()).removeItem(selectedItem);
                    }
                    if(currentRoomNode.getRoomType()==RoomType.ITEMROOM)
                    {
                        ((ItemRoom)currentRoomNode.getRoom()).removeItem(selectedItem);
                    }

                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "pressed q");
        this.getActionMap().put("pressed q", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.useHealthPotion();

            }
        });
    }

    public void initGraphics()
    {
        try
        {
            //player képeinek betöltése
            playerImages = getImages(300,450,100,150,
                    4,4,100,50,"player.png");

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
            trapDoorOpenTexture = ImageIO.read(this.getClass().getClassLoader().getResource("trapdoor_open.png"));
            trapDoorClosedTexture=ImageIO.read(this.getClass().getClassLoader().getResource("trapdoor_closed.png"));
            hearthTexture = ImageIO.read(this.getClass().getClassLoader().getResource("hearth.png"));
            enemyTexture = ImageIO.read(this.getClass().getClassLoader().getResource("enemy.png"));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



            hearthSprite = new Sprite(window_w+180,7,45,40,hearthTexture);

            System.out.println(n);
            System.out.println(m);
            currentRoomNode.getRoom().printRoom();
            level.printLevel();



    }
    //Kezdő állapotban lévő elemenk létrehozása.
    public void initTiles() {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    Image actImage;
                    Tile type;
                    //getstarting
                    switch (currentRoomNode.getRoom().getLayout()[j][i]) {
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
                            actImage = itemDoorClosedTexture;
                            type = Tile.ITEMDOOR_CLOSED;
                            break;
                        case SHOPDOOR_OPEN:
                            actImage = shopDoorOpenTexture;
                            type = Tile.SHOPDOOR_OPEN;
                            break;
                        case SHOPDOOR_CLOSED:
                            actImage = shopDoorClosedTexture;
                            type = Tile.SHOPDOOR_CLOSED;
                            break;
                        case TRAPDOOR_OPEN:
                            actImage = trapDoorOpenTexture;
                            type = Tile.TRAPDOOR_OPEN;
                            break;
                        case TRAPDOOR_CLOSED:
                            actImage = trapDoorClosedTexture;
                            type = Tile.TRAPDOOR_CLOSED;
                            break;
                        default:
                            actImage = wallTexture;
                            type = Tile.WALL;
                    }
                    Sprite act = new Sprite(i * tileHeight, j * tileWidth, tileWidth ,tileHeight, actImage);
                    act.setType(type);
                    tiles[j][i] = act;
                }
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
                          int height_margin, int rows, int cols, int starter_height, int starter_width,String fileName)
            throws IOException
    {

        // packagek előtt:  BufferedImage bigImg = ImageIO.read(this.getClass().getResource(fileName));
        BufferedImage bigImg = ImageIO.read(this.getClass().getClassLoader().getResource(fileName));

        Image images[] = new Image[rows * cols];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                images[(i * cols) + j] = bigImg.getSubimage(
                        starter_width + (j * (width+width_margin)),
                        starter_height+ (i * (height+height_margin)),
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

        if(this.itemStatLabels!=null)
        {
            for (JLabel label : itemStatLabels)
            {
                this.add(label);
            }
        }
        if(items!=null)
        {
            for (Item it : items) {
                it.draw(grphcs);
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(grphcs);
        }
        for (Attack att : currentAttacks)
        {
            att.draw(grphcs);
        }



        Graphics2D g2 = (Graphics2D)grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        Font font = new Font("SansSerif", Font.BOLD, 40);
        g2.setFont(font);
        hearthSprite.draw(grphcs);
        g2.drawString(Integer.toString(+player.getHealth()),window_w+230,40);
        player.draw(grphcs);

        collide();

    }


    public void collide()
    {
        for(int i = 0; i <n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                //Player
                if(tiles[i][j].collides(player))
                {
                    //case-l szebb lehet ez
                    if (tiles[i][j].getType() == Tile.WALL)
                    {
                        //System.out.println("collided with WALL");
                        player.stepBack();

                    }
                    if (tiles[i][j].getType() == Tile.DOOR_OPEN)
                    {
                        //System.out.println("collided with DOOR_OPEN");
                        transition(i, j);
                    }
                    if (tiles[i][j].getType() == Tile.ITEMDOOR_OPEN)
                    {
                        //System.out.println("collided with ITEMDOOR_OPEN");
                        transition(i, j);
                    }
                    if (tiles[i][j].getType() == Tile.SHOPDOOR_OPEN)
                    {
                        //System.out.println("collided with SHOPDOOR_OPEN");
                        transition(i, j);
                    }
                    if (tiles[i][j].getType() == Tile.BOSSDOOR_OPEN)
                    {
                        //System.out.println("collided with BOSSDOOR_OPEN");
                        transition(i, j);
                    }
                    if ((tiles[i][j].getType() == Tile.DOOR_CLOSED))
                    {
                        //System.out.println("collided with DOOR_CLOSED");
                        player.stepBack();
                    }
                    if ((tiles[i][j].getType() == Tile.BOSSDOOR_CLOSED))
                    {
                        //System.out.println("collided with BOSSDOOR_CLOSED");
                        player.stepBack();
                    }
                    if ((tiles[i][j].getType() == Tile.ITEMDOOR_CLOSED))
                    {
                        //System.out.println("collided with ITEMDOOR_CLOSED");
                        player.stepBack();
                    }
                    if ((tiles[i][j].getType() == Tile.SHOPDOOR_CLOSED))
                    {
                        //System.out.println("collided with SHOPDOOR_CLOSED");
                        player.stepBack();
                    }
                    if ((tiles[i][j].getType() == Tile.TRAPDOOR_OPEN))
                    {
                        newLevel();
                    }

                }
                    for (int k = 0; k < enemies.size(); k++)
                    {

                        if (tiles[i][j].collides(enemies.get(k)))
                        {

                            //case-l szebb lehet ez,

                            if (tiles[i][j].getType() == Tile.WALL)
                            {
                                ((Enemy) enemies.get(k)).moveBack();
                                ((Enemy) enemies.get(k)).randDirection();
                            }
                            if (tiles[i][j].getType() == Tile.DOOR_OPEN)
                            {
                                //System.out.println("collided with DOOR_OPEN");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if (tiles[i][j].getType() == Tile.ITEMDOOR_OPEN)
                            {
                                //System.out.println("collided with ITEMDOOR_OPEN");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if (tiles[i][j].getType() == Tile.SHOPDOOR_OPEN)
                            {
                                //System.out.println("collided with SHOPDOOR_OPEN");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if (tiles[i][j].getType() == Tile.BOSSDOOR_OPEN)
                            {
                                //System.out.println("collided with BOSSDOOR_OPEN");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if ((tiles[i][j].getType() == Tile.DOOR_CLOSED))
                            {
                                //System.out.println("collided with DOOR_CLOSED");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if ((tiles[i][j].getType() == Tile.BOSSDOOR_CLOSED))
                            {
                                //System.out.println("collided with BOSSDOOR_CLOSED");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if ((tiles[i][j].getType() == Tile.ITEMDOOR_CLOSED))
                            {
                                //System.out.println("collided with ITEMDOOR_CLOSED");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if ((tiles[i][j].getType() == Tile.SHOPDOOR_CLOSED))
                            {
                                //System.out.println("collided with SHOPDOOR_CLOSED");
                                ((Enemy) enemies.get(k)).moveBack();
                            }
                            if ((tiles[i][j].getType() == Tile.TRAPDOOR_OPEN))
                            {
                                ((Enemy) enemies.get(k)).moveBack();

                            }
                            if (enemies.get(k).collides(player))
                            {
                                if(collide_timer_down)
                                {
                                    collide_timer_down=false;
                                    player.setHealth(player.getHealth()-((Enemy)enemies.get(k)).getDamage());
                                    collide_with_enemy = new java.util.Timer();
                                    collide_with_enemy.schedule(new collideTask(),500);
                                }
                            }
                        }
                }
            }
        }
        boolean didCollideWithItem = false;
        if(items!=null)
        {
            for (Item item : items)
            {
                if (item.collides(player))
                {
                    didCollideWithItem= didCollideWithItem||item.collides(player);
                    selectedItem = item;
                    if(!isAdded) {
                        purchaseHint = new JLabel();
                        purchaseHint.setText("<html><body>If you want to buy an item<br> press <b>b</b> while on top of it!</body></html>");
                        purchaseHint.setBounds(tileWidth, tileHeight, purchaseHint.getFont().getSize() * 15, purchaseHint.getFont().getSize() * 4);
                        purchaseHint.setBackground(new Color(200, 200, 200));
                        purchaseHint.setOpaque(true);
                        this.add(purchaseHint);
                        isAdded=true;
                    }
                }
            }
            if(!didCollideWithItem)
            {
                selectedItem = null;
                if(purchaseHint!=null) remove(purchaseHint);
                isAdded=false;
            }
        }
        else
        {
            selectedItem = null;
            if(purchaseHint!=null) remove(purchaseHint);
            isAdded=false;
        }

        for(int a = 0; a < currentAttacks.size(); a++)
        {
            for(int e = 0; e < enemies.size(); e++)
            {
                if(enemies.get(e).collides(currentAttacks.get(a))&&!((Enemy)(enemies.get(e))).getGotAttacked())
                {
                    Enemy en= ((Enemy)(enemies.get(e)));
                    en.setGotAttacked(true);
                    if(en.damaged(player.getDamage(), player.getAttackSpeed()))
                    {
                        //meghalt
                        enemies.remove(en);
                    }
                }
            }
        }

    }
    private void generateItemStatLabels()
    {
        int verticalGapSize = Math.round( window_w*0.8f*0.33f);
        for(Item item : items)
        {
            Color bgColor = new Color(200,200,200);
            if(item.getName().equals("weapon"))
            {
                Weapon tmp = (Weapon)item;
                JLabel itemStat = new JLabel(
                        "Name: " + tmp.getName() +"\n"+
                                "Rangemod: " + tmp.getRangeModifier()+ "%"+"\n"+
                                "Damagemod: " + tmp.getDamageModifier()+ "%"+"\n"+
                                "AttSpdmod: " + tmp.getAttackSpeedModifier()+ "%" +"\n"+
                                "Price: " + tmp.getPrice()
                        , null, SwingConstants.LEFT);
                if(tmp.getPrice()!=0)
                {
                    itemStat.setText(
                        "<html><body>" +
                                "Name: " + tmp.getName() +"<br>"+
                                "Range: +" + tmp.getRangeModifier()+ "<br>"+
                                "Damage: +" + tmp.getDamageModifier()+ "<br>"+
                                "Attackspeed: +" + tmp.getAttackSpeedModifier()*100+ "%" +"<br>"+
                                "Price: " + tmp.getPrice() +
                                "</body></html>");
                }
                else
                {
                    itemStat.setText(
                            "<html><body>" +
                                    "Name: " + tmp.getName() +"<br>"+
                                    "Range: +" + tmp.getRangeModifier()+ "<br>"+
                                    "Damage: +" + tmp.getDamageModifier()+ "<br>"+
                                    "Attackspeed: +" + tmp.getAttackSpeedModifier()*100+ "%" +"<br>" +
                                    "</body></html>");
                }
                itemStat.setBackground(bgColor);
                itemStat.setOpaque(true);
                int fontSize = itemStat.getFont().getSize();
                int lineCount = 5;
                if(tmp.getPrice()==0)lineCount--;
                itemStat.setBounds(tmp.getX() + tmp.getWidth(),tmp.getY(),verticalGapSize-tmp.getWidth()/2,fontSize*lineCount + lineCount*4 );
                this.itemStatLabels.add(itemStat);
            }
            if(item.getName().equals("statItem"))
            {
                StatItem tmp = (StatItem)item;
                JLabel itemStat = new JLabel(
                        "Name:" + tmp.getName() +"\n"+
                                "Health: +" + tmp.getHealthModifier() + "\n"+
                                "Range: +" +tmp.getRangeModifier()+"%"+"\n"+
                                "Attack speed +: " +tmp.getRangeModifier()+"%"+"\n"+
                                "Damage +: " +tmp.getDamageModifier()+"%"+"\n"+
                                "Speed +: " + tmp.getSpeedModifier()+"%" +"\n"+
                                "Price: " + tmp.getPrice()
                        , null, SwingConstants.LEFT);
                if(tmp.getPrice()!=0) {
                    itemStat.setText(
                            "<html><body>" +
                                    "Name: " + tmp.getName() + "<br>" +
                                    "Health: +" + tmp.getHealthModifier() + "<br>" +
                                    "Range: +" + tmp.getRangeModifier() * 100 + "%" + "<br>" +
                                    "Damage: +" + tmp.getDamageModifier() * 100 + "%" + "<br>" +
                                    "Attack speed: +" + tmp.getAttackSpeedModifier() * 100 + "%" + "<br>" +
                                    "MoveMove speed: +" + tmp.getSpeedModifier() * 100 + "%" + "<br>" +
                                    "Price: " + tmp.getPrice() +
                                    "</body></html>"

                    );
                }
                else
                {
                    itemStat.setText(
                            "<html><body>" +
                                    "Name: " + tmp.getName() + "<br>" +
                                    "Health: +" + tmp.getHealthModifier() + "<br>" +
                                    "Range: +" + tmp.getRangeModifier() * 100 + "%" + "<br>" +
                                    "Damage: +" + tmp.getDamageModifier() * 100 + "%" + "<br>" +
                                    "Attack speed: +" + tmp.getAttackSpeedModifier() * 100 + "%" + "<br>" +
                                    "Move speed: +" + tmp.getSpeedModifier() * 100 + "%" + "<br>" +
                                    "</body></html>");
                }
                itemStat.setBackground(bgColor);
                itemStat.setOpaque(true);
                int fontSize = itemStat.getFont().getSize();
                int lineCount = 8;
                if(tmp.getPrice()==0)lineCount--;
                itemStat.setBounds(tmp.getX() + tmp.getWidth(),tmp.getY(),verticalGapSize-tmp.getWidth()/2,/*horizontalGapSize-Math.round(tmp.getHeight()*0.8f)*/ fontSize* lineCount + lineCount*4 );
                this.itemStatLabels.add(itemStat);
            }
            if(item.getName().equals("potion"))
            {

                Potion tmp = (Potion)item;
                JLabel itemStat;
                if(tmp.getGrantExp()==0)
                {
                    itemStat = new JLabel(
                            "Name:" + tmp.getName() + "\n" +
                                    "Restores " + tmp.getHealthRestore() + " health." + "\n" +
                                    "Price: " + tmp.getPrice()
                            , null, SwingConstants.LEFT);
                    itemStat.setText(
                            "<html><body>" +
                                    "Name: " + tmp.getName() +"<br>"+
                                    "Restores " +tmp.getHealthRestore() +" health."+"<br>"+
                                    "Price: " + tmp.getPrice() +
                                    "</body></html>"

                    );
                }
                else
                {
                    itemStat = new JLabel(
                            "Name:" + tmp.getName() + "\n" +
                                    "Grants " + tmp.getGrantExp() + "experience." + "\n" +
                                    "Price: " + tmp.getPrice()
                            , null, SwingConstants.LEFT);
                    itemStat.setText(
                            "<html><body>" +
                                    "Name: " + tmp.getName() +"<br>"+
                                    "Grants " +tmp.getGrantExp()+ " experience." +"<br>"+
                                    "Price: " + tmp.getPrice() +
                                    "</body></html>"

                    );
                }
                itemStat.setBackground(bgColor);
                itemStat.setOpaque(true);
                int fontSize = itemStat.getFont().getSize();
                final int lineCount = 3;
                itemStat.setBounds(tmp.getX() + tmp.getWidth(),tmp.getY(),verticalGapSize-tmp.getWidth()/2,fontSize*lineCount +lineCount*4  );
                //height of the label is exactly as big as the text filling it, at most 8 pixels taller
                this.itemStatLabels.add(itemStat);
            }
        }
    }

    private void setItemPositions(boolean forShop)
    {


        //items will be displayed in 2 rows
        //the ugly formulas for the x coordinates ensure an even spread
        //within the middle 80% of the room, for both rows
        //works for rows that contains 1, 2 or 3 items
        if(forShop)
        {
            int half = (int)(Math.ceil(this.items.size()*0.5f));
            for (int i = 0; i < half; ++i) {
                this.items.get(i).setX(
                        Math.round(window_h  /* *0.8f */ * (i + 1.0f) / (half + 1.0f)) /*+ Math.round(window_h   *0.1f)*/- Math.round(items.get(i).getWidth()/2.f)
                );
                this.items.get(i).setY(Math.round(window_w  /* * 0.8f  */ *0.33f) - Math.round(items.get(i).getHeight()/2.f));
            }
            for (int i = half; i < this.items.size(); ++i) {
                this.items.get(i).setX(
                        Math.round(window_h /* * 0.8f*/ * ((i - half) + 1.0f) / ((this.items.size() - half) + 1.0f)) + /*Math.round(window_h * 0.1f)*/ - Math.round(items.get(i).getWidth()/2.f)
                );
                this.items.get(i).setY(Math.round(window_w  /* * 0.8f*/ * 0.66f) - Math.round(items.get(i).getHeight()/2.f) );
            }
        }
        else
        {
            items.get(0).setX( Math.round((window_h /*  *0.8f */ )*0.33f)  /* + Math.round(window_h*0.1f) */ - Math.round(items.get(0).getWidth()/2.f) );
            items.get(0).setY(Math.round((window_w /* *0.8f */ )*0.5f) /* +Math.round(window_h*0.1f) */  - Math.round(items.get(0).getHeight()/2.f) );
            items.get(1).setX(Math.round((window_h /* *0.8f */ )*0.66f) /* + Math.round(window_h*0.1f) */ - Math.round(items.get(1).getWidth()/2.f) );
            items.get(1).setY(Math.round((window_w /* *0.8f */ )*0.5f) /* +Math.round(window_h*0.1f) */ - Math.round(items.get(1).getHeight()/2.f) );
        }
        System.out.println(items.get(0).getX());
        System.out.println(items.get(1).getX());
    }

    private void transition(int x, int y) {

        long difference = 1000000;
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(lastTransitionTime.toString());
            Date date2 = format.parse(LocalTime.now().toString());
            difference = date2.getTime() - date1.getTime();
            System.out.println(date1);
            System.out.println(date2);
            System.out.println(difference);
        } catch (Exception e) {
            difference = 1000000;
        }


        if (difference >= 200) {
            //System.out.println(x +" "+ y);
            if (x == 0) {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i - 1][currentRoomNode.getCoordinate().j];
                player.stepBackAfterLeveltransition(player.getX(), window_w / m * (m - 4));

            } else if (y == 0) {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i][currentRoomNode.getCoordinate().j - 1];
                player.stepBackAfterLeveltransition(window_h / n * (n - 2), player.getY());

            } else if (x == n - 1) {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i + 1][currentRoomNode.getCoordinate().j];
                player.stepBackAfterLeveltransition(player.getX(), window_w / m * 2);

            } else if (y == m - 1) {
                currentRoomNode = roomMatrix[currentRoomNode.getCoordinate().i][currentRoomNode.getCoordinate().j + 1];
                player.stepBackAfterLeveltransition(window_h / n * 2, player.getY());

            }

            if(currentRoomNode!=null)
            {
                if (currentRoomNode.getRoomType() == RoomType.SHOP)
                {

                    Shop temp = (Shop) currentRoomNode.getRoom(); // this casting doesn't work inline for some reason
                    items=new Vector<>();
                    for(Item item : temp.getItems())
                    {
                        if (item!=null)
                        {
                            items.add(item);
                        }
                    }

                    if(!currentRoomNode.getRoom().getVisited())
                    {
                        setItemPositions(true);
                        this.itemStatLabels = new Vector<>();

                        currentRoomNode.getRoom().setVisited(true);
                    }
                    generateItemStatLabels();


                }
                if (currentRoomNode.getRoomType() == RoomType.ITEMROOM)
                {
                    this.itemStatLabels=new Vector<>();
                    this.items = new Vector<Item>();

                    ItemRoom temp = (ItemRoom) currentRoomNode.getRoom();
                    if(temp.getStatItem()!=null) this.items.add(temp.getStatItem());
                    if(temp.getWeapon()!=null)  this.items.add(temp.getWeapon());


                    if(!currentRoomNode.getRoom().getVisited())
                    {
                        setItemPositions(false);

                        currentRoomNode.getRoom().setVisited(true);
                    }
                    generateItemStatLabels();

                }
                if(currentRoomNode.getRoomType()==RoomType.COMBATROOM||currentRoomNode.getRoomType()==RoomType.BOSSROOM||currentRoomNode.getRoomType()==RoomType.STARTROOM)
                    {
                        if(itemStatLabels!=null) {
                            for (JLabel label : itemStatLabels) {
                                this.remove(label);
                            }
                            this.itemStatLabels= new Vector<>();
                        }

                        this.items = null;
                        currentRoomNode.getRoom().setVisited(true);
                    }
                for (int i = 0; i < m; i++)
                {
                    for (int j = 0; j < n; j++)
                    {
                        Image actImage;
                        Tile type;
                        switch (currentRoomNode.getRoom().getLayout()[j][i])
                        {
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
                                actImage = itemDoorClosedTexture;
                                type = Tile.ITEMDOOR_CLOSED;
                                break;
                            case SHOPDOOR_OPEN:
                                actImage = shopDoorOpenTexture;
                                type = Tile.SHOPDOOR_OPEN;
                                break;
                            case SHOPDOOR_CLOSED:
                                actImage = shopDoorClosedTexture;
                                type = Tile.SHOPDOOR_CLOSED;
                                break;
                            case TRAPDOOR_CLOSED:
                                actImage = trapDoorClosedTexture;
                                type = Tile.TRAPDOOR_CLOSED;
                                break;
                            case TRAPDOOR_OPEN:
                                actImage = trapDoorOpenTexture;
                                type = Tile.TRAPDOOR_OPEN;
                                break;
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
            //level.printLevelWithPlayerPos(currentRoomNode);
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

    public Sprite[][] getTiles()
    {
        return tiles;
    }

    public void setTiles(Sprite[][] tiles)
    {
        this.tiles = tiles;
    }

    //Maguktól mozgó dolgokat kell ebben az osztályban kezelni, illetve ha a mozgó objecktek ütköznek valamivel, azt is itt.
    class NewFrameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            player.moveX();
            player.moveY();

            if(currentAttacks.size()>0)
            {
                for (Attack attack : currentAttacks)
                {
                    attack.cast();
                }
            }

            for (int i = 0; i < enemies.size(); i++)
            {
                ((Enemy) enemies.get(i)).move();
                ((Enemy) enemies.get(i)).behaviour(player.getX(),player.getY());

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
            for(int i = 0; i < currentAttacks.size(); i++)
            {
                if(currentAttacks.get(i).isEnded())
                {
                    currentAttacks.remove(currentAttacks.get(i));
                }
            }
        }
    }

    class collideTask extends TimerTask
    {
        public void run()
        {
            //System.out.println("Time's up!");
            collide_timer_down = true;
        }
    }

    class attackTask extends TimerTask
    {
        @Override
        public void run()
        {
           attack_timer_down=true;
        }
    }
}



