package com.csapat.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

/**
* ItemGenerator class
* used for creating items either dropped by enemies or for shops or item com.csapat.rooms
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

//todo: assign textures based on stats
public class ItemGenerator
{
    Random rand;
    Image redPotion;
    Image bluePotion;
    Image glasses;
    Image sword;
    Image bow;
    Image rubyPendant;
    Image emeraldPendant;
    Image muscle;
    Image boots;
    Image fast;
    public ItemGenerator()
    {
        this.rand=new Random();
        try
        {
            redPotion= ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("red_potion.png")));
            bluePotion= ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("blue_potion.png")));
            glasses= ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("glasses.png")));
            sword= ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("sword.png")));
            bow= ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("bow.png")));
            rubyPendant= ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ruby_pendant.png")));
            emeraldPendant= ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("emerald_pendant.png")));
            muscle = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("muscle.png")));
            boots = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("boots.png")));
            fast = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("fast.png")));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public Item generateStatItem(int levelDepth, boolean hasPrice)
    {
        int healthModifier= (rand.nextInt(26)+10) + (levelDepth-1)*15;
        float rangeModifier = ((rand.nextInt(16)+5) + (levelDepth-1)*5 ) /100.f;
        float attackSpeedModifier=((rand.nextInt(11)+5) + (levelDepth-1)*3) /100.f;
        float damageModifier=((rand.nextInt(9)+2) + (levelDepth-1)*2) /100.f;
        float speedModifier=((rand.nextInt(9)+2) + (levelDepth-1)*2) /100.f;

        //formula for ValueRatio: actual value generated divided by average
        float rangeValueRatio =(rangeModifier*100.f/12.5f);
        float attackSpeedValueRatio =(attackSpeedModifier*100.f/10.f);
        float damageValueRatio =  (damageModifier*100.f/6.f);
        float speedValueRatio = (speedModifier/100.f/6.f);
        float healthValueRatio = (healthModifier/22.5f);

        //calculating which image to use
        float[] valueRatios = new float[5];
        valueRatios[0]=rangeValueRatio;
        valueRatios[1]=attackSpeedValueRatio;
        valueRatios[2]=damageValueRatio;
        valueRatios[3]=speedValueRatio;
        valueRatios[4]=healthValueRatio;

        float maxValueRatio = rangeValueRatio;
        int maxIndex = 0;
        for(int i=1;i<5;++i)
        {
            if(maxValueRatio<valueRatios[i])
            {
                maxValueRatio=valueRatios[i];
                maxIndex=i;
            }
        }
        Image statItemImage=null;

        //it bothers me how unreadable this came out to be,
        // but this is the only way to do this without importing libraries
        switch(maxIndex)
        {
            case 0:
                statItemImage = glasses;
                break;
            case 1:
                statItemImage = emeraldPendant;
                break;
            case 2:
                statItemImage = muscle;
                break;
            case 3:
                statItemImage=boots;
                break;
            case 4:
                statItemImage = rubyPendant;
                break;
            default:
                System.out.println("Something went horribly wrong!");
                break;
        }







        float priceMultiplier = (rangeValueRatio + damageValueRatio + attackSpeedValueRatio + speedValueRatio + healthValueRatio)/5;
        float price = (50 + (50 * (levelDepth-1)*0.4f) ) * priceMultiplier;


        int realPrice=0;
        if(hasPrice){ realPrice = Math.round(price);}

        return new StatItem(200,200,50,50,statItemImage,realPrice,"statItem", healthModifier, rangeModifier, attackSpeedModifier, damageModifier, speedModifier);
    }

    public Item generateWeapon(int levelDepth, boolean hasPrice)
    {
        int weaponRangeModifier = rand.nextInt(41)+50 + (levelDepth-1)*15;
        int weaponDamageModifier = rand.nextInt(11)+15 + (levelDepth-1)*8 ;
        float attackSpeedMultiplier = ((rand.nextInt(16)+10) + (levelDepth-1)*5)  /100.f;

        float weaponDamageValueRatio=(weaponRangeModifier/70.f);
        float weaponRangeValueRatio=(weaponDamageModifier/20.f);
        float attackSpeedValueRatio=(attackSpeedMultiplier*100/12.5f);

        float priceMultiplier = (weaponDamageValueRatio +  weaponRangeValueRatio + attackSpeedValueRatio )/3;
        float price = ( (50 + (50 * (levelDepth-1)*0.4f)) * priceMultiplier );

        float[] valueRatios = new float[3];
        valueRatios[0]=weaponRangeValueRatio;
        valueRatios[1]=attackSpeedValueRatio;
        valueRatios[2]=weaponDamageValueRatio;


        float maxValueRatio = weaponDamageValueRatio;
        int maxIndex = 0;
        for(int i=1;i<3;++i)
        {
            if(maxValueRatio<valueRatios[i])
            {
                maxValueRatio=valueRatios[i];
                maxIndex=i;
            }
        }
        Image weaponImage=null;

        //it bothers me how unreadable this came out to be,
        // but this is the only way to do this without importing libraries
        switch(maxIndex)
        {
            case 0:
                weaponImage = bow;
                break;
            case 1:
                weaponImage = fast;
                break;
            case 2:
                weaponImage = sword;
                break;
            default:
                System.out.println("Something went horribly wrong!");
                break;
        }


        int realPrice=0;
        if(hasPrice){ realPrice = Math.round(price);}
       return new Weapon(200, 400,50, 50,weaponImage,realPrice, "weapon" , weaponRangeModifier, weaponDamageModifier, attackSpeedMultiplier);
    }

    public Item generatePotion(int levelDepth, boolean hasPrice)
    {
        int healthRestore =0;
        int grantExp=0;
        float price=25+  25 * (levelDepth-1)*0.4f;
        Image potionImage=null;
        if(rand.nextBoolean())
        {
            healthRestore= rand.nextInt(31) +20 + ((levelDepth-1)*15) ;
            price *= healthRestore/35.f;
            potionImage=redPotion;
        }
        else
        {
            grantExp = rand.nextInt(51)+30 + ((levelDepth-1)*20) ;
            price *= grantExp/55.f;
            potionImage=bluePotion;

        }
        int realPrice=0;
        if(hasPrice){ realPrice = Math.round(price);}
        return new Potion(0,0,50,50,potionImage,realPrice, "potion", healthRestore, grantExp);
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
