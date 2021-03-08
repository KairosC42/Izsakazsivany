import javax.swing.*;
import java.awt.Image;

public class Player extends Sprite
{
    private int healthPoints;
    private int experince;
    //private Inventory inventory;
    private int moveSpeed = 4;
    private int velx;
    private int vely;
    private int money;
    //vector<Item> EquippesItems;
    int walkingTime;
    Image playerImages[];

    public Player(int x, int y, int width, int height, Image playerImages[])
    {
        this.x = x;
        this.y = y;
        this.width=width;
        this.height= height;
        this.playerImages = playerImages;
        this.image = playerImages[0];
    }

    public void update(float deltaTime){
        walkingTime += deltaTime ;
    }

    public void moveX()
    {
        if ((velx < 0 && x > 0) || (velx > 0 && x + width <= 900))
        {
            if(velx>0)
            {
                //jobbra
                this.image = playerImages[12];
            }
            else if(velx<0)
            {
                //balra
                this.image = playerImages[9];
            }
            x += velx;
        }
    }

    public void moveY()
    {
        if ((vely < 0 && y > 0) || (vely > 0 && y + height <= 600))
        {
            if(vely>0)
            {
                //előre
                this.image=this.image = playerImages[0];
            }
            else if(vely<0)
            {
                //hátra
                this.image=this.image = playerImages[4];
            }
            y += vely;
        }
    }
    public int getHealth()
    {
        return this.healthPoints;
    }

    public int getVelX()
    {
        return this.velx;
    }

    public int getVelY()
    {
        return this.vely;
    }

    public void setHealth(int health)
    {
        this.healthPoints = health;
    }

    public void setVelX(int velx)
    {
        this.velx = velx;
    }

    public void setVelY(int vely)
    {
        this.vely = vely;
    }

    public int getMoveSpeed()
    {
        return moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed)
    {
        this.moveSpeed = moveSpeed;
    }
}
