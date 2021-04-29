package com.csapat.gui;

import com.csapat.entity.Player;
import com.csapat.entity.StatItem;
import com.csapat.levelLayoutGeneration.Level;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class MapInventoryPanel extends JPanel
{

    private Level level;
    private Player player;
    Vector<StatItem> equippedItems = new Vector<>();

    public MapInventoryPanel(Level level, Player player)
    {
        this.level=level;
        this.player=player;
        equippedItems=player.getEquippedItems();

        System.out.println(equippedItems.size());


        this.setLayout(new BorderLayout());

        for (StatItem item:equippedItems)
        {
            ImageIcon icon = new ImageIcon(item.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            JLabel jLabel=new JLabel("",icon,JLabel.CENTER);
            System.out.println("img");
            this.add(jLabel,BorderLayout.CENTER);

        }

    }

}
