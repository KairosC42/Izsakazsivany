package levelLayoutGeneration;

import rooms.RoomType;
import rooms.StartRoom;
import rooms.Tile;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @org.junit.jupiter.api.Test
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
        System.out.println(level.getStartingRoom().getRoom().getLayout()[0][m/2-1]);
        assertEquals(Tile.DOOR_OPEN,level.getStartingRoom().getRoom().getLayout()[n/2-1][m-1]);
        assertEquals(Tile.WALL,level.getStartingRoom().getRoom().getLayout()[n-1][m/2-1]);
    }
}