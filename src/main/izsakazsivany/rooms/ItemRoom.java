package rooms;

import entity.Item;
import entity.ItemGenerator;
import entity.StatItem;
import entity.Weapon;
import rooms.Room;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.util.Random;

import java.util.Random;
import java.util.Vector;

/**
 * <h1>ItemRoom class</h1>
 * Is next to spawn
 * contains 2 items - 1 statitem and 1 weapon
 * these have randomly generated stats scaling with levelDepth
 * uses ItemGenerator for creating its items
 * size is 20*30 default
 *
 * @author Ballai András
 */
public class ItemRoom extends Room
{
    private Item statItem;

    private Item weapon;

    private ItemGenerator generator;


    public ItemRoom(int levelDepth)
    {
        super(levelDepth);
        this.generator=new ItemGenerator();
        this.roomSpecificGen();
    }


    /*
    Placeholder method!
     */

    @Override
    public void roomSpecificGen()
    {
       //for actual item generation see ItemGenerator
        this.statItem = generator.generateStatItem(levelDepth, false);

        this.weapon = generator.generateWeapon(levelDepth,false);
    }

    //getters


    public Item getStatItem() {
        return statItem;
    }

    public Item getWeapon() {
        return weapon;
    }
}