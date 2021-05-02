package com.csapat.sound;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sfx
{

    private final String audioFolderPath="audio/";
    private final String  playerAttackFileName="playerAttack.wav";
    private final String  itemPickUpFileName="itemPickUp.wav";
    private final String  enemyDeathFileName="enemyDeath.wav";

    private Clip playerAttack;
    private Clip enemyAttack;
    private Clip levelUp;
    private Clip itemPickUp;
    private Clip enemyDeath;


    public Sfx()
    {
        playerAttack= loadAudioFile(playerAttackFileName,audioFolderPath);
        itemPickUp= loadAudioFile(itemPickUpFileName,audioFolderPath);
        enemyDeath= loadAudioFile(enemyDeathFileName,audioFolderPath);

    }

    

    private Clip loadAudioFile(String fileName, String folder)
    {
        try
        {
            Clip clip;
            AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(folder+fileName));
            clip=AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
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
        return null;
    }


}
