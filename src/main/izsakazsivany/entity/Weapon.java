package entity;

import java.awt.Image;

public class Weapon extends Item
{
    //these are flat values, but they are going to be quite high.

    int rangeModifier;
    int damageModifier;
    float attackSpeedModifier;

    public Weapon()
    {
        rangeModifier=0;
        damageModifier=0;
        attackSpeedModifier=0;
    }

    public Weapon(int x, int y, int width, int height, Image image, int price, String name, int rangeModifier, int damageModifier, float attackSpeedModifier)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.price=price;
        this.name=name;
        this.rangeModifier=rangeModifier;
        this.attackSpeedModifier=attackSpeedModifier;
        this.damageModifier=damageModifier;
    }

    //getters


    public int getRangeModifier() {
        return rangeModifier;
    }

    public float getAttackSpeedModifier() {
        return attackSpeedModifier;
    }

    public int getDamageModifier() {
        return damageModifier;
    }
}
