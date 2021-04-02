package levelLayoutGeneration;

import com.csapat.levelLayoutGeneration.*;
import com.csapat.rooms.*;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

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
                        if (roomNodes[i][j].getRoomType() == RoomType.BOSSROOM)
                        {
                            numberOfBossRooms++;
                        }
                        if (roomNodes[i][j].getRoomType() == RoomType.ITEMROOM)
                        {
                            numberOfItemRooms++;
                        }
                        if (roomNodes[i][j].getRoomType() == RoomType.SHOP)
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
    @Test
    void generateLevelTest()
    {
        Level level=new Level(1);
        RoomNode[][] levelLayout = new RoomNode[20][20];


        levelLayout[9][9]=new RoomNode(9,9,0);
        levelLayout[9][9].setRoomType(RoomType.STARTROOM);
        levelLayout[9][9].room = new StartRoom();

        levelLayout[8][9]=new RoomNode();
        levelLayout[8][9].setRoomType(RoomType.ITEMROOM);//above if im not mistaken

        levelLayout[9][10]=new RoomNode();
        levelLayout[9][10].setRoomType(RoomType.COMBATROOM); //to the right, i hope

        level.setRoomMatrix(levelLayout);
        Vector<RoomNode> roomVector = new Vector<>();
        roomVector.add(levelLayout[9][9]);
        level.setRoomVector(roomVector);
        level.setStartingRoom(levelLayout[9][9]);


        level.generateDoors();

        int n=20;
        int m=30;
        assertEquals(Tile.WALL,level.getStartingRoom().getRoom().getLayout()[n/2-1][0]);
        assertEquals(Tile.ITEMDOOR_OPEN,level.getStartingRoom().getRoom().getLayout()[0][m/2-1]);
        assertEquals(Tile.DOOR_OPEN,level.getStartingRoom().getRoom().getLayout()[n/2-1][m-1]);
        assertEquals(Tile.WALL,level.getStartingRoom().getRoom().getLayout()[n-1][m/2-1]);
    }

}