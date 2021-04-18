package com.csapat.entity;

import javax.imageio.ImageIO;
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
     *
     *
     *      -módosító külön
     */
    //Todo: leveling system!

    private int healthPointsMax=100;
    private int healthPoints=100;
    private float range = 0;
    private float attackSpeed = 0;
    private float damage = 0;
    private int moveSpeed = 2;
    private int experince = 0;
    private Directions direction = Directions.Down;
    //private Inventory inventory;
    private int velx;
    private int vely;
    private int money=100;
    Vector<StatItem> equippedItems = new Vector<>();
    Vector<Potion> potions=new Vector<>();
    Weapon equippedWeapon = new Weapon();
    int walkingTime;
    Image playerImages[];
    private int frameHeight;
    private int frameWidth;

    private int healthPointsMaxModifier=0;

    private float rangeModifier=1;
    private float attackSpeedModifier=1;
    private float damageModifier=1;
    private float moveSpeedModifier=1;

    private boolean lastMove; //false, y-n, true- x-en

    public Player(int x, int y, int width, int height, Image playerImages[],int frameHeight, int frameWidth)
    {
        Image weaponImage=null;
        try{
            weaponImage = ImageIO.read(this.getClass().getClassLoader().getResource("sword.png"));
        }
        catch (Exception e)
        {
            System.out.println("Missing texture!");
        }
        equipItem(new Weapon(0,0,0,0,weaponImage,0,"starting weapon",60,20,1));
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

    public void buyItem(Item item)
    {
        this.money-=item.getPrice();
        this.equipItem(item);
    }

    public void stepBack()
    {
        if(lastMove)
        {
            x-=velx;
            //System.out.println("x-en visszalép");
        }
        else if(!lastMove)
        {
            y-=vely;
            //System.out.println("y-on visszalép");
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

    public void setMoveSpeed(int moveSpeed)
    {
        this.moveSpeed = moveSpeed;
    }

    /**
     *
     * @param money Should always be positive!
     */
    public void setMoney(int  money){this.money+=money;}

    public int getMoveSpeed()
    {
        return (int)(moveSpeed*moveSpeedModifier);
    }



    public Directions getDirection()
    {
        return direction;
    }

    public int getHealthPointsMax()
    {
        return healthPointsMax+healthPointsMaxModifier;
    }

    public int getHealthPoints()
    {
        return healthPoints;
    }

    public float getAttackSpeed()
    {
        return attackSpeed*attackSpeedModifier;
    }

    public float getDamage()
    {
        return damage*damageModifier;
    }

    public int getExperince()
    {
        return experince;
    }

    public int getMoney(){return money;}


    public void equipWeapon(Weapon weapon)
    {
        rangeModifier -= equippedWeapon.rangeModifier;
        attackSpeedModifier -= equippedWeapon.attackSpeedModifier;
        attackSpeedModifier -= equippedWeapon.damageModifier;

        rangeModifier += weapon.rangeModifier;
        attackSpeedModifier += weapon.attackSpeedModifier;
        attackSpeedModifier += weapon.damageModifier;
        equippedWeapon = weapon;


    }

    public void equipItem(Item item)
    {
        if(item instanceof StatItem)
        {
            StatItem statItem=(StatItem) item;
            healthPointsMaxModifier += statItem.getHealthModifier();
            healthPoints += statItem.getHealthModifier();

            rangeModifier += statItem.getRangeModifier();
            attackSpeedModifier += statItem.getAttackSpeedModifier();
            damageModifier += statItem.getDamageModifier();
            moveSpeedModifier += statItem.getSpeedModifier();
        }
        else if(item instanceof Potion)
        {
            if( ((Potion)item).getHealthRestore()==0)
            {
                experince+=((Potion)item).grantExp;
            }
            potions.add((Potion) item);
        }
        else if(item instanceof Weapon)
        {
            Weapon weapon=(Weapon) item;
            range -= equippedWeapon.rangeModifier;
            attackSpeed -= equippedWeapon.attackSpeedModifier;
            damage -= equippedWeapon.damageModifier;

            range += weapon.rangeModifier;
            attackSpeed += weapon.attackSpeedModifier;
            damage += weapon.damageModifier;
            equippedWeapon = weapon;

        }
        System.out.println(range);
        System.out.println(rangeModifier);
    }

    public void useHealthPotion()
    {
        for(Potion pot : potions)
        {
            if(getExperince()==0)
            {
                healthPoints+=pot.getHealthRestore();
                if(healthPoints>getHealthPointsMax())
                {
                    healthPoints=getHealthPointsMax();
                }
                potions.remove(pot);
                break;
            }
        }
    }





    public int getRange()
    {

        return (int)(range*rangeModifier);
    }

    public void stepBackAfterLeveltransition(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
}
