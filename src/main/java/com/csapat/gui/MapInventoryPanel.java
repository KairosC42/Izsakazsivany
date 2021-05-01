package com.csapat.gui;

import com.csapat.entity.Player;
import com.csapat.entity.Potion;
import com.csapat.entity.StatItem;
import com.csapat.entity.Weapon;
import com.csapat.levelLayoutGeneration.Level;
import com.csapat.levelLayoutGeneration.RoomNode;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class MapInventoryPanel extends JPanel
{

    private final Level level;
    private final Player player;
    private final RoomNode currentRoomNode;
    //GridBagConstraints c;
    GridBagLayout layout=new GridBagLayout();
    private Vector<StatItem> equippedItems = new Vector<>();
    private Vector<Potion> potions = new Vector<>();
    private final JPanel jPanelSouth = new JPanel();
    private final JPanel jPanelWest = new JPanel();
    private final JPanel jPanelNorth = new JPanel();
    private final JPanel jPanelCenter= new JPanel();
    private final JPanel jPanelEast= new JPanel();

    private final Color backgroundColor=Color.BLACK;
    private final String fontColor="<font color='white'>";
    private final String combatRoomColor="<font color= rgb(165,34,189)>";
    private final String itemRoomColor="<font color='yellow'>";
    private final String shopRoomColor="<font color='blue'>";
    private final String bossRoomColor="<font color='red'>";
    private final String startRoomColor="<font color='white'>";
    private final String nullRoomColor="<font color='black'>";

    private final String currentPosColor="<font color='green'>";

    public MapInventoryPanel(Level level, Player player,RoomNode currentRoomNode)
    {
        this.level=level;
        this.player=player;
        this.currentRoomNode=currentRoomNode;
        this.equippedItems=player.getEquippedItems();
        this.potions=player.getPotions();
        this.setLayout(new BorderLayout());
        //c= new GridBagConstraints();
        this.setBackground(Color.BLACK);

        jPanelSouth.setLayout(new FlowLayout());
        jPanelSouth.setBackground(backgroundColor);
        jPanelWest.setLayout(new FlowLayout());
        jPanelWest.setBackground(backgroundColor);
        jPanelNorth.setLayout(new FlowLayout());
        jPanelNorth.setBackground(backgroundColor);
        jPanelCenter.setLayout(new BorderLayout());
        jPanelCenter.setBackground(backgroundColor);
        jPanelEast.setLayout(new FlowLayout());
        jPanelEast.setBackground(backgroundColor);
        displayEquippedItems();
        displayStats();
        displayPotions();
        displayWeaponStats();
        displayMap();


    }

    private void displayEquippedItems()
    {
        for (StatItem item:equippedItems)
        {
            ImageIcon icon = new ImageIcon(item.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            jPanelSouth.add(new JLabel(icon));
        }
        ImageIcon icon = new ImageIcon(player.getEquippedWeapon().getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        jPanelSouth.add(new JLabel(icon));

        //c.fill=GridBagConstraints.HORIZONTAL;
        //c.gridx=1;
        //c.gridy=4;
        //c.gridwidth=1;
        //c.gridheight=3;
        //c.anchor=GridBagConstraints.PAGE_END;
        //layout.setConstraints(jPanelSouth,c);
        //this.add(jPanelSouth,c);
        this.add(jPanelSouth,BorderLayout.SOUTH);

    }

    private void displayStats()
    {
        JLabel jLabel=new JLabel();
        String text="<tr><td>"+fontColor+"Player statistics "+"</font></td></tr>";
        String levelDepth="<tr><td>"+fontColor+"Level depth: "+level.getLevelDepth()+"</font></td></tr>";
        String hp="<tr><td>"+fontColor+"Health: "+player.getHealthPoints()+"\\"+player.getHealthPointsMax()+"</font></td></tr>";
        String range="<tr><td>"+fontColor+"Range: "+player.getRange()+"</font></td></tr>";
        String attackSpeed="<tr><td>"+fontColor+"Attack speed: "+player.getAttackSpeed()+"</font><td></tr>";
        String damage="<tr><td>"+fontColor+"Damage: "+player.getDamage()+"</font></td></tr>";
        String level="<tr><td>"+fontColor+"Level: "+player.getPlayerLevel()+"</td></tr>";
        String experience="<tr><td>"+fontColor+"Experience: "+player.getExperience()+"\\"+player.getNextLevelThreshold()+"</font></td></tr>";
        String money="<tr><td>"+fontColor+"Money: "+player.getMoney()+"</font></td></tr>";
        String moveSpeed="<tr><td>"+fontColor+"Move speed: "+player.getMoveSpeed()+"</font></td></tr>";
        String killCount="<tr><td>"+fontColor+"Enemies killed: "+player.getKillcount()+"</font></td></tr>";
        jLabel.setText("<html><table>"+levelDepth+text+level+experience+hp+damage+attackSpeed+range+moveSpeed+money+killCount+"</table></html>");
        jPanelWest.add(jLabel);

        //c.fill=GridBagConstraints.VERTICAL;
        //c.gridx=0;
        //c.gridy=1;
        //c.gridwidth=1;
        //c.gridheight=3;
        //c.anchor=GridBagConstraints.FIRST_LINE_START;
        //layout.setConstraints(jPanelWest,c);
        //this.add(jPanelWest,c);

        this.add(jPanelWest,BorderLayout.WEST);

    }

    private void displayPotions()
    {
        for (Potion potion:potions)
        {
            ImageIcon icon = new ImageIcon(potion.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            String restore="<html>"+fontColor+potion.getHealthRestore()+"</font></html>";
            jPanelNorth.add(new JLabel(restore,icon,JLabel.TRAILING));
        }
        //c.fill=GridBagConstraints.VERTICAL;
        //c.gridx=1;
        //c.gridy=0;
        //c.gridwidth=1;
        //c.gridheight=3;
        //c.anchor=GridBagConstraints.PAGE_START;
        //layout.setConstraints(jPanelNorth,c);
        //this.add(jPanelNorth,c);
        this.add(jPanelNorth,BorderLayout.NORTH);

    }

    private void displayWeaponStats()
    {
        Weapon weapon=player.getEquippedWeapon();
        JLabel jLabel=new JLabel();
        String text="<tr><td>"+fontColor+"Weapon statistics"+"</font></td></tr>";
        String damage="<tr><td>"+fontColor+"Damage: "+String.valueOf(weapon.getDamageModifier())+"</font></td></tr>";
        String attackSpeed="<tr><td>"+fontColor+"Attack speed: "+String.valueOf(weapon.getAttackSpeedModifier())+"</font></td></tr>";
        String range="<tr><td>"+fontColor+"Range: "+String.valueOf(weapon.getRangeModifier())+"</font></td></tr>";
        jLabel.setText("<html><table>"+text+range+damage+attackSpeed+"</table></html>");
        jPanelEast.add(jLabel);

        //c.fill=GridBagConstraints.HORIZONTAL;
        //c.gridx=4;
        //c.gridy=1;
        //c.gridwidth=1;
        //c.gridheight=3;
        //c.anchor=GridBagConstraints.LINE_END;
        //layout.setConstraints(jPanelEast,c);
        //this.add(jPanelEast,c);
        this.add(jPanelEast,BorderLayout.EAST);


    }

    private void displayMap()
    {
        RoomNode[][] roomMatrix=level.getRoomMatrix();
        JLabel jLabel=new JLabel();
        String fontEnd="</font>";

        StringBuilder map= new StringBuilder("<html><table>");
        for (int i = 0; i <roomMatrix.length ; i++)
        {
            map.append("<tr>");
            for (int j = 0; j <roomMatrix[0].length ; j++)
            {
                map.append("<td style=\"font-size:20px;\">");

                if(roomMatrix[i][j]!=null)
                {
                    if(roomMatrix[i][j].getCoordinate()!=currentRoomNode.getCoordinate())
                    {
                        switch (roomMatrix[i][j].getRoomType())
                        {
                            case STARTROOM:
                                map.append(startRoomColor + "O").append(fontEnd);
                                break;
                            case ITEMROOM:
                                map.append(itemRoomColor + "I").append(fontEnd);
                                break;
                            case SHOP:
                                map.append(shopRoomColor + "S").append(fontEnd);
                                break;
                            case BOSSROOM:
                                map.append(bossRoomColor + "B").append(fontEnd);
                                break;
                            case COMBATROOM:
                                map.append(combatRoomColor + "C").append(fontEnd);
                                break;

                        }
                    }
                    else
                    {
                        map.append(currentPosColor + "P").append(fontEnd);
                    }
                }
                else
                {
                    map.append(nullRoomColor + " ").append(fontEnd);

                }
                map.append("</td>");
            }
            map.append("</tr>");

        }



        map.append("</table></html>");

        String start="<tr><td style=\"font-size:8px;\">";
        String end="</td></tr>";
        String startRoomText=start+startRoomColor+"O = Start Room"+fontEnd+end;
        String itemRoomText=start+itemRoomColor+"I = Item Room"+fontEnd+end;
        String shopRoomText=start+shopRoomColor+"S = Shop Room"+fontEnd+end;
        String bossRoomText=start+bossRoomColor+"B = Boss Room"+fontEnd+end;
        String combatRoomText=start+combatRoomColor+"C = Combat Room"+fontEnd+end;
        String playerPosText=start+currentPosColor+"P = Player position"+fontEnd+end;

        JLabel ref=new JLabel();
        StringBuilder refText= new StringBuilder("<html><table>");
        refText.append(startRoomText).append(itemRoomText).append(shopRoomText).append(bossRoomText).append(combatRoomText).append(playerPosText);
        refText.append("</table></html>");
        ref.setText(refText.toString());

        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        ref.setHorizontalAlignment(JLabel.CENTER);
        ref.setVerticalAlignment(JLabel.CENTER);

        jLabel.setText(map.toString());
        jPanelCenter.add(jLabel,BorderLayout.CENTER);
        //jPanelCenter.add(ref,BorderLayout.SOUTH);


        //c.fill=GridBagConstraints.BOTH;
        //c.gridx=1;
        //c.gridy=1;
        //c.gridwidth=3;
        //c.gridheight=3;
        //c.anchor=GridBagConstraints.CENTER;
        //layout.setConstraints(jPanelCenter,c);
        //this.add(jPanelCenter,c);
        this.add(jPanelCenter,BorderLayout.CENTER);

    }

}
