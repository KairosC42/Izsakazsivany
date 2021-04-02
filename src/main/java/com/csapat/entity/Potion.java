package com.csapat.entity;

import java.awt.Image;

public class Potion extends Item
{
    int healthRestore=0;
    int grantExp=0;

    public Potion()
    {

    }

    public Potion(int x, int y, int width, int height, Image image, int price, String name, int healthRestore, int grantExp)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.price=price;
        this.name=name;
        this.healthRestore=healthRestore;
        this.grantExp=grantExp;
    }

    //getters

    public int getHealthRestore() {
        return healthRestore;
    }

    public int getGrantExp() {
        return grantExp;
    }
}
