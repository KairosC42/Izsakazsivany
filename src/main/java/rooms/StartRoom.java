package rooms;

import java.util.Random;

//safe room without enemy, this is where the player character starts the game
public class StartRoom extends Room
{

    public StartRoom()
    {
        super();
        roomSpecificGen();
    }
    @Override
    public void roomSpecificGen()
    {
/*
        //!This is only for testing, otherwise StartRoom's roomSpecificGen is empty!!
        //only for testing trapdoor spawn location, rendering and level transition
        Random rand = new Random();
        int n= rand.nextInt(N-4)+2;
        int m=rand.nextInt(M-4)+2;
        //layout[N][..] is out of bounds
        //layout[N-1] is a wall, shouldn't overwrite that
        //layout[0] is also a wall, so random has to start at 1 (: reason for +1)
        //N-3+1= N-2 is the first non wall floor
        this.layout[n][m]= Tile.TRAPDOOR_OPEN;

 */
    }
}
