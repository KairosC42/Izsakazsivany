package levelLayoutGeneration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest
{

    @Test
    void getRoomMatrix()
    {
        Level level = new Level(1);
        assertNotNull(level.getRoomMatrix());
    }

    @Test
    void getStartingRoom()
    {
    }

    @Test
    void getNumberOfRooms()
    {
    }
}