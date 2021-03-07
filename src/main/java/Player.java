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

    public Player(int x, int y, int width, int height, Image image)
    {
        super(x, y, width, height, image);
    }

    public void moveX()
    {
        if ((velx < 0 && x > 0) || (velx > 0 && x + width <= 900))
        {
            if(velx>0)
            {
                this.image= new ImageIcon(this.getClass().getResource("playerRight.png")).getImage();
            }
            else if(velx<0)
            {
                this.image = new ImageIcon(this.getClass().getResource("playerLeft.png")).getImage();
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
                this.image= new ImageIcon(this.getClass().getResource("playerFront.png")).getImage();
            }
            else if(vely<0)
            {
                this.image = new ImageIcon(this.getClass().getResource("playerBack.png")).getImage();
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
