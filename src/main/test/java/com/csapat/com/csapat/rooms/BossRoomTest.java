package com.csapat.rooms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BossRoomTest {

    @Test
    void roomSpecificGen()
    {
        BossRoom bossRoom=new BossRoom(1);

        boolean hasTrapdoor=false;
        for(int i=0;i<bossRoom.getN();++i)
        {
            for (int j=0;j< bossRoom.getM();++j)
            {
                if(bossRoom.getLayout()[i][j]==Tile.TRAPDOOR_CLOSED)
                {
                    hasTrapdoor=true;
                    break;
                }
            }
        }

        assertTrue(hasTrapdoor);

    }
}