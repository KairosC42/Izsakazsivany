package rooms;

import rooms.Room;

import java.util.Random;

public class CombatRoom extends Room
{
    Random rand= new Random();
    private int enemyCount=rand.nextInt(6)+3;
    @Override
    public void roomSpecificGen()
    {
        for(int i=0;i<enemyCount;++i)
        {

            //randomly generate enemy
            //choose random texture from enemy texture pool
        }
    }
    //does a combat room prevent you from leaving while enemies are alive?
    //if so, make that

}
