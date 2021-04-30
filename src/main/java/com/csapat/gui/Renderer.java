package com.csapat.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
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
import com.csapat.rooms.BossRoom;
import com.csapat.rooms.CombatRoom;
import com.csapat.rooms.Room;


public class Renderer extends JPanel {
    Item selectedItem;

    boolean isAdded = false;
    private JFrame frame;
    private int window_w;
    private int window_h;
    private int tile_size = 30;
    private Timer newFrameTimer;
    //private Timer animationTimer;
    private final int FPS = 240;
    //private Image background;
    private Player player;
    private int player_width = 40;
    private int player_height = 40;
    long last_time = System.nanoTime();
    int delta_time = 0;
    long time;
    private Image attackImg;
    private Image enemyAttackImg = null;
    Vector<Item> items;
    Vector<Attack> currentAttacks = new Vector<Attack>();
    Graphics grphcs;
    JLabel purchaseHint;

    private Vector<JLabel> itemStatLabels;

    private int tileHeight;
    private int tileWidth;
    private Level level;


    private Sprite tiles[][];
    private Vector<Sprite> tilesVector;
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
    private Image playerAttackUp;
    private Image playerAttackDown;
    private Image playerAttackLeft;
    private Image playerAttackRight;
    private Image playerAttackUpRight;
    private Image playerAttackUpLeft;
    private Image playerAttackDownLeft;
    private Image playerAttackDownRight;
    private Image enemyAttackUp;
    private Image enemyAttackDown;
    private Image enemyAttackLeft;
    private Image enemyAttackRight;
    private Image[] playerImages;

    private Vector<Enemy> enemies = new Vector<>();
    // ebbe töltődnek majd be az enemy-k a szoba/level betöltésénél.


    private RoomNode[][] roomMatrix;
    private RoomNode currentRoomNode;
    private LocalTime lastTransitionTime = LocalTime.now();

    private Sprite hearthSprite;

    private Boolean collide_timer_down = true;
    private java.util.Timer collide_with_enemy;

    private Boolean attack_timer_down = true;
    private java.util.Timer attack_timer;
    private Directions primaryPlayerAttackDirection;
    private Directions secondaryPlayerAttackDirection;
    private boolean isPrimaryPlayerAttackDirectionSet = false;
    private boolean isSecondaryPlayerAttackDirectionSet = false;
    private Vector<Image> rotatedImages;
    private Vector<Integer> rotationDegrees;
    private Vector<Attack> attacksWithRotatedImages;

    private Boolean moveTimeOut = true;
    private java.util.Timer enemyMoveTimer;


    public Renderer(int height, int width, JFrame frame) {
        super();
        this.window_h = height;
        this.window_w = width;
        this.frame = frame;

        purchaseHint = new JLabel();
        itemStatLabels = new Vector<>();
        items = new Vector<>();

        rotatedImages = new Vector<>();
        rotationDegrees = new Vector<>();
        attacksWithRotatedImages = new Vector<>();

        tilesVector = new Vector<>();

        handleInputs();
        this.level = new Level(levelDepth);
        this.n = level.getStartingRoom().getRoom().getN();  //20magas - sorok száma
        this.m = level.getStartingRoom().getRoom().getM();//30széles - oszlopok száma
        roomMatrix = level.getRoomMatrix();


        this.window_h = tile_size * this.m;
        this.window_w = tile_size * this.n;


        tileHeight = tile_size;
        tileWidth = tile_size;
        player_width = tileWidth;
        player_height = tileHeight;
        this.tiles = new Sprite[this.n][this.m];

        currentRoomNode = level.getStartingRoom();
        initGraphics();

        player = new Player(450, 100, player_height, player_width, playerImages, this.window_h, this.window_w);
        /*for (int i = 0; i < levelDepth + 2; ++i)
        {
            enemies.add(new Enemy(200 + 50 * i, 400 + 50 * i, 50, 50, enemyTexture,10,10,10));
        }*/

        initTiles();

        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();

    }

    public void newLevel() {
        newFrameTimer.stop();
        //timer is stopped here, restarted later, to avoid any weird behaviour while a new floor is generated and loaded
        this.level = new Level(++levelDepth);
        //the rest of the things set in the constructor are still valid here

        this.player.setX(n / 2 * tileHeight);
        this.player.setY(m / 2 * tileWidth);
        currentRoomNode = level.getStartingRoom();
        roomMatrix = level.getRoomMatrix();
        //player starts in the middle of the room
        initTiles();
        // calling init() is a temporary solution, as that also resets textures, realistically i only want the starting room's textures and layout
        repaint();
        newFrameTimer.start();
    }

