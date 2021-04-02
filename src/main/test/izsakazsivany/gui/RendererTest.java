package izsakazsivany.gui;

import gui.Renderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

class RendererTest
{
    @Test
    public void RendererTest()
    {
        JFrame frame = new JFrame("Izsák A Zsivány");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gui.Renderer gameRenderer = new Renderer(900,600,frame);
        gameRenderer.initTiles();
        Assertions.assertFalse(gameRenderer.getTiles().length==0,"A pálya beöltése nem üres tömböt eredményez");
    }
}