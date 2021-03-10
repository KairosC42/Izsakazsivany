package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Sprite
{
    protected int x=0;
    protected int y=0;
    protected int  width=0;
    protected int height=0;
    protected Image image=null;

    Sprite()
    {

    }

    Sprite(int x, int y, int width, int height, Image image)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public void draw(Graphics g)
    {
        g.drawImage(image, x, y, width, height, null);
    }

    public boolean collides(Sprite other)
    {
        Rectangle rect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);
        return rect.intersects(otherRect);
    }

    //getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //setters


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }
}
