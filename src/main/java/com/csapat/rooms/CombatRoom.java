package com.csapat.rooms;

import com.csapat.entity.Enemy;
import com.csapat.entity.Item;
import com.csapat.gui.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

/**
 * CombatRoom
 * contains 3-7 enemies, all of which have random stats that scale with levelDepth
 */
public class CombatRoom extends Room
{

    Random rand= new Random();
    private Image[] enemyImages;
    private int enemyCount;


    public CombatRoom(int levelDepth)
    {
        super(levelDepth);
        enemyCount=rand.nextInt(4)+3;
        enemies= new Vector<Enemy>();
        this.roomSpecificGen();
        this.levelDepth=levelDepth;
    }

    @Override
    public void roomSpecificGen()
    {
        Image enemyTexture=null;
        try
        {
            enemyImages = getImages(40,65,20,13,5,4,10,10,"enemys.png");
        }
        catch(Exception e){System.out.println("Enemy texture is missing");}
        for(int i=0;i<enemyCount;++i)
        {
            int healthPoints = rand.nextInt(20)+50 + (levelDepth-1)*15 ;
            float moveSpeed = 1.3f + (levelDepth-1)*0.1f ;
            int visionRange = rand.nextInt(25)+200 + (levelDepth-1)*50; //enemies have full vision of every room from depth12
            int attackRange = rand.nextInt(20) + 55 + (levelDepth-1)*20 ;
            int damage = Math.round(rand.nextInt(10)+20 +(levelDepth-1)*15);

            Random ran = new Random();
            int x_coordinate = ran.nextInt(26)+2;
            int y_coordinate = ran.nextInt(16)+2;

            enemies.add(new Enemy( x_coordinate*30 ,/*getM()/2*/ y_coordinate*30  ,40,65,enemyImages,damage,visionRange,attackRange,healthPoints,moveSpeed,levelDepth,  false));
        }
    }

    public Image[] getImages(int width, int height, int width_margin,
                             int height_margin, int rows, int cols, int starter_height, int starter_width, String fileName)
            throws IOException
    {

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


    //does a combat room prevent you from leaving while enemies are alive?
    //if so, make that


}
