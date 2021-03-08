package levelLayoutGeneration;
import org.jgrapht.*;
import org.jgrapht.graph.Multigraph;

import java.util.function.Supplier;

public class RoomNode
{
    int i;
    int j;
    int bias;
    int distanceFromStart;


    //bias is between 0 and 4
    public RoomNode(int i, int j,int bias)
    {
        this.i = i;
        this.j = j;
        this.bias = bias;
    }
}
