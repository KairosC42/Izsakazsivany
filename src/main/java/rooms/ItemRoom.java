package rooms;

import entity.Item;
import entity.StatItem;
import entity.Weapon;
import rooms.Room;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.util.Random;

import java.util.Random;
import java.util.Vector;

public class ItemRoom extends Room
{
    private StatItem statItem;

    private Weapon weapon;

    public ItemRoom()
    {
        this.N=20;
        this.M=30;
        layout = new Tile[N][M];
        this.generateRoom();
        this.roomSpecificGen();
    }

    public ItemRoom(int N, int M, int levelDepth)
    {
        this.N=N;
        this.M=M;
        layout = new Tile[N][M];
        this.generateRoom();
        this.roomSpecificGen();
    }


    /*
    Placeholder method!
     */

    @Override
    public void roomSpecificGen()
    {
        Random r = new Random();
        //int weaponImageIndex = r.nextInt(weaponImages.size()); //idea: randomly assign texture, item texture pool isn't in place yet however.
        //sItemIndex = r.nextInt(itemImages.size());
        //int nameIndex = r.nextInt(itemNames.size()); //similar idea, come up with a String[] of weapon names.

        //first the statitem
        Image i = null;
        try{
           i = ImageIO.read(this.getClass().getClassLoader().getResource("ruby_pendant.jpg"));
        }
        catch (Exception e)
        {
            System.out.println("Missing texture!");
        }
        int healthModifier=r.nextInt(25)+10;
        float rangeModifier = (r.nextInt(15)+5)/100.f;
        float attackSpeedModifier=(r.nextInt(10)+5)/100.f;
        float damageModifier=(r.nextInt(8)+2)/100.f;
        float speedModifier=(r.nextInt(8)+2)/100.f;

        this.statItem=new StatItem(0,0,50,50,i,0,"pendant", healthModifier, rangeModifier, attackSpeedModifier, damageModifier, speedModifier);

        //now for the weapon

        int weaponRangeModifier = r.nextInt(150)+200;
        int weaponDamageModifier = r.nextInt(10)+15 ;
        float attackSpeedMultiplier = (r.nextInt(20)+10/100.f);
        try{
            i = ImageIO.read(this.getClass().getClassLoader().getResource("sword.jpg"));
        }
        catch (Exception e)
        {
            System.out.println("Missing texture!");
        }
        this.weapon= new Weapon(0, 0,50, 50,i,0, "sword" , weaponRangeModifier, weaponDamageModifier, attackSpeedMultiplier);

    }
}
