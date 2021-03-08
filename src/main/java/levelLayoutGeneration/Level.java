package levelLayoutGeneration;


import java.util.Vector;

import java.util.Random;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

public class Level
{
    int levelDepth;
    Multigraph <RoomNode, DefaultEdge>rooms = new Multigraph<>(DefaultEdge.class);
    Vector<Pair> roomCords;
    Random rand = new Random();
    Vector<Integer> roomBiases;
    final int min=6;
    final int max=8;
    double sumOfBiases;
    int numberOfRooms;


    /*input:
    *   levelDepth: positive integer
    **/
    public void generateLevel(int levelDepth)
    {

        this.levelDepth=levelDepth;
        numberOfRooms=3*levelDepth + rand.nextInt((max - min) + 1) + min;
        int currentNumberOfRooms=numberOfRooms;

        //hasinlóképpen kéne a ciklusban
        RoomNode r=new RoomNode(0,0,4);
        rooms.addVertex(r);
        roomCords.add(new Pair(r.i,r.j));
        roomBiases.add(r.bias);
        sumOfBiases = roomBiases.stream().mapToInt(Integer::valueOf).sum();
        currentNumberOfRooms--;

        while( currentNumberOfRooms>0)
        {
            /*TODO: kiválasztani egy random szobát a biasok alapján és abból kiindulva létrehozni egy újat
                    a szomszédos szobák biasát megfelelően módosítani és a megfelelő éleket és csúcsokat létrehozni.
                    Minél több szomszédja van egy szobának annál rosszabb a biasa.
                    Egy szobának maximum négy szomszdéja lehet.

             */
            currentNumberOfRooms--;

        }

        /* TODO: Minden szoba távolságát lemérni és a legtávolabbitól a legközelebbifelé haladva a következő szobákat
                 helyezi el: Boss, Item, Shop, a többi combat szoba
                 Gondolat: A létrejött gráfot mátrixá alakítani, bár a gráf is tökéletesen működhet a szobák közti
                 közlekedésre


        */



    }

}
