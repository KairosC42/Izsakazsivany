package com.csapat.sound;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sfx
{

    private final String audioFolderPath="audio/";
    private final String  playerAttackFileName="playerAttack.wav";
    private final String  itemPickUpFileName="itemPickUp.wav";
    private final String  enemyDeathFileName="enemyDeath.wav";
    private final String  levelUpFileName="levelUp.wav";
    private final String  enemyAttackFileName="enemyAttack.wav";
    private final String playerDeathFileName="playerDeath.wav";
    private final String playerHurtFileName="playerHurt.wav";
    private final String potionPickUpFileName="potionPickUp.wav";
    private final String usePotionFileName="usePotion.wav";
    private final String weaponPickUpFileName="weaponPickUp.wav";

    //Todo: potion drinking sfx, weapon equip sfx, player hurt sfx

    //private static Clip playerAttack;
    //private static Clip enemyAttack;
    //private static Clip levelUp;
    //private static Clip itemPickUp;
    //private static Clip enemyDeath;
    //private static Clip playerDeath;




    public synchronized  void playerAttack()
    {
        playAudioFile(playerAttackFileName,audioFolderPath);
    }

    public synchronized  void itemPickUp()
    {
        playAudioFile(itemPickUpFileName,audioFolderPath);
    }

    public synchronized  void enemyDeath()
    {
        playAudioFile(enemyDeathFileName,audioFolderPath);
    }

    public synchronized  void levelUp()
    {
        playAudioFile(levelUpFileName,audioFolderPath);
    }
    public synchronized  void enemyAttack()
    {
        playAudioFile(enemyAttackFileName,audioFolderPath);
    }
    public synchronized  void playerDeath()
    {
        playAudioFile(playerDeathFileName,audioFolderPath);
    }

    public synchronized void potionPickUp()
    {
        playAudioFile(potionPickUpFileName,audioFolderPath);
    }

    public synchronized void usePotion()
    {
        playAudioFile(usePotionFileName,audioFolderPath);
    }

    public synchronized void playerHurt()
    {
        playAudioFile(playerHurtFileName,audioFolderPath);
    }

    public synchronized void weaponPickUp()
    {
        playAudioFile(weaponPickUpFileName,audioFolderPath);
    }




    private synchronized  void playAudioFile(String fileName, String folder)
    {
        try
        {
            Clip clip;
            AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(folder+fileName));
            clip=AudioSystem.getClip();
            clip.open(audioInputStream);
            //clip.addLineListener(new CloseClipWhenDone());
            clip.start();
        } catch (UnsupportedAudioFileException e)
        {
            e.printStackTrace();
            System.out.println("Wrong audio format while loading "+fileName);
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Can't find file:  "+fileName);
        } catch (LineUnavailableException e)
        {
            e.printStackTrace();
            System.out.println("Clip error while loading: "+fileName);
        }
    }
    /*
    private static class CloseClipWhenDone implements LineListener
    {
        @Override public void update(LineEvent event)
        {
            if (event.getType().equals(LineEvent.Type.STOP))
            {
                Line soundClip = event.getLine();
                soundClip.close();

                //Just to prove that it is called...
                System.out.println("Done playing " + soundClip.toString());
            }
        }
    }
     */


}
