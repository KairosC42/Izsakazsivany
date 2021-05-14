package com.csapat.entity;

import java.awt.*;

public class Boss extends Enemy{

    private int maxHealth;
    private boolean isBoosInSecondPhase = false;


    public Boss(int x, int y, int width, int height, Image[] images, int damage, int visionRange, int attackRange, int healthPoints, float speed, int levelDepth) {
        super(x, y, width, height, images, damage, visionRange, attackRange, healthPoints, speed, levelDepth);
        this.maxHealth=healthPoints;
    }

    @Override
    public void takeDamage(int damage)
    {
        int healthPercantage=(healthPoints/maxHealth)*100;
        int nextHealth=healthPoints-damage;
        int nextHealthPercantage=(nextHealth/maxHealth)*100;
        if(healthPercantage>34 && nextHealthPercantage<=34)
        {
            isBoosInSecondPhase = true;
        }
        this.healthPoints-=damage;

        if(healthPoints<0)healthPoints=0;
    }


    public boolean isBoosInSecondPhase() {
        return isBoosInSecondPhase;
    }

    public void setBoosInSecondPhase(boolean boosInSecondPhase) {
        isBoosInSecondPhase = boosInSecondPhase;
    }
}
