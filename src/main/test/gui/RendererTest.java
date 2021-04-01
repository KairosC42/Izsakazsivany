package gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class RendererTest
{
    @Test
    public void RendererTest()
    {
        JFrame frame = new JFrame("Izsák A Zsivány");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Renderer gameRenderer = new Renderer(900,600,frame);
        gameRenderer.initTiles();
        Assertions.assertFalse(gameRenderer.getTiles().length==0,"A pálya beöltése nem üres tömböt eredményez");
    }
}