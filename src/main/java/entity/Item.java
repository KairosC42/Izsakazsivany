package entity;

import entity.Sprite;

import java.awt.*;

public class Item extends Sprite
{
    private int price;
    private String name;

    Item(int x, int y, int width, int height, Image image, int price, String name)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.price=price;
        this.name=name;
    }

    //getters

    public int getPrice()
    {
        return price;
    }

    public String getName()
    {
        return name;
    }
    //setters


    public void setPrice(int price)
    {
        this.price = price;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}
