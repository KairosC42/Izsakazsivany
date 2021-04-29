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
    private Vector<StatItem> equippedItems = new Vector<>();
    private JPanel jLabelSouth= new JPanel();
    private JPanel jLabelWest = new JPanel();

    public MapInventoryPanel(Level level, Player player)
    {
        this.level=level;
        this.player=player;
        equippedItems=player.getEquippedItems();
        this.setLayout(new BorderLayout());

        jLabelSouth.setLayout(new FlowLayout());
        jLabelWest.setLayout(new FlowLayout());
        displayEquippedItems();
        displayStats();


    }

    private void displayEquippedItems()
    {
        for (StatItem item:equippedItems)
        {
            ImageIcon icon = new ImageIcon(item.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            System.out.println("img");
            jLabelSouth.add(new JLabel(icon));
        }
        this.add(jLabelSouth,BorderLayout.SOUTH);

    }

    private void displayStats()
    {
        JLabel jLabel=new JLabel();
        String hp="<tr><td>Health: "+player.getHealthPoints()+"\\"+player.getHealthPointsMax()+"<td></tr>";
        String range="<tr><td>Range: "+player.getRange()+"<td></tr>";
        String attackSpeed="<tr><td>Attack speed: "+player.getAttackSpeed()+"<td></tr>";
        String damage="<tr><td>Damage: "+player.getDamage()+"<td></tr>";
        String level="<tr><td>Level: "+player.getPlayerLevel()+"<td></tr>";
        String experience="<tr><td>Experience: "+player.getExperience()+"\\"+player.getNextLevelThreshold()+"<td></tr>";
        String money="<tr><td>Money: "+player.getMoney()+"<td></tr>";
        String moveSpeed="<tr><td>Move speed: "+player.getMoveSpeed()+"<td></tr>";
        jLabel.setText("<html><table>"+level+experience+hp+damage+attackSpeed+range+moveSpeed+money+"</table></html>");
        jLabelWest.add(jLabel);
        this.add(jLabelWest,BorderLayout.WEST);

    }
    


}
