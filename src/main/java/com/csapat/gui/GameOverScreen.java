package com.csapat.gui;

import com.csapat.entity.Player;
import com.csapat.levelLayoutGeneration.Level;

import javax.swing.*;
import java.awt.*;

public class GameOverScreen extends JPanel
{
    private Level level;
    private Player player;

    private final Color backgroundColor=Color.BLACK;


    private final String gameOverColor="<font color='red'>";
    private final String fontColor="<font color='white'>";

    private final JPanel jPanelCenter= new JPanel();


    public GameOverScreen(Level level,Player player)
    {
        this.level=level;
        this.player=player;
        this.setLayout(new BorderLayout());

        jPanelCenter.setLayout(new BorderLayout());
        jPanelCenter.setBackground(backgroundColor);

        displayGameOver();


    }

    private void displayGameOver()
    {
        JLabel jLabel=new JLabel();
        String startBig="<tr><td style=\"text-align:center;font-size:45\">";
        String start="<tr><td style=\"text-align:center; font-size:20\">";
        String end="</td></tr>";
        String fontEnd="</font>";
        StringBuilder gameOver=new StringBuilder("<html><table>");
        gameOver.append(startBig).append(gameOverColor).append("Game Over").append(fontEnd).append(end);
        gameOver.append(start).append(fontColor).append("Press enter to retry").append(fontEnd).append(end);


        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jLabel.setText(gameOver.toString());
        jPanelCenter.add(jLabel,BorderLayout.CENTER);

        this.add(jPanelCenter,BorderLayout.CENTER);


    }
}
