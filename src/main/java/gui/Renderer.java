package gui;

import entity.Enemy;
import entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Renderer extends JPanel {

    private final JFrame frame;
    private final int window_w;
    private final int window_h;
    //private Timer animationTimer;
    private final int FPS = 240;
    private final int player_width = 40;
    private final int player_height = 40;
    private final int enemy_width = 40;
    private final int enemy_height = 40;
    long last_time = System.nanoTime();
    int delta_time = 0;
    long time;
    private Timer newFrameTimer;
    //private Image background;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();



    public Renderer(int height, int width, JFrame frame) {
        super();
        this.window_h = height;
        this.window_w = width;
        this.frame = frame;

        handleInputs();

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
    }

    //Kezdő állapotban lévő elemenk létrehozása.
    public void init() {
        try {
            //player képeinek betöltése
            Image playerImages[] = getImages(300, 450, 100, 150,
                    4, 4, 100, 50, "player.png");
            player = new Player(450, 100, player_width, player_height, playerImages);
            Image enemyImage = new ImageIcon("src/main/resources/enemy.png").getImage();

                      ///lvl.getNumberOfRooms()
            for (int i = 0; i < 3; i++) {
                enemies.add( new Enemy(500+i, 200+i, enemy_width, enemy_height, enemyImage));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //egy képmátrix felbontására szolgáló függvény
    /*
    width - egy képrészlet szélessége
    height - egy képrészlet magassága
    width_margin - két képrészlet közti távolság egy sorban
    width_hieght - két képrészlet közti távolság egy sorban
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

        player.draw(grphcs);


        for (int i = 0; i < 3; i++) {
            enemies.get(i).draw(grphcs);
        }

    }


    //Maguktól mozgó dolgokat kell ebben az osztályban kezelni, illetve ha a mozgó objecktek ütköznek valamivel, azt is itt.
    class NewFrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            player.moveX();
            player.moveY();

            for (int i = 0; i < 3; i++) {
                if ((enemies.get(i).getX() < 0) || (enemies.get(i).getX() >= 900)
                        || (enemies.get(i).getY() < 0) || (enemies.get(i).getY() >= 600)

                ) {
                    if(enemies.get(i).getX()==0 || enemies.get(i).getX()==900){
                        enemies.get(i).moveBack();
                    }
                    if(enemies.get(i).getY()==0 || enemies.get(i).getY()==600){
                        enemies.get(i).moveBack();
                    }
                    enemies.get(i).move();
                    enemies.get(i).randDirection();
                } else {
                    enemies.get(i).move();
                }

                if(enemies.get(i).collides(player)){
                    player.setHealth(enemies.get(i).getDamage());
                }

            }




            //TODO animilás
            /*animate(delta_time);
            time = System.nanoTime();
            delta_time = (int) ((time - last_time) / 1000000);
            last_time = time;
             */


            repaint();
        }
    }
}

