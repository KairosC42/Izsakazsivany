package gui;

import com.csapat.entity.Player;
import com.csapat.gui.Renderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

class RendererTest
{
    @Test
    public void RendererTest()
    {
        JFrame frame = new JFrame("Izsak");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        com.csapat.gui.Renderer gameRenderer = new Renderer(900,600,frame);
        gameRenderer.initTiles();
        Assertions.assertFalse(gameRenderer.getTiles().length==0,"A palya betoltese nem ures tombot eredmenyez");

        try
        {
            Image playerImages[] = gameRenderer.getImages(300,450,100,150,
                4,4,100,50,"player.png");
            Player player = new Player(450,100, 30, 30,playerImages,30,30);
            Assertions.assertTrue(player.getHealth()==100, "Kezdő HP");
            Assertions.assertTrue(player.getMoveSpeed()==2, "kezdő mozgási sebesség");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}