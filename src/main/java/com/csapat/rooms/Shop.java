package com.csapat.rooms;

import com.csapat.entity.*;

import java.util.Random;
import java.util.Vector;

/**
 * <h1>Shop class</h1>
 * contains a random amount of items (between 3 and 6)
 * some of these will be statItems, some will be Weapons, the rest are potions
 * generates its items via ItemGenerator
 *
 * @author Ballai András
 */


//todo: when colliding with a shop item (determined by price), purchase it
    //also needed: item stats display
    //also needed: item positioning in shops
public class Shop extends Room
{
    private int itemCount;
    private Vector<Item> items= new Vector<Item>();
    private ItemGenerator generator;


    public Shop(int levelDepth)
    {
        super(levelDepth);
        this.generateRoom();
        this.items= new Vector<Item>();
        this.generator=new ItemGenerator();
        this.roomSpecificGen();
    }

    @Override
    public void roomSpecificGen()
    {

        this.items= new Vector<Item>();
        Random rand= new Random();
        itemCount=rand.nextInt(3)+3;
        int statItemCount = rand.nextInt((itemCount/2))+1;
        int weaponCount   = rand.nextInt(itemCount-statItemCount-1)+1;
        int potionCount = itemCount -statItemCount - weaponCount;
        for(int i=0;i<statItemCount;++i)
        {
            items.add( generator.generateStatItem(levelDepth, true ));
        }
        for (int i=0;i<weaponCount;++i)
        {
            items.add( generator.generateWeapon(levelDepth,true) );
        }
        for (int i=0;i<potionCount;++i)
        {
            items.add(generator.generatePotion(levelDepth, true));
        }
    }
    //getters

    public Vector<Item> getItems() {
        return items;
    }


    //how should shop items be displayed?
}
