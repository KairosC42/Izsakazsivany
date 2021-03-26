package entity;

import java.awt.*;
import java.util.Vector;

public class Attack extends Sprite{

    private Sprite source;
    private Vector<Sprite> target;
    private int speed = 1;
    private boolean isEnded = false;
    private int range;
    private int starter_x;
    private int starter_y;
    private Directions dir;


    public Attack(int x, int y, int width, int height, Image image, Sprite source, Vector<Sprite> target, Directions dir, int range) {
        if(dir==Directions.Down)
        {
            this.x = source.x + ((source.width/2) - (width/2));
            this.y = source.y + source.height;
        }
        if(dir==Directions.Up)
        {
            this.x = source.x + ((source.width/2) - (width/2));
            this.y = source.y - height;
        }
        if(dir==Directions.Left)
        {
            this.x = source.x - height;
            this.y = source.y + ((source.height/2) - (width/2));
        }
        if(dir==Directions.Right)
        {
            this.x = source.x + source.width;
            this.y = source.y + ((source.height/2) - (width/2));
        }
        starter_x = this.x;
        starter_y = this.y;

        if(dir==Directions.Right || dir==Directions.Left)
        {
            this.height = width;
            this.width = height;
        }
        else
        {
            this.width = width;
            this.height = height;
        }
        this.image = image;
        this.source = source;
        this.target = target;
        this.dir = dir;
        this.range = range;
    }

    public void cast()
    {
        switch (dir) {
            case Up: this.y-=speed;
                break;
            case Down: this.y+=speed;
                break;
            case Left: this.x-=speed;
                break;
            case Right:this.x+=speed;
                break;
        }
        if(isOutOfRange())
        {
            isEnded = true;
        }
    }

    public boolean isEnded() {
        return isEnded;
    }

    public boolean isOutOfRange()
    {
        int base_y = this.y + source.height/2;
        int base_x = this.x + source.width/2;
        //System.out.println((Math.sqrt((base_y - this.y) * (base_y - this.y) + (base_x - this.x) * (base_x - this.x))));
        //System.out.println((Math.sqrt(((base_y - this.y-this.height) * (base_y - this.y-this.height)) + ((base_x - this.x) * (base_x - this.x)))));

        switch (dir)
        {
            case Left:
            case Right:
                if(Math.abs(starter_x-this.x)>range-this.width)
                {
                    return true;
                }
                break;
            case Up:
            case Down:
                if(Math.abs(starter_y-this.y)>range-this.height)
                {
                    return true;
                }
                break;
            default: return false;

        }
        return false;
    }


}
