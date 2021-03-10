package levelLayoutGeneration;
import org.jgrapht.*;
import org.jgrapht.graph.Multigraph;
import rooms.Room;


import java.util.Objects;
import java.util.function.Supplier;

public class RoomNode
{
    protected Coordinate coordinate;
    protected int bias;
    protected int distanceFromStart;
    protected Room room;




    //bias is between 0 and 4
    public RoomNode(int i, int j,int bias)
    {
        this.coordinate=new Coordinate(i,j);
        this.bias = bias;
    }

    public RoomNode(Coordinate cord,int bias)
    {
        this.coordinate=cord;
        this.bias = bias;
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

    public void setRoom(Room room)
    {
        this.room = room;
    }
}