    public void handleInputs() {
        //input kezelések
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "pressed w");
        this.getActionMap().put("pressed w", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelX() == 0) {
                    player.setVelY(-player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "released w");
        this.getActionMap().put("released w", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelY(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "pressed a");
        this.getActionMap().put("pressed a", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelY() == 0) {
                    player.setVelX(-player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "released a");
        this.getActionMap().put("released a", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelX(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "pressed s");
        this.getActionMap().put("pressed s", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelX() == 0) {
                    player.setVelY(player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "released s");
        this.getActionMap().put("released s", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelY(0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "pressed d");
        this.getActionMap().put("pressed d", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (player.getVelY() == 0) {
                    player.setVelX(player.getMoveSpeed());
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "released d");
        this.getActionMap().put("released d", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.setVelX(0);
            }
        });


        /*this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "pressed space");
        this.getActionMap().put("pressed space", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(attack_timer_down)
                {
                    attack_timer_down = false;
                    setPlayerAttackImg(player.getDirection());
                    Attack attack = new Attack(player.getX(), player.getY(), 25,player.getRange(), attackImg , player, enemies, player.getDirection(), player.getRange(), (int)player.getDamage());
                    currentAttacks.add(attack);
                    attack_timer = new java.util.Timer();
                    attack_timer.schedule(new attackTask(), (int)(1000/player.getAttackSpeed()));
                }
            }
        });*/


        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, false), "pressed b");
        this.getActionMap().put("pressed b", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (selectedItem != null)
                    if (player.getMoney() >= selectedItem.getPrice()) {
                        if (selectedItem instanceof Weapon) {
                            Item tmp = player.dropCurrentWeapon();
                            if (tmp != null && !tmp.getName().equals("starting weapon")) {
                                tmp.setPrice(0);
                                if (!overTheEdge(tmp.getX(), tmp.getY(), tmp.getWidth(), tmp.getHeight())) {
                                    tmp.setX(safeSetX(player.getX() + tmp.getWidth(), tmp.getWidth()));
                                    tmp.setY(safeSetY(player.getY() + tmp.getHeight(), tmp.getHeight()));
                                } else {
                                    tmp.setX(player.getX() - tmp.getWidth());
                                    tmp.setY(player.getY() - tmp.getHeight());
                                }
                                items.add(tmp);
                                currentRoomNode.getRoom().getDroppedItems().add(tmp);
                                generateItemStatLabels();
                                addLabels();
                            }
                        }
                        player.buyItem(selectedItem);
                        remove(itemStatLabels.get(items.indexOf(selectedItem)));
                        itemStatLabels.remove(itemStatLabels.get(items.indexOf(selectedItem)));
                        items.remove(selectedItem);
                        if (currentRoomNode.getRoomType() == RoomType.SHOP) {
                            ((Shop) currentRoomNode.getRoom()).removeItem(selectedItem);
                        }
                        if (currentRoomNode.getRoomType() == RoomType.ITEMROOM) {
                            ((ItemRoom) currentRoomNode.getRoom()).removeItem(selectedItem);
                        }
                        if (currentRoomNode.getRoomType() == RoomType.COMBATROOM) {
                            ((CombatRoom) currentRoomNode.getRoom()).removeItem(selectedItem);
                        }
                        if (currentRoomNode.getRoomType() == RoomType.BOSSROOM) {
                            ((BossRoom) currentRoomNode.getRoom()).removeItem(selectedItem);
                        }

                    }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "pressed q");
        this.getActionMap().put("pressed q", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player.useHealthPotion();

            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, false), "pressed k");
        this.getActionMap().put("pressed k", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (currentRoomNode != null && enemies != null) {
                    for (Enemy enemy : enemies) {
                        enemy.takeDamage(99999);
                    }
                }

            }
        });

