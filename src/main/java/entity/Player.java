package entity;

import entity.Sprite;

import java.awt.Image;
import java.util.Vector;

public class Player extends Sprite
{


    /**
     * Todo:
     *      -healthPointsMax is a new data,
     *      -check the usage of healthPoints and replace it with healthPointsMax if necessary
     *      -revision needed for starting stats
     *      -potions as a quippedItem? or in a separate Vector?
     *
     *
     *      -módosító külön
     */
    private int healthPointsMax=100;
    private int healthPoints=100;
    private float range = 60;
    private float attackSpeed = 1;
    private float damage = 20;
    private int moveSpeed = 4;
    private int experince = 0;
    private Directions direction = Directions.Down;
    //private Inventory inventory;
    private int velx;
    private int vely;
    private int money;
    Vector<StatItem> equippedItems = new Vector<>();
    Weapon equippedWeapon = new Weapon();
    int walkingTime;
    Image playerImages[];
    private int frameHeight;
    private int frameWidth;

    private boolean lastMove; //false, y-n, true- x-en

    public Player(int x, int y, int width, int height, Image playerImages[],int frameHeight, int frameWidth)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.playerImages = playerImages;
        this.image = playerImages[0];
        this.frameHeight = frameHeight;
        this.frameWidth = frameWidth;
    }

    public void update(float deltaTime)
    {
        walkingTime += deltaTime;
    }

    public void stepBack()
    {
        if(lastMove)
        {
            x-=velx;
            System.out.println("x-en visszalép");
        }
        else if(!lastMove)
        {
            y-=vely;
            System.out.println("y-on visszalép");
        }
    }
    public void moveX()
    {
            if (velx > 0)
            {
                //jobbra
                this.image = playerImages[12];
                direction = Directions.Right;
                lastMove = true;
            } else if (velx < 0)
            {
                //balra
                this.image = playerImages[9];
                direction = Directions.Left;
                lastMove = true;
            }
            x += velx;
    }

    public void moveY()
    {
            if (vely > 0)
            {
                //előre
                this.image = this.image = playerImages[0];
                direction = Directions.Down;
                lastMove = false;
            } else if (vely < 0)
            {
                //hátra
                this.image = this.image = playerImages[4];
                direction = Directions.Up;
                lastMove = false;
            }
            y += vely;


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

    public Directions getDirection()
    {
        return direction;
    }

    public int getHealthPointsMax()
    {
        return healthPointsMax;
    }

    public int getHealthPoints()
    {
        return healthPoints;
    }

    public float getAttackSpeed()
    {
        return attackSpeed;
    }

    public float getDamage()
    {
        return damage;
    }

    public int getExperince()
    {
        return experince;
    }

    public void equipWeapon(Weapon weapon)
    {
        range -= equippedWeapon.rangeModifier;
        attackSpeed -= equippedWeapon.attackSpeedModifier;
        attackSpeed -= equippedWeapon.damageModifier;

        range += weapon.rangeModifier;
        attackSpeed += weapon.attackSpeedModifier;
        attackSpeed += weapon.damageModifier;

        equippedWeapon = weapon;


    }

    public void equipItem(StatItem item)
    {

        healthPointsMax += item.getHealthModifier();
        healthPoints += item.getHealthModifier();

        range *= item.getRangeModifier();
        attackSpeed *= item.getAttackSpeedModifier();
        damage *= item.getDamageModifier();
        moveSpeed *= item.getSpeedModifier();
    }



    public int getRange()
    {
        /**
         * TODO:
         *      -range int or float?? in Player and every item is a float, in Attack is an int, consensus needed.
         *      -szorzat modi * stat
         */
        return ((int) range);
    }
}
