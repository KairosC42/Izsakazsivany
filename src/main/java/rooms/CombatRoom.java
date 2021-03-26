package rooms;

import entity.Item;
import rooms.Room;

import java.util.Random;
import java.util.Vector;

/**
 * <h1>CombatRoom</h1>
 * contains 3-7 enemies, all of which have random stats that scale with levelDepth
 */
public class CombatRoom extends Room
{

    Random rand= new Random();
    private int enemyCount;


    public CombatRoom(int levelDepth)
    {
        super(levelDepth);
        int enemyCount=rand.nextInt(4)+3;
        this.roomSpecificGen();
    }

    @Override
    public void roomSpecificGen()
    {
        for(int i=0;i<enemyCount;++i)
        {
            int healthPoints = rand.nextInt(20)+50 + (levelDepth-1)*15 ;
            float moveSpeed = 3.2f + (levelDepth-1)*0.4f ;
            int visionRange = rand.nextInt(125)+25 + (levelDepth-1)*50; //enemies have full vision of every room from depth12
            int attackRange = rand.nextInt(20) + 40 + (levelDepth-1)*20 ;
            float damage = rand.nextInt(10)+20 +(levelDepth-1)*15;

            //todo
            //i have yet to think of how much HP they'll have and how the scaling will look
            //also the enemy class has yet to be made.



        }
    }
    //does a combat room prevent you from leaving while enemies are alive?
    //if so, make that

}
