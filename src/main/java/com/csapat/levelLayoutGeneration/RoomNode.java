package com.csapat.levelLayoutGeneration;

import com.csapat.rooms.Room;
import com.csapat.rooms.RoomType;
import java.util.Comparator;
import java.util.Objects;

/**
 * RoomNode is a class which store the necessary information about the Rooms for the Level generation algorithm
 * For out of package use you will only need the getter for the Room, RoomType, Coordinate fields
 *
 * @author Kovács Máté
 */


public class RoomNode implements Comparator<RoomNode>
{
    protected Coordinate coordinate;
    protected int bias;
    protected int distanceFromStart;
    public Room room;
    protected RoomType roomType;
    protected boolean visited=false;

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public Room getRoom()
    {
        return room;
    }

    //bias is between 0 and 4
    public RoomNode(int i, int j, int bias)
    {
        this.roomType=RoomType.NONE;
        this.coordinate=new Coordinate(i,j);
        this.bias = bias;
    }

    protected RoomNode(Coordinate cord,int bias)
    {
        this.coordinate=cord;
        this.bias = bias;
    }

    public RoomNode()
    {

    }

    public RoomType getRoomType()
    {
        return roomType;
    }

    public void setRoomType(RoomType roomType)
    {
        this.roomType = roomType;
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public int getDistanceFromStart()
    {
        return distanceFromStart;
    }

    public void setDistanceFromStart(int distanceFromStart)
    {
        this.distanceFromStart = distanceFromStart;
    }

    @Override
    public int compare(RoomNode o1, RoomNode o2)
    {
        return o2.distanceFromStart-o1.distanceFromStart;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomNode roomNode = (RoomNode) o;
        return coordinate.equals(roomNode.coordinate);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(coordinate);
    }


}