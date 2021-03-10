package levelLayoutGeneration;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import java.util.Random;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

//
public class Level
{
    final int mSize=32;
    final int min=6;
    final int max=8;
    int numberOfRooms;
    Vector <RoomNode> roomVector=new Vector<RoomNode>();
    RoomNode[][] roomMatrix=new RoomNode[mSize][mSize];

    Random rand = new Random();




    public void generateLevel(int levelDepth)
    {
        //with levelDepth the number of rooms is increasing with the following formula: 3 * levelDepth + 6 to 8

        Arrays.fill(roomMatrix,null);
        numberOfRooms=3*levelDepth + rand.nextInt((max - min) + 1) + min;
        int currentNumberOfRooms=numberOfRooms;

        RoomNode startingRoom=new RoomNode(mSize/2+1,mSize/2+1,4);
        roomVector.add(startingRoom);
        roomMatrix[mSize/2+1][mSize/2+1]=startingRoom;
        currentNumberOfRooms--;

        while(currentNumberOfRooms>0)
        {


                currentNumberOfRooms--;
        }


    }



}
