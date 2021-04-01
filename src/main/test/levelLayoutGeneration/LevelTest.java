package levelLayoutGeneration;

import org.junit.jupiter.api.Test;
import rooms.RoomType;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest
{

    @Test
    void getRoomMatrix()
    {
        Level level = new Level(1);
        RoomNode[][] roomNodes=level.getRoomMatrix();
        RoomNode s = level.getStartingRoom();
        assertNotNull(roomNodes);
        assertEquals(s,roomNodes[s.getCoordinate().i][s.getCoordinate().j]);
    }
    @Test
    void specialRooms()
    {
        int size[]={1,2,3,10,15,30};
        for (int k = 0; k <size.length ; k++)
        {
            Level level=new Level(size[k]);
            RoomNode[][] roomNodes = level.getRoomMatrix();
            int numberOfFoundRooms = 0;
            int numberOfBossRooms = 0;
            int numberOfItemRooms = 0;
            int numberOfShopRooms = 0;
            for (int i = 0; i < roomNodes.length; i++)
            {
                for (int j = 0; j < roomNodes[0].length; j++)
                {
                    if (roomNodes[i][j] != null)
                    {
                        if (roomNodes[i][j].roomType == RoomType.BOSSROOM)
                        {
                            numberOfBossRooms++;
                        }
                        if (roomNodes[i][j].roomType == RoomType.ITEMROOM)
                        {
                            numberOfItemRooms++;
                        }
                        if (roomNodes[i][j].roomType == RoomType.SHOP)
                        {
                            numberOfShopRooms++;
                        }
                        numberOfFoundRooms++;
                    }
                }

            }
            assertEquals(1, numberOfBossRooms);
            assertEquals(1, numberOfItemRooms);
            assertEquals(1, numberOfShopRooms);
            assertEquals(level.getNumberOfRooms(), numberOfFoundRooms);
        }
    }


    
    @Test
    void startingRoom()
    {
        Level level = new Level(1);
        RoomNode s = level.getStartingRoom();
        assertNotNull(s);
        assertEquals(RoomType.STARTROOM,s.getRoomType());
        assertEquals(0,s.getDistanceFromStart());

    }

}