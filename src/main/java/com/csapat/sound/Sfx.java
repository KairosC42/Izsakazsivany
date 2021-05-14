package com.csapat.sound;

import com.csapat.gui.Renderer;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

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
    private final String gameOverFileName="gameOver.wav";

    public synchronized  void playerAttack()
    {
        playAudioFile(playerAttackFileName);
    }

    public synchronized  void itemPickUp()
    {
        playAudioFile(itemPickUpFileName);
    }

    public synchronized  void enemyDeath()
    {
        playAudioFile(enemyDeathFileName);
    }

    public synchronized  void levelUp()
    {
        playAudioFile(levelUpFileName);
    }
    public synchronized  void enemyAttack()
    {
        playAudioFile(enemyAttackFileName);
    }
    public synchronized  void playerDeath()
    {
        playAudioFile(playerDeathFileName);
    }

    public synchronized void potionPickUp()
    {
        playAudioFile(potionPickUpFileName);
    }

    public synchronized void usePotion()
    {
        playAudioFile(usePotionFileName);
    }

    public synchronized void playerHurt()
    {
        playAudioFile(playerHurtFileName);
    }

    public synchronized void weaponPickUp()
    {
        playAudioFile(weaponPickUpFileName);
    }
    public synchronized void gameOver()
    {
        playAudioFile(gameOverFileName);
    }

    private Renderer.IntWrapper volume;




    private synchronized  void playAudioFile(String fileName)
    {
        try
        {
            Clip clip;
            AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(Objects.requireNonNull(this.getClass().getClassLoader().getResource(audioFolderPath + fileName)));
            clip=AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl =(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float fVolume = volume.getValue()/100f;
            float gain = (range * fVolume) + gainControl.getMinimum();
            gainControl.setValue(gain);
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

    public void setVolume(Renderer.IntWrapper volume) {
        this.volume = volume;
    }

    public void bossAttack() {
    }
}