        //plan 8 directional attacks, set by the arrow cluster
        //2 Directions store the directions,

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isPrimaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Right) {
                    primaryPlayerAttackDirection = Directions.Left;
                    isPrimaryPlayerAttackDirectionSet = true;
                } else if (!isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Left && primaryPlayerAttackDirection != Directions.Right && primaryPlayerAttackDirection != Directions.Left) {
                    secondaryPlayerAttackDirection = Directions.Left;
                    isSecondaryPlayerAttackDirectionSet = true;
                }
                if (primaryPlayerAttackDirection == Directions.Left && isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection == Directions.Left) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "released left");
        this.getActionMap().put("released left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (primaryPlayerAttackDirection == Directions.Left) {
                    primaryPlayerAttackDirection = null;
                    isPrimaryPlayerAttackDirectionSet = false;
                }
                if (secondaryPlayerAttackDirection == Directions.Left) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });


        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isPrimaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Down) {
                    primaryPlayerAttackDirection = Directions.Up;
                    isPrimaryPlayerAttackDirectionSet = true;
                } else if (!isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Up && primaryPlayerAttackDirection != Directions.Down && primaryPlayerAttackDirection != Directions.Up) {
                    secondaryPlayerAttackDirection = Directions.Up;
                    isSecondaryPlayerAttackDirectionSet = true;
                }
                if (primaryPlayerAttackDirection == Directions.Up && isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection == Directions.Up) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "released up");
        this.getActionMap().put("released up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (primaryPlayerAttackDirection == Directions.Up) {
                    primaryPlayerAttackDirection = null;
                    isPrimaryPlayerAttackDirectionSet = false;
                }
                if (secondaryPlayerAttackDirection == Directions.Up) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });


        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isPrimaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Up) {
                    primaryPlayerAttackDirection = Directions.Down;
                    isPrimaryPlayerAttackDirectionSet = true;
                } else if (!isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Down && primaryPlayerAttackDirection != Directions.Up && primaryPlayerAttackDirection != Directions.Down) {
                    secondaryPlayerAttackDirection = Directions.Down;
                    isSecondaryPlayerAttackDirectionSet = true;
                }
                if (primaryPlayerAttackDirection == Directions.Down && isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection == Directions.Down) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "released down");
        this.getActionMap().put("released down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (primaryPlayerAttackDirection == Directions.Down) {
                    primaryPlayerAttackDirection = null;
                    isPrimaryPlayerAttackDirectionSet = false;
                }
                if (secondaryPlayerAttackDirection == Directions.Down) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });


        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isPrimaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Left) {
                    primaryPlayerAttackDirection = Directions.Right;
                    isPrimaryPlayerAttackDirectionSet = true;
                } else if (!isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection != Directions.Right && primaryPlayerAttackDirection != Directions.Left && primaryPlayerAttackDirection != Directions.Right) {
                    secondaryPlayerAttackDirection = Directions.Right;
                    isSecondaryPlayerAttackDirectionSet = true;
                }
                if (primaryPlayerAttackDirection == Directions.Right && isSecondaryPlayerAttackDirectionSet && secondaryPlayerAttackDirection == Directions.Right) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "released right");
        this.getActionMap().put("released right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (primaryPlayerAttackDirection == Directions.Right) {
                    primaryPlayerAttackDirection = null;
                    isPrimaryPlayerAttackDirectionSet = false;
                }
                if (secondaryPlayerAttackDirection == Directions.Right) {
                    secondaryPlayerAttackDirection = null;
                    isSecondaryPlayerAttackDirectionSet = false;
                }
            }
        });
    }

    /**
     * Returns true if an element in originX,originY extended by addedX or addedY would be off-screen
     *
     * @param originX
     * @param originY
     * @param addedX
     * @param addedY
     * @return
     */
    public boolean overTheEdge(int originX, int originY, int addedX, int addedY) {
        return originX + addedX > window_h - tileWidth || originY + addedY > window_w - tileHeight;
    }

    /**
     * Creates a new attack based attack directions set by listeners for the arrows
     * if no direction is set, it returns null
     *
     * @return
     */
    private Attack createPlayerAttack() {
        if (isPrimaryPlayerAttackDirectionSet) {
            if (isSecondaryPlayerAttackDirectionSet) {
                //int x, int y, int width, int height, Image image, Sprite source, Vector target, Directions primary, Directions secondary, int range, int damage
                setPlayerAttackImg(primaryPlayerAttackDirection, secondaryPlayerAttackDirection);
                return new Attack(0, 0, 35, player.getRange(), null, player, enemies, primaryPlayerAttackDirection, secondaryPlayerAttackDirection, player.getRange(), (int) player.getDamage());
            }
            setPlayerAttackImgPrimary(primaryPlayerAttackDirection);
            return new Attack(0, 0, 35, player.getRange(), attackImg, player, enemies, primaryPlayerAttackDirection, player.getRange(), (int) player.getDamage());

        }
        return null;

    }

    /**
     * Adds labels currently in itemStatLabels to this, if they aren't already in
     */
    public void addLabels() {
        if (itemStatLabels != null) {
            for (JLabel label : itemStatLabels) {
                if (label.getParent() == null) {
                    add(label);
                }
            }
        }
    }

    /**
     * Functions safeSetX and safeSetY
     *
     * @param posX
     * @param width
     * @return posX(Y) or the closest value to it that is in-bounds
     */
    public int safeSetX(int posX, int width) {
        if (posX > window_h - tileWidth - width) return window_h - tileWidth - width;
        if (posX < tileWidth + width) return tileWidth + width;
        return posX;
    }

    public int safeSetY(int posY, int height) {
        if (posY > window_w - tileWidth - height) return window_w - tileHeight - height;
        if (posY < tileWidth + height) return tileHeight + height;
        return posY;
    }

    /**
     * reads the textures used in the game
     */
    public void initGraphics() {
        try {
            //player képeinek betöltése
            playerImages = getImages(300, 450, 100, 150,
                    4, 4, 100, 50, "player.png");

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
            trapDoorClosedTexture = ImageIO.read(this.getClass().getClassLoader().getResource("trapdoor_closed.png"));
            hearthTexture = ImageIO.read(this.getClass().getClassLoader().getResource("hearth.png"));
            playerAttackUp = ImageIO.read(this.getClass().getClassLoader().getResource("attackUp.png"));
            playerAttackDown = ImageIO.read(this.getClass().getClassLoader().getResource("attackDown.png"));
            playerAttackLeft = ImageIO.read(this.getClass().getClassLoader().getResource("attackLeft.png"));
            playerAttackRight = ImageIO.read(this.getClass().getClassLoader().getResource("attackRight.png"));
            playerAttackUpRight = ImageIO.read(this.getClass().getClassLoader().getResource("attackUpRight.png"));
            playerAttackUpLeft = ImageIO.read(this.getClass().getClassLoader().getResource("attackUpLeft.png"));
            playerAttackDownLeft = ImageIO.read(this.getClass().getClassLoader().getResource("attackDownLeft.png"));
            playerAttackDownRight = ImageIO.read(this.getClass().getClassLoader().getResource("attackDownRight.png"));
            enemyAttackUp = ImageIO.read(this.getClass().getClassLoader().getResource("enemyAttackUp.png"));
            enemyAttackDown = ImageIO.read(this.getClass().getClassLoader().getResource("enemyAttackDown.png"));
            enemyAttackLeft = ImageIO.read(this.getClass().getClassLoader().getResource("enemyAttackLeft.png"));
            enemyAttackRight = ImageIO.read(this.getClass().getClassLoader().getResource("enemyAttackRight.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }


        hearthSprite = new Sprite(window_w + 180, 7, 45, 40, hearthTexture);
        currentRoomNode.getRoom().printRoom();
        level.printLevel();


    }

    /**
     * sets tiles[][], which is then used to render a room
     * this should be called every time rooms are changed!
     */
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
                Sprite act = new Sprite(i * tileHeight, j * tileWidth, tileWidth, tileHeight, actImage);
                act.setType(type);
                tiles[j][i] = act;
            }
        }
        tilesVector.removeAllElements();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i == 0 || j == 0 || i == m - 1 || j == n - 1 || tiles[j][i].getType() == Tile.TRAPDOOR_OPEN || tiles[j][i].getType() == Tile.TRAPDOOR_CLOSED) {
                    tilesVector.add(tiles[j][i]);
                }
            }
        }


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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
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
    protected void paintComponent(Graphics grphcs) {
        grphcs.setColor(Color.darkGray);
        super.paintComponent(grphcs);
        grphcs.fillRect(0, 0, 900, 600);
        drawRoom(grphcs);

        if (items != null) {
            for (Item it : items) {
                it.draw(grphcs);
            }
        }
        if (enemies != null) {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).draw(grphcs);
            }
        }
        for (Attack att : currentAttacks) {
            att.draw(grphcs);
            att.decreaseDuration();
        }
        if (rotatedImages.size() > 0)
            for (Image rotated : rotatedImages) {
                int idx = rotatedImages.indexOf(rotated);
                Graphics2D g2 = (Graphics2D) grphcs;
                AffineTransform af = AffineTransform.getTranslateInstance(attacksWithRotatedImages.get(idx).getX(), attacksWithRotatedImages.get(idx).getY());
                af.rotate(Math.toRadians(rotationDegrees.get(idx)));
                af.scale(1f, player.getRange() / 35.f);
                g2.drawImage(rotated, af, null);
            }
        if (attacksWithRotatedImages != null && attacksWithRotatedImages.size() > 0) {
            Vector<Attack> rotatedCopy = new Vector(attacksWithRotatedImages);
            for (Attack at : attacksWithRotatedImages) {
                if (at.getDuration() <= 0) {
                    int idx = attacksWithRotatedImages.indexOf(at);
                    rotatedImages.remove(idx);
                    rotationDegrees.remove(idx);
                    rotatedCopy.remove(idx);
                }
            }

            attacksWithRotatedImages = rotatedCopy;
        }
        //for()


        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        Font font = new Font("SansSerif", Font.BOLD, 40);
        g2.setFont(font);
        hearthSprite.draw(grphcs);
        g2.drawString(Integer.toString(+player.getHealth()), window_w + 230, 40);
        player.draw(grphcs);

        collide();

    }


    public void collide() {
        for (Sprite tile : tilesVector) {
            if (tile.collides(player)) {
                if (tile.getType() == Tile.WALL) {
                    player.stepBack();
                    break;
                }
                if (tile.getType() == Tile.DOOR_OPEN) {
                    transition(tile);
                    break;
                }
                if (tile.getType() == Tile.ITEMDOOR_OPEN) {
                    transition(tile);
                    break;
                }
                if (tile.getType() == Tile.SHOPDOOR_OPEN) {
                    transition(tile);
                    break;
                }
                if (tile.getType() == Tile.BOSSDOOR_OPEN) {
                    transition(tile);
                    break;
                }
                if ((tile.getType() == Tile.DOOR_CLOSED)) {
                    player.stepBack();
                    break;
                }
                if ((tile.getType() == Tile.BOSSDOOR_CLOSED)) {
                    player.stepBack();
                    break;
                }
                if ((tile.getType() == Tile.ITEMDOOR_CLOSED)) {
                    player.stepBack();
                    break;
                }
                if ((tile.getType() == Tile.SHOPDOOR_CLOSED)) {
                    player.stepBack();
                    break;
                }
                if ((tile.getType() == Tile.TRAPDOOR_OPEN)) {
                    newLevel();
                    break;
                }
            }
            if (enemies != null) {
                for (int k = 0; k < enemies.size(); k++) {

                    if (tile.collides(enemies.get(k))) {
                        if (tile.getType() == Tile.WALL) {
                            enemies.get(k).moveBack();
                            enemies.get(k).randDirection();
                        }
                        if (tile.getType() == Tile.DOOR_OPEN) {
                            enemies.get(k).moveBack();
                        }
                        if (tile.getType() == Tile.ITEMDOOR_OPEN) {
                            enemies.get(k).moveBack();
                        }
                        if (tile.getType() == Tile.SHOPDOOR_OPEN) {

                            enemies.get(k).moveBack();
                        }
                        if (tile.getType() == Tile.BOSSDOOR_OPEN) {
                            enemies.get(k).moveBack();
                        }
                        if ((tile.getType() == Tile.DOOR_CLOSED)) {
                            enemies.get(k).moveBack();
                        }
                        if ((tile.getType() == Tile.BOSSDOOR_CLOSED)) {
                            enemies.get(k).moveBack();
                        }
                        if ((tile.getType() == Tile.ITEMDOOR_CLOSED)) {
                            enemies.get(k).moveBack();
                        }
                        if ((tile.getType() == Tile.SHOPDOOR_CLOSED)) {
                            enemies.get(k).moveBack();
                        }
                        if ((tile.getType() == Tile.TRAPDOOR_OPEN)) {
                            enemies.get(k).moveBack();

                        }

                    }
                }
            }
        }
        boolean didCollideWithItem = false;
        if (items != null) {
            for (Item item : items) {
                if (item.collides(player)) {
                    didCollideWithItem = didCollideWithItem || item.collides(player);
                    selectedItem = item;
                    if (!isAdded) {
                        purchaseHint = new JLabel();
                        purchaseHint.setText("<html><body>If you want to buy an item<br> press <b>b</b> while on top of it!</body></html>");
                        purchaseHint.setBounds(tileWidth, tileHeight, purchaseHint.getFont().getSize() * 15, purchaseHint.getFont().getSize() * 4);
                        purchaseHint.setBackground(new Color(200, 200, 200));
                        purchaseHint.setOpaque(true);
                        this.add(purchaseHint);
                        isAdded = true;
                    }

                }
            }
            if (!didCollideWithItem) {
                selectedItem = null;
                if (purchaseHint != null) remove(purchaseHint);

                isAdded = false;
            }
        } else {
            selectedItem = null;
            if (purchaseHint != null) remove(purchaseHint);
            isAdded = false;
        }
        for (Attack attack : currentAttacks) {
            /*
            if(enemies!=null)
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
            }*/

            if (enemies != null) {
                Vector<Enemy> enemiesCopy = new Vector<>(enemies);
                for (Enemy enemy : enemies) {
                    if (enemy.collides(attack) && !enemy.getGotAttacked() && attack.getSource() == player) {
                        enemy.setGotAttacked(true);
                        enemy.damaged(attack.getDamage(), player.getAttackSpeed());
                    }
                }
                enemies = enemiesCopy;
            }

            if (player.collides(attack) && attack.getSource() != player) {
                player.takeDamage(attack.getDamage());
            }
        }
        if (enemies != null)
            for (Enemy enemy : enemies) {
                for (Enemy enemy2 : enemies) {
                    if (enemy.collides(enemy2) && enemy != enemy2) {
                        enemy.moveBack();
                    }
                }
                if (enemy.collides(player)) {
                    enemy.moveBack();
                }
            }
    }

    private void generateItemStatLabels() {
        if (itemStatLabels.size() < items.size()) {
            int verticalGapSize = Math.round(window_w * 0.33f);
            for (int i = itemStatLabels.size(); i < items.size(); ++i) {
                Color bgColor = new Color(200, 200, 200);
                if (items.get(i).getItemType() == ItemType.WEAPON) {
                    Weapon tmp = (Weapon) items.get(i);
                    JLabel itemStat = new JLabel(
                            "Name: " + tmp.getName() + "\n" +
                                    "Rangemod: " + tmp.getRangeModifier() + "%" + "\n" +
                                    "Damagemod: " + tmp.getDamageModifier() + "%" + "\n" +
                                    "AttSpdmod: " + tmp.getAttackSpeedModifier() + "%" + "\n" +
                                    "Price: " + tmp.getPrice()
                            , null, SwingConstants.LEFT);
                    if (tmp.getPrice() != 0) {
                        itemStat.setText(
                                "<html><body>" +
                                        "Name: " + tmp.getName() + "<br>" +
                                        "Range: +" + tmp.getRangeModifier() + "<br>" +
                                        "Damage: +" + tmp.getDamageModifier() + "<br>" +
                                        "Attackspeed: +" + tmp.getAttackSpeedModifier() * 100 + "%" + "<br>" +
                                        "Price: " + tmp.getPrice() +
                                        "</body></html>");
                    } else {
                        itemStat.setText(
                                "<html><body>" +
                                        "Name: " + tmp.getName() + "<br>" +
                                        "Range: +" + tmp.getRangeModifier() + "<br>" +
                                        "Damage: +" + tmp.getDamageModifier() + "<br>" +
                                        "Attackspeed: +" + tmp.getAttackSpeedModifier() * 100 + "%" + "<br>" +
                                        "</body></html>");
                    }
                    itemStat.setBackground(bgColor);
                    itemStat.setOpaque(true);
                    int fontSize = itemStat.getFont().getSize();
                    int lineCount = 5;
                    if (tmp.getPrice() == 0) lineCount--;
                    if (!overTheEdge(tmp.getX(), tmp.getY(), verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4)) {
                        itemStat.setBounds(tmp.getX() + tmp.getWidth(), tmp.getY(), verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4);
                    } else {
                        itemStat.setBounds(tmp.getX() - verticalGapSize, tmp.getY() - fontSize * lineCount + lineCount * 4, verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4);
                    }
                    this.itemStatLabels.add(itemStat);
                }
                if (items.get(i).getItemType() == ItemType.STATITEM) {
                    StatItem tmp = (StatItem) items.get(i);
                    JLabel itemStat = new JLabel(
                            "Name:" + tmp.getName() + "\n" +
                                    "Health: +" + tmp.getHealthModifier() + "\n" +
                                    "Range: +" + tmp.getRangeModifier() + "%" + "\n" +
                                    "Attack speed +: " + tmp.getRangeModifier() + "%" + "\n" +
                                    "Damage +: " + tmp.getDamageModifier() + "%" + "\n" +
                                    "Speed +: " + tmp.getSpeedModifier() + "%" + "\n" +
                                    "Price: " + tmp.getPrice()
                            , null, SwingConstants.LEFT);
                    if (tmp.getPrice() != 0) {
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
                    } else {
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
                    if (tmp.getPrice() == 0) lineCount--;
                    if (!overTheEdge(tmp.getX(), tmp.getY(), verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4)) {
                        itemStat.setBounds(tmp.getX() + tmp.getWidth(), tmp.getY(), verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4);
                    } else {
                        itemStat.setBounds(tmp.getX() - verticalGapSize, tmp.getY() - fontSize * lineCount + lineCount * 4, verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4);
                    }
                    this.itemStatLabels.add(itemStat);
                }
                if (items.get(i).getItemType() == ItemType.POTION) {

                    Potion tmp = (Potion) items.get(i);
                    JLabel itemStat;
                    if (tmp.getGrantExp() == 0) {
                        if (tmp.getPrice() != 0) {
                            itemStat = new JLabel(
                                    "Name:" + tmp.getName() + "\n" +
                                            "Restores " + tmp.getHealthRestore() + " health." + "\n" +
                                            "Price: " + tmp.getPrice()
                                    , null, SwingConstants.LEFT);
                            itemStat.setText(
                                    "<html><body>" +
                                            "Name: " + tmp.getName() + "<br>" +
                                            "Restores " + tmp.getHealthRestore() + " health." + "<br>" +
                                            "Price: " + tmp.getPrice() +
                                            "</body></html>"

                            );
                        } else {
                            itemStat = new JLabel(
                                    "Name:" + tmp.getName() + "\n" +
                                            "Restores " + tmp.getHealthRestore() + " health." + "\n" +
                                            "Price: " + tmp.getPrice()
                                    , null, SwingConstants.LEFT);
                            itemStat.setText(
                                    "<html><body>" +
                                            "Name: " + tmp.getName() + "<br>" +
                                            "Restores " + tmp.getHealthRestore() + " health." + "<br>" +
                                            "</body></html>");
                        }
                    } else {
                        if (tmp.getPrice() != 0) {
                            itemStat = new JLabel(
                                    "Name:" + tmp.getName() + "\n" +
                                            "Grants " + tmp.getGrantExp() + "experience." + "\n" +
                                            "Price: " + tmp.getPrice()
                                    , null, SwingConstants.LEFT);
                            itemStat.setText(
                                    "<html><body>" +
                                            "Name: " + tmp.getName() + "<br>" +
                                            "Grants " + tmp.getGrantExp() + " experience." + "<br>" +
                                            "Price: " + tmp.getPrice() +
                                            "</body></html>"

                            );
                        } else {
                            itemStat = new JLabel(
                                    "Name:" + tmp.getName() + "\n" +
                                            "Restores " + tmp.getHealthRestore() + " health." + "\n" +
                                            "Price: " + tmp.getPrice()
                                    , null, SwingConstants.LEFT);
                            itemStat.setText(
                                    "<html><body>" +
                                            "Name: " + tmp.getName() + "<br>" +
                                            "Grants " + tmp.getGrantExp() + " experience." + "<br>" +
                                            "</body></html>");
                        }
                    }
                    itemStat.setBackground(bgColor);
                    itemStat.setOpaque(true);
                    int fontSize = itemStat.getFont().getSize();
                    final int lineCount = 3;
                    if (!overTheEdge(tmp.getX(), tmp.getY(), verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4)) {
                        itemStat.setBounds(tmp.getX() + tmp.getWidth(), tmp.getY(), verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4);
                    } else {
                        itemStat.setBounds(tmp.getX() - verticalGapSize, tmp.getY() - fontSize * lineCount + lineCount * 4, verticalGapSize - tmp.getWidth() / 2, fontSize * lineCount + lineCount * 4);
                    }
                    //height of the label is exactly as big as the text filling it, at most 8 pixels taller
                    this.itemStatLabels.add(itemStat);
                }
            }
        }
    }

    private void setItemPositions(boolean forShop) {


        //items will be displayed in 2 rows
        //the ugly formulas for the x coordinates ensure an even spread
        //within the middle 80% of the room, for both rows
        //works for rows that contains 1, 2 or 3 items
        if (forShop) {
            int half = (int) (Math.ceil(this.items.size() * 0.5f));
            for (int i = 0; i < half; ++i) {
                this.items.get(i).setX(
                        Math.round(window_h  /* *0.8f */ * (i + 1.0f) / (half + 1.0f)) /*+ Math.round(window_h   *0.1f)*/ - Math.round(items.get(i).getWidth() / 2.f)
                );
                this.items.get(i).setY(Math.round(window_w  /* * 0.8f  */ * 0.33f) - Math.round(items.get(i).getHeight() / 2.f));
            }
            for (int i = half; i < this.items.size(); ++i) {
                this.items.get(i).setX(
                        Math.round(window_h /* * 0.8f*/ * ((i - half) + 1.0f) / ((this.items.size() - half) + 1.0f)) + /*Math.round(window_h * 0.1f)*/ -Math.round(items.get(i).getWidth() / 2.f)
                );
                this.items.get(i).setY(Math.round(window_w  /* * 0.8f*/ * 0.66f) - Math.round(items.get(i).getHeight() / 2.f));
            }
        } else {
            items.get(0).setX(Math.round((window_h /*  *0.8f */) * 0.33f)  /* + Math.round(window_h*0.1f) */ - Math.round(items.get(0).getWidth() / 2.f));
            items.get(0).setY(Math.round((window_w /* *0.8f */) * 0.5f) /* +Math.round(window_h*0.1f) */ - Math.round(items.get(0).getHeight() / 2.f));
            items.get(1).setX(Math.round((window_h /* *0.8f */) * 0.66f) /* + Math.round(window_h*0.1f) */ - Math.round(items.get(1).getWidth() / 2.f));
            items.get(1).setY(Math.round((window_w /* *0.8f */) * 0.5f) /* +Math.round(window_h*0.1f) */ - Math.round(items.get(1).getHeight() / 2.f));
        }
    }


    private int[] getTileCoords(Sprite tile) {
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[j][i] == tile) {
                    int ret[] = new int[2];
                    ret[0] = i;
                    ret[1] = j;
                    return ret;
                }
            }
        }
        //this is bad practice, but if getTileCoords is called properly, this will never be reached
        return null;
    }

    private void transition(Sprite tile) {
        int[] coords = getTileCoords(tile);
        int x = coords[1];
        int y = coords[0];
        long difference = 1000000;
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(lastTransitionTime.toString());
            Date date2 = format.parse(LocalTime.now().toString());
            difference = date2.getTime() - date1.getTime();
        } catch (Exception e) {
            difference = 1000000;
        }


        if (difference >= 100) {
            newFrameTimer.stop();
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

            if (currentRoomNode != null) {
                if (!currentRoomNode.getRoom().getVisited()) enemies = currentRoomNode.getRoom().getEnemies();
                for (JLabel itemStatLabel : itemStatLabels) {
                    remove(itemStatLabel);
                }
                itemStatLabels.removeAllElements();
                items.removeAllElements();
                for (Item item : currentRoomNode.getRoom().getDroppedItems()) {
                    if (item != null) {
                        items.add(item);
                    }
                }
                if (currentRoomNode.getRoomType() == RoomType.SHOP) {
                    Shop temp = (Shop) currentRoomNode.getRoom(); // this casting doesn't work inline for some reason
                    for (Item item : temp.getItems()) {
                        if (item != null) {
                            items.add(item);
                        }
                    }

                    if (!currentRoomNode.getRoom().getVisited()) {
                        setItemPositions(true);
                        itemStatLabels.removeAllElements();
                        currentRoomNode.getRoom().setVisited(true);
                    }
                    generateItemStatLabels();
                    addLabels();


                }
                if (currentRoomNode.getRoomType() == RoomType.ITEMROOM) {
                    ItemRoom temp = (ItemRoom) currentRoomNode.getRoom();
                    if (temp.getStatItem() != null) this.items.add(temp.getStatItem());
                    if (temp.getWeapon() != null) this.items.add(temp.getWeapon());


                    if (!currentRoomNode.getRoom().getVisited()) {
                        setItemPositions(false);

                        currentRoomNode.getRoom().setVisited(true);
                    }
                    generateItemStatLabels();
                    addLabels();

                }
                if (currentRoomNode.getRoomType() == RoomType.COMBATROOM || currentRoomNode.getRoomType() == RoomType.BOSSROOM) {
                    if (!currentRoomNode.getRoom().getVisited()) {
                        changeDoors(currentRoomNode.getRoom());
                    }
                    generateItemStatLabels();
                    addLabels();
                }
                if (currentRoomNode.getRoomType() == RoomType.STARTROOM) {
                    currentRoomNode.getRoom().setVisited(true);
                }
                initTiles();

                lastTransitionTime = LocalTime.now();
            }


            //level.printLevelWithPlayerPos(currentRoomNode);

            newFrameTimer.start();
        }

    }

    public void drawRoom(Graphics grphcs) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tiles[i][j].draw(grphcs);
            }
        }
    }

    public Dimension getWindowSize() {
        Dimension dim = new Dimension(window_h, window_w);
        return dim;
    }

    public Sprite[][] getTiles() {
        return tiles;
    }

    public void setTiles(Sprite[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * Changes all doors of a room from closed to open or vice-versa, based on current state
     *
     * @param room
     */
    public void changeDoors(Room room) {

        Tile northernDoor = room.getLayout()[0][room.getM() / 2 - 1];
        Tile southernDoor = room.getLayout()[room.getN() - 1][room.getM() / 2 - 1];
        Tile easternDoor = room.getLayout()[room.getN() / 2 - 1][room.getM() - 1];
        Tile westernDoor = room.getLayout()[room.getN() / 2 - 1][0];

        switch (northernDoor) {
            case BOSSDOOR_CLOSED:
                northernDoor = Tile.BOSSDOOR_OPEN;
                break;
            case ITEMDOOR_CLOSED:
                northernDoor = Tile.ITEMDOOR_OPEN;
                break;
            case SHOPDOOR_CLOSED:
                northernDoor = Tile.SHOPDOOR_OPEN;
                break;
            case DOOR_CLOSED:
                northernDoor = Tile.DOOR_OPEN;
                break;
            case BOSSDOOR_OPEN:
                northernDoor = Tile.BOSSDOOR_CLOSED;
                break;
            case ITEMDOOR_OPEN:
                northernDoor = Tile.ITEMDOOR_CLOSED;
                break;
            case SHOPDOOR_OPEN:
                northernDoor = Tile.SHOPDOOR_CLOSED;
                break;
            case DOOR_OPEN:
                northernDoor = Tile.DOOR_CLOSED;
                break;
        }
        switch (southernDoor) {
            case BOSSDOOR_CLOSED:
                southernDoor = Tile.BOSSDOOR_OPEN;
                break;
            case ITEMDOOR_CLOSED:
                southernDoor = Tile.ITEMDOOR_OPEN;
                break;
            case SHOPDOOR_CLOSED:
                southernDoor = Tile.SHOPDOOR_OPEN;
                break;
            case DOOR_CLOSED:
                southernDoor = Tile.DOOR_OPEN;
                break;
            case BOSSDOOR_OPEN:
                southernDoor = Tile.BOSSDOOR_CLOSED;
                break;
            case ITEMDOOR_OPEN:
                southernDoor = Tile.ITEMDOOR_CLOSED;
                break;
            case SHOPDOOR_OPEN:
                southernDoor = Tile.SHOPDOOR_CLOSED;
                break;
            case DOOR_OPEN:
                southernDoor = Tile.DOOR_CLOSED;
                break;
        }
        switch (easternDoor) {
            case BOSSDOOR_CLOSED:
                easternDoor = Tile.BOSSDOOR_OPEN;
                break;
            case ITEMDOOR_CLOSED:
                easternDoor = Tile.ITEMDOOR_OPEN;
                break;
            case SHOPDOOR_CLOSED:
                easternDoor = Tile.SHOPDOOR_OPEN;
                break;
            case DOOR_CLOSED:
                easternDoor = Tile.DOOR_OPEN;
                break;
            case BOSSDOOR_OPEN:
                easternDoor = Tile.BOSSDOOR_CLOSED;
                break;
            case ITEMDOOR_OPEN:
                easternDoor = Tile.ITEMDOOR_CLOSED;
                break;
            case SHOPDOOR_OPEN:
                easternDoor = Tile.SHOPDOOR_CLOSED;
                break;
            case DOOR_OPEN:
                easternDoor = Tile.DOOR_CLOSED;
                break;
        }
        switch (westernDoor) {
            case BOSSDOOR_CLOSED:
                westernDoor = Tile.BOSSDOOR_OPEN;
                break;
            case ITEMDOOR_CLOSED:
                westernDoor = Tile.ITEMDOOR_OPEN;
                break;
            case SHOPDOOR_CLOSED:
                westernDoor = Tile.SHOPDOOR_OPEN;
                break;
            case DOOR_CLOSED:
                westernDoor = Tile.DOOR_OPEN;
                break;
            case BOSSDOOR_OPEN:
                westernDoor = Tile.BOSSDOOR_CLOSED;
                break;
            case ITEMDOOR_OPEN:
                westernDoor = Tile.ITEMDOOR_CLOSED;
                break;
            case SHOPDOOR_OPEN:
                westernDoor = Tile.SHOPDOOR_CLOSED;
                break;
            case DOOR_OPEN:
                westernDoor = Tile.DOOR_CLOSED;
                break;
        }

        Tile[][] roomLayout = room.getLayout();
        roomLayout[0][room.getM() / 2 - 1] = northernDoor;
        roomLayout[room.getN() - 1][room.getM() / 2 - 1] = southernDoor;
        roomLayout[room.getN() / 2 - 1][room.getM() - 1] = easternDoor;
        roomLayout[room.getN() / 2 - 1][0] = westernDoor;
        room.setLayout(roomLayout);
        currentRoomNode.getRoom().setLayout(roomLayout);
        initTiles();
    }

    //Maguktól mozgó dolgokat kell ebben az osztályban kezelni, illetve ha a mozgó objecktek ütköznek valamivel, azt is itt.

    /**
     * Sets the player attack texture when only a primary direction is given
     *
     * @param d
     */
    private void setPlayerAttackImgPrimary(Directions d) {
        switch (d) {
            case Up:
                attackImg = playerAttackUp;
                break;
            case Down:
                attackImg = playerAttackDown;
                break;
            case Left:
                attackImg = playerAttackLeft;
                break;
            case Right:
                attackImg = playerAttackRight;
                break;
        }
    }

    /**
     * Sets the player attack image when both primary and secondary directions are given
     * be careful to only call this with sensible primary and secondary combinations
     *
     * @param primary
     * @param secondary
     */
    private void setPlayerAttackImg(Directions primary, Directions secondary) {
        attackImg = null;
        switch (primary) {
            case Up:
                if (secondary == Directions.Left) {
                    rotatedImages.add(playerAttackUp);
                    rotationDegrees.add(315);
                }
                if (secondary == Directions.Right) {
                    rotatedImages.add(playerAttackUp);
                    rotationDegrees.add(45);
                }
                break;
            case Down:
                if (secondary == Directions.Left) {
                    rotatedImages.add(playerAttackDown);
                    rotationDegrees.add(45);
                }
                if (secondary == Directions.Right) {
                    rotatedImages.add(playerAttackDown);
                    rotationDegrees.add(315);
                }
                break;
            case Left:
                if (secondary == Directions.Up) {
                    rotatedImages.add(playerAttackUp);
                    rotationDegrees.add(315);
                }
                if (secondary == Directions.Down) {
                    rotatedImages.add(playerAttackDown);
                    rotationDegrees.add(45);
                }
                break;
            case Right:
                if (secondary == Directions.Up) {
                    rotatedImages.add(playerAttackUp);
                    rotationDegrees.add(45);
                }
                if (secondary == Directions.Down) {
                    rotatedImages.add(playerAttackDown);
                    rotationDegrees.add(315);
                }
                break;
        }
    }

    private void setEnemyAttackImg(Directions d) {
        switch (d) {
            case Up:
                enemyAttackImg = enemyAttackUp;
                break;
            case Down:
                enemyAttackImg = enemyAttackDown;
                break;
            case Left:
                enemyAttackImg = enemyAttackLeft;
                break;
            case Right:
                enemyAttackImg = enemyAttackRight;
                break;
        }
    }


    class NewFrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (player.isDead()) {
                //mourn
            }
            player.moveX();
            player.moveY();

            if (attack_timer_down) {
                Attack playerAttack = createPlayerAttack();
                if (playerAttack != null) {
                    attack_timer_down = false;
                    //player attack return null if no primary direction is present, and attackImg is set then
                    //therefore attackImg is never null when used here
                    if (isPrimaryPlayerAttackDirectionSet && isSecondaryPlayerAttackDirectionSet) {
                        attacksWithRotatedImages.add(playerAttack);
                    } else {
                        playerAttack.setImage(attackImg);
                    }
                    currentAttacks.add(playerAttack);
                    attack_timer = new java.util.Timer();
                    attack_timer.schedule(new attackTask(), (int) (1000 / player.getAttackSpeed()));
                }
            }

            /*
            if(currentAttacks.size()>0)
            {
                for (Attack attack : currentAttacks)
                {
                    attack.cast();
                }
            }*/
            if (enemies != null) {
                Vector<Enemy> enemiesCopy = new Vector<Enemy>(enemies);
                for (Enemy enemy : enemies) {
                    if (enemy.getHealthPoints() == 0) {
                        Item loot = enemy.dropLoot(player);
                        if (loot != null) {
                            loot.setX(enemy.getX());
                            loot.setY(enemy.getY());
                            currentRoomNode.getRoom().getDroppedItems().add(loot);
                            items.add(loot);

                        }

                        enemiesCopy.remove(enemy);
                    } else {
                        Attack att = enemy.behaviour(player);

                        if (att != null) {
                            setEnemyAttackImg(enemy.getDirection());
                            att.setImage(enemyAttackImg);
                            currentAttacks.add(att);
                        }

                    }
                }
                generateItemStatLabels();
                addLabels();
                enemies = enemiesCopy;
                if (enemies.size() == 0) {
                    enemies = null;
                }
            } else {
                if (currentRoomNode.getRoomType() == RoomType.COMBATROOM || currentRoomNode.getRoomType() == RoomType.BOSSROOM) {
                    if (!currentRoomNode.getRoom().getVisited()) {
                        currentRoomNode.getRoom().setVisited(true);
                        changeDoors(currentRoomNode.getRoom());
                    }
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

        void clearAttacks() {
            for (int i = 0; i < currentAttacks.size(); i++) {
                if (currentAttacks.get(i).getDuration() <= 0) {
                    currentAttacks.remove(currentAttacks.get(i));
                }
               /* if(currentAttacks.get(i).isEnded())
                {
                    currentAttacks.remove(currentAttacks.get(i));
                }*/
            }
        }
    }

    class collideTask extends TimerTask {
        public void run() {
            collide_timer_down = true;
        }
    }

    class attackTask extends TimerTask {
        @Override
        public void run() {
            attack_timer_down = true;
        }
    }

    class enemyMoveTask extends TimerTask {

        @Override
        public void run() {
            moveTimeOut = true;
        }
    }
}



