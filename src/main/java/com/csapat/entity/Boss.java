package com.csapat.entity;

import com.csapat.sound.Sfx;

import java.awt.*;

public class Boss extends Enemy{

    private float maxHealth;
    private boolean isBoosInSecondPhase = false;
    private boolean playedBoosInSecondPhase = false;
    private Sfx sfx;


    public Boss(int x, int y, int width, int height, Image[] images, int damage, int visionRange, int attackRange, int healthPoints, float speed, int levelDepth) {
        super(x, y, width, height, images, damage, visionRange, attackRange, healthPoints, speed, levelDepth, true);
        this.maxHealth=healthPoints;
    }

    @Override
    public Boolean damaged(float dmg,float attack_speed)
    {
        float healthPercentage=(healthPoints/maxHealth)*100;
        int nextHealth=healthPoints-damage;
        float nextHealthPercentage=(nextHealth/maxHealth)*100;
        System.out.println(healthPercentage);
        System.out.println(nextHealthPercentage);

        this.healthPoints-=dmg;
        enemyAttacked = new java.util.Timer();
        enemyAttacked.schedule(new gotDamagedTask(),(int)(1000/attack_speed));

        if(healthPercentage>34 && nextHealthPercentage<=34)
        {
            activateSecondPhase();

        }
        if(this.healthPoints<=0)
        {
            //died
            this.healthPoints=0;
            return true;
        }
        else
        {
            return false;
        }
    }


    public void activateSecondPhase()
    {
        isBoosInSecondPhase = true;
        this.attackRange=(int)(this.attackRange*1.5);
        this.damage=(int)(this.damage*1.5);
    }

    public boolean isBoosInSecondPhase() {
        return isBoosInSecondPhase;
    }


    public void setBoosInSecondPhase(boolean boosInSecondPhase) {
        isBoosInSecondPhase = boosInSecondPhase;
    }

    public void setPlayedBoosInSecondPhase(boolean playedBoosInSecondPhase)
    {
        this.playedBoosInSecondPhase = playedBoosInSecondPhase;
    }

    public boolean isPlayedBoosInSecondPhase()
    {
        return playedBoosInSecondPhase;
    }
}
