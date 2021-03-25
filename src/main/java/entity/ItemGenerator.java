package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Random;
/**
* ItemGenerator class
* used for creating items either dropped by enemies or for shops or item rooms
* for shops call with hasPrice=true to calculate a price, otherwise with false to have price set to 0
*
*  i wanted this class to be static so it doesn't require instantiation, but random cannot be used in a static environment
* value inside are subject to change
*
*  use method generateAnything() to generate either a statItem, weapon or potion
*
*  for now textures aren't randomly chosen, this will change
*
*  levelDepth is just freshly implemented, and is also subject to change just like the rest of the statistics are.
*  for now the per-level scaling isn't very high, that's because enemies can drop items too
*
*
* @author: Ballai András
 */
public  class ItemGenerator
{
    Random rand;
    Image i;
    public ItemGenerator()
    {
        this.rand=new Random();
        this.i=null;
    }
    public Item generateStatItem(int levelDepth, boolean hasPrice)
    {
        Image i = null;
        try{
            i = ImageIO.read(this.getClass().getClassLoader().getResource("ruby_pendant.png"));
        }
        catch (Exception e)
        {
            System.out.println("Missing texture!");
        }
        int healthModifier= (rand.nextInt(25)+10) + (levelDepth-1)*15;
        float rangeModifier = (rand.nextInt(15)+5) + (levelDepth-1)*5  /100.f;
        float attackSpeedModifier=(rand.nextInt(10)+5) + (levelDepth-1)*3 /100.f;
        float damageModifier=(rand.nextInt(8)+2) + (levelDepth-1)*2 /100.f;
        float speedModifier=(rand.nextInt(8)+2) + (levelDepth-1)*2 /100.f;

        float price = ( (50 + (levelDepth-1)*0.4f) * (rangeModifier*100.f/12.5f) * (damageModifier*100.f/6.f) * (attackSpeedModifier*100.f/10.f) * (speedModifier/100.f)/6.f * (healthModifier/22.5f) );
        int realPrice=0;
        if(hasPrice){ realPrice = Math.round(price);}

        return new StatItem(0,0,50,50,i,realPrice,"statItem", healthModifier, rangeModifier, attackSpeedModifier, damageModifier, speedModifier);
    }

    public Item generateWeapon(int levelDepth, boolean hasPrice)
    {
        int weaponRangeModifier = rand.nextInt(40)+50 + (levelDepth-1)*15;
        int weaponDamageModifier = rand.nextInt(10)+15 + (levelDepth-1)*8 ;
        float attackSpeedMultiplier = ((rand.nextInt(15)+10 + (levelDepth-1)*5)  /100.f);
        try{
            i = ImageIO.read(this.getClass().getClassLoader().getResource("sword.png"));
        }
        catch (Exception e)
        {
            System.out.println("Missing texture!");
        }
        float price = ( (50 + (levelDepth-1)*0.4f) * (weaponRangeModifier/275.f) * (weaponDamageModifier/20.f) * (attackSpeedMultiplier*100/20.0f) );
        int realPrice=0;
        if(hasPrice){ realPrice = Math.round(price);}
       return new Weapon(0, 0,50, 50,i,realPrice, "weapon" , weaponRangeModifier, weaponDamageModifier, attackSpeedMultiplier);
    }

    public Item generatePotion(int levelDepth, boolean hasPrice)
    {
        int healthRestore =0;
        int grantExp=0;
        float price=25 + (levelDepth-1)*0.4f;
        if(rand.nextBoolean())
        {
            healthRestore= rand.nextInt(30)+20 +(levelDepth-1)*15 ;
            price *= healthRestore/35.f;
        }
        else
        {
            grantExp = rand.nextInt(50)+30 +(levelDepth-1)*20 ;
            price *= grantExp/55.f;

        }
        Image im = null;
        try{
            im = ImageIO.read(this.getClass().getClassLoader().getResource("red_potion.png"));
        }
        catch (Exception e) {
            System.out.println("Missing texture!");
        }
        int realPrice=0;
        if(hasPrice){ realPrice = Math.round(price);}
        return new Potion(0,0,50,50,im,realPrice, "potion", healthRestore, grantExp);
    }

    public Item generateSomething(int levelDepth,boolean hasPrice)
    {
        int what = rand.nextInt(3);
        Item ret=null;
        switch(what)
        {
            case 0:
                ret= generateStatItem(levelDepth,hasPrice);
                break;
            case 1:
                ret= generateWeapon(levelDepth,hasPrice);
                break;
            case 2:
                ret= generatePotion(levelDepth,hasPrice);
                break;
        }
        return ret;
    }

}
