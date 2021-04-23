package com.csapat.rooms;

import java.util.Random;

public class BossRoom extends CombatRoom
{

    public BossRoom(int levelDepth)
    {
        super(levelDepth);
    }
    @Override
    public void roomSpecificGen()
    {
        Random rand = new Random();
        int n= rand.nextInt(N-4)+2;
        int m=rand.nextInt(M-4)+2;
        //layout[N][..] is out of bounds
        //layout[N-1] is a wall, shouldn't overwrite that
        //layout[0] is also a wall, so random has to start at 1 (: reason for +1)
        //N-3+1= N-2 is the first non wall floor
        this.layout[n][m]= Tile.TRAPDOOR_OPEN;
        //boss should be an enemy which is bigger with 10-20x the HP and 1-1.5x the damage of normal enemies
    }
}

