package rooms;

import entity.Item;
import rooms.Room;

import java.util.Random;
import java.util.Vector;

public class CombatRoom extends Room
{

    Random rand= new Random();
    private int enemyCount;

    public CombatRoom()
    {
        int enemyCount=rand.nextInt(4)+3;
        this.N=20;
        this.M=30;
        layout = new Tile[N][M];
        this.generateRoom();
        this.roomSpecificGen();
    }

    public CombatRoom(int N, int M, int levelDepth)
    {
        int enemyCount=rand.nextInt(4)+3;
        this.N=N;
        this.M=M;
        layout = new Tile[N][M];
        this.generateRoom();
        this.roomSpecificGen();
    }

    @Override
    public void roomSpecificGen()
    {
        for(int i=0;i<enemyCount;++i)
        {
            //todo
            //i have yet to think of how much HP they'll have and how the scaling will look
            //also the enemy class has yet to be made.
            //for now i'll finish roomgen

            //randomly generate enemy
            //choose random texture from enemy texture pool
        }
    }
    //does a combat room prevent you from leaving while enemies are alive?
    //if so, make that

}
