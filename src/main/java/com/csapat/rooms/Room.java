package com.csapat.rooms;

import com.csapat.entity.Enemy;
import com.csapat.entity.Sprite;

import java.util.Vector;


/**
 * <h1>Room class</h1>
 * Creates a new N*M level, wall tiles all along the perimeter, floor everywhere else
 * The following classes extend Room:
 *               Shop Room: Place to buy new Items, Weapons, Potions
 *               Item room: Contains one random Item and one random Weapon
 *               Combat room: Contains 3-7 enemies, upon entry the doors are locked,
 *                            they only get unlocked once all enemies perish.
 *                            Dead enemies drop experience and money, can drop Items and Weapons.
 *               Each of these com.csapat.rooms have their own special rules, which they act out by overriding the roomSpecificGen() method
 *               contains a boolean to see it has been visited already, maintained outside of this class
 *               levelDepth is passed down (probably from main), will be used to create more dangerous enemies and better items.
 *
 * @author  Ballai András
 */

public abstract class Room
{
    int N, M;
    public boolean visited;
    int levelDepth;
    Tile[][] layout; // used for background, eg: walls, floor, doors
    Vector<Enemy> enemies;
    //could be private with setters and getters, but room is quite far down, for ease of access its public.
    public Room()
    {

        N = 20;
        M = 30;
        layout = new Tile[N][M]; // used for background, eg: walls, floor, doors
        generateRoom();
    }
    public Room(int levelDepth)
    {
        this.N =20;
        this.M = 30;
        layout = new Tile[N][M];
        this.visited=false;
        this.levelDepth=levelDepth;
        generateRoom();
    }


    public abstract void roomSpecificGen();
    public void generateRoom()
    {
        for(int i=0;i<N;++i)
        {
            for(int j=0;j<M;++j)
            {
                if (i==0||j==0||i==N-1||j==M-1)layout[i][j]= Tile.WALL;
                else layout[i][j]= Tile.FLOOR;
                //NOTE: com.csapat.rooms should also have doors, but those are placed by generateLevel() found in Level
            }
        }
    }

    Sprite[][] overlay= new Sprite[N][M]; //used for displaying over background, for sprites with variable positions


    public void printRoom()
    {
        for (int i=0;i<N;++i)
        {
            String line="";
            for (int j=0;j<M;++j)
            {
                switch(layout[i][j])
                {
                    case FLOOR:
                        line+="q ";
                        break;
                    case WALL:
                        line+="| ";
                        break;
                    case DOOR_CLOSED:
                        line+="_ ";
                        break;
                    case DOOR_OPEN:
                        line+="A ";
                        break;
                    case BOSSDOOR_CLOSED:
                        line+="BC";
                        break;
                    case SHOPDOOR_CLOSED:
                        line+="SC";
                        break;
                    case ITEMDOOR_CLOSED:
                        line+="IC";
                        break;
                    case BOSSDOOR_OPEN:
                        line+="BO";
                        break;
                    case SHOPDOOR_OPEN:
                        line+="SO";
                        break;
                    case ITEMDOOR_OPEN:
                        line+="IO";
                        break;
                    case TRAPDOOR_CLOSED:
                        line+="x ";
                        break;
                    case TRAPDOOR_OPEN:
                        line+="O ";
                        break;
                    default:
                        line+="T ";
                        break;
                }

            }
            System.out.println(line);
            line="";

        }
    }

    //setters

    public void setLayout(Tile[][] layout)
    {
        this.layout = layout;
    }


    //getters


    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public Tile[][] getLayout()
    {
        return layout;
    }
}

