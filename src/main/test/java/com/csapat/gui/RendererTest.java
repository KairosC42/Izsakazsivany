package gui;

import com.csapat.entity.Attack;
import com.csapat.entity.Enemy;
import com.csapat.entity.Player;
import com.csapat.entity.Sprite;
import com.csapat.gui.Renderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

class RendererTest
{
    @Test
    public void RendererTest()
    {
        /*JFrame frame = new JFrame("Izsak");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        com.csapat.gui.Renderer gameRenderer = new Renderer(900,600,frame);
        gameRenderer.initTiles();
        Assertions.assertFalse(gameRenderer.getTiles().length==0,"A palya betoltese nem ures tombot eredmenyez");*/

        try
        {
            Image playerimg = ImageIO.read(this.getClass().getClassLoader().getResource("player.png"));
            Image[] playerImages = {playerimg};
            Player player = new Player(450,100, 30, 30,playerImages,30,30);
            Assertions.assertTrue(player.getHealth()==100, "Kezdő HP");
            Assertions.assertTrue(player.getMoveSpeed()==2, "kezdő mozgási sebesség");

            Image attackImg = ImageIO.read(this.getClass().getClassLoader().getResource("attack.png"));

            Vector<Sprite> enemies = new Vector<>();

            Attack attack = new Attack(player.getX(), player.getY(), 10,50, attackImg , player, enemies, player.getDirection(), player.getRange());
            Assertions.assertTrue(attack.getSpeed()==1,"Attack sebessége");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}