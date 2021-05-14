package com.csapat.rooms;
import com.csapat.entity.Boss;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
public class BossRoom extends CombatRoom
{


    private Image[] bossImages;
    public BossRoom(int levelDepth)
    {
        super(levelDepth);
        generateBoss();
    }
    private void generateBoss(){
        Image enemyTexture=null;
        try
        {
            bossImages = getImages(49,64,11,7,1,4,7,14,"bossImage.png");
        }
        catch(Exception e){System.out.println("Enemy texture is missing");}
        Random rand = new Random();
        int hp = rand.nextInt(100)+400 + (levelDepth-1)*250;
        int damage = rand.nextInt(10)+28 +(levelDepth-1)*25;
        int visionRange = rand.nextInt(50)+400 +(levelDepth-1)*80;
        int attackRange = rand.nextInt(10)+70 +(levelDepth-1)*25;
        float speed = (float) (rand.nextInt(1) + 1.5f +(levelDepth-1)*0.3f);
        float attackSpeed = (float)(1.5f+(levelDepth-1)*0.3f);
        this.enemies = new Vector<>();
        enemies.add(new Boss(400, 500, 100,100, bossImages, damage, visionRange, attackRange, hp, speed, levelDepth ));
    }
    @Override
    public void roomSpecificGen()
    {

        Random rand = new Random();
        int n= rand.nextInt(N-5)+3;
        int m=rand.nextInt(M-5)+3;
        //layout[N][..] is out of bounds
        //layout[N-1] is a wall, shouldn't overwrite that
        //layout[0] is also a wall, so random has to start at 1 (: reason for +1)
        //N-3+1= N-2 is the first non wall floor
        this.layout[n][m]= Tile.TRAPDOOR_OPEN;
        //boss should be an enemy which is bigger with 10-20x the HP and 1-1.5x the damage of normal enemies
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
}