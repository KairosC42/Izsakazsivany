package com.csapat.entity;

import java.awt.Image;

public class StatItem extends Item
{
    //all save for healthModifier are percentages, apply them with multiplication!
    int healthModifier=0;
    float rangeModifier=1.f;
    float attackSpeedModifier=1.f;
    float damageModifier=1.f;
    float speedModifier=1.f;

    public StatItem(int x, int y, int width, int height, Image image, int price, String name,int healthModifier, float rangeModifier, float attackSpeedModifier, float damageModifier, float speedModifier)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.price=price;
        this.name=name;
        this. healthModifier=healthModifier;
        this.attackSpeedModifier=attackSpeedModifier;
        this.speedModifier=speedModifier;
        this.rangeModifier=rangeModifier;
        this.damageModifier=damageModifier;

    }

    //getters


    public float getAttackSpeedModifier() {
        return attackSpeedModifier;
    }

    public float getDamageModifier() {
        return damageModifier;
    }

    public float getRangeModifier() {
        return rangeModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public int getHealthModifier() {
        return healthModifier;
    }
}
