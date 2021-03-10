package levelLayoutGeneration;
import org.jgrapht.*;
import org.jgrapht.graph.Multigraph;


import java.util.function.Supplier;

public class RoomNode
{
    protected Coordinate coordinate;
    protected int bias;
    protected int distanceFromStart;
    protected Coordinate north=null;
    protected Coordinate east=null;
    protected Coordinate south=null;
    protected Coordinate west=null;



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
}
