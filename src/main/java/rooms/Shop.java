package rooms;

import entity.Item;
import entity.StatItem;
import entity.Weapon;
import entity.Potion;
import rooms.Room;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.util.Random;
import java.util.Vector;


public class Shop extends Room
{
    private int itemCount;
    private Vector<Item> items= new Vector<Item>();

    public Shop()
    {
        this.N=20;
        this.M=30;
        this.items= new Vector<Item>();
        this.layout = new Tile[N][M];
        this.generateRoom();
        this.roomSpecificGen();
    }

    public Shop(int N, int M, int levelDepth)
    {
        this.N=N;
        this.M=M;
        this.items= new Vector<Item>();
        this.layout = new Tile[N][M];
        this.generateRoom();
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
            Image im = null;
            try{
                im = ImageIO.read(this.getClass().getClassLoader().getResource("ruby_pendant.jpg"));
            }
            catch (Exception e)
            {
                System.out.println("Missing texture!");
            }
            int healthModifier=rand.nextInt(25)+10;
            float rangeModifier = (rand.nextInt(15)+5)/100.f;
            float attackSpeedModifier=(rand.nextInt(10)+5)/100.f;
            float damageModifier=(rand.nextInt(8)+2)/100.f;
            float speedModifier=(rand.nextInt(8)+2)/100.f;

            float price = (50 * (rangeModifier*100.f/12.5f) * (damageModifier*100.f/6.f) * (attackSpeedModifier*100.f/10.f) * (speedModifier/100.f)/6.f * (healthModifier/22.5f) );
            int realPrice = Math.round(price);
            items.add(new StatItem(0,0,50,50,im,realPrice,"pendant", healthModifier, rangeModifier, attackSpeedModifier, damageModifier, speedModifier));
        }
        for (int i=0;i<weaponCount;++i)
        {
            int weaponRangeModifier = rand.nextInt(150)+200;
            int weaponDamageModifier = rand.nextInt(10)+15 ;
            float attackSpeedMultiplier = (rand.nextInt(20)+10/100.f);
            float price = (50 * (weaponRangeModifier/275.f) * (weaponDamageModifier/20.f) * (attackSpeedMultiplier*100/20.0f) );
            int realPrice = Math.round(price);
                //price: 50 is the base value multiplied by how good the attributes are, an average roll is a 1.0 multiplier on the price.
            Image im = null;
            try{
                im = ImageIO.read(this.getClass().getClassLoader().getResource("sword.jpg"));
            }
            catch (Exception e) {
                System.out.println("Missing texture!");
            }
            items.add(new Weapon(0, 0,50, 50,im,realPrice, "sword" , weaponRangeModifier, weaponDamageModifier, attackSpeedMultiplier));
        }
        for (int i=0;i<potionCount;++i)
        {
            int healthRestore =0;
            int grantExp=0;
            float price=25;
            if(rand.nextBoolean())
            {
                healthRestore= rand.nextInt(30)+20;
                price *= healthRestore/35;
            }
            else
            {
                grantExp = rand.nextInt(50)+30;
                price *= grantExp/55;

            }
            Image im = null;
            try{
                im = ImageIO.read(this.getClass().getClassLoader().getResource("red_potion.jpg"));
            }
            catch (Exception e) {
                System.out.println("Missing texture!");
            }
            int realPrice = Math.round(price);
            items.add(new Potion(0,0,50,50,im,realPrice, "potion", healthRestore, grantExp));
        }
    }
    //how should shop items be displayed?
}
