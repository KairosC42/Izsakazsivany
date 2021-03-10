package rooms;

import entity.Enemy;
import entity.Sprite;

import java.util.Vector;

public abstract class Room
{
    Room()
    {
    }
    Room(int N, int M)
    {
        this.N = N;
        this.M = M;
    }
    int N, M;
    Tile[][] layout= new Tile[N][M]; // used for background, eg: walls, floor, doors
    Vector<Enemy> enemies;
    public abstract void roomSpecificGen();
    public void generateRoom()
    {
        for(int i=0;i<N;++i)
        {
            for(int j=0;j<M;++j)
            {
                if (i==0||j==0||i==N-1||j==M-1)layout[i][j]= Tile.WALL;
                else layout[i][j]= Tile.FLOOR;
                //NOTE: rooms should also have doors, but those are placed by generateLevel() found in Level
            }
        }
        roomSpecificGen();
    }

    Sprite[][] overlay= new Sprite[N][M]; //used for displaying over background, for sprites with variable positions



    //setters

    public void setLayout(Tile[][] layout)
    {
        this.layout = layout;
    }


    //getters


    public Tile[][] getLayout()
    {
        return layout;
    }
}

