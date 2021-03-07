import java.util.Random;
import java.util.Vector;


public class Shop extends Room
{
    private int itemCount;
    private Vector<Item> items= new Vector<Item>();
    @Override
    public void roomSpecificGen()
    {
        Random rand= new Random();
        itemCount=rand.nextInt(4)+2;
        for(int i=0;i<itemCount;++i)
        {
            //new random item from item pool
            //also a price!!
            //items[i]=..
        }
    }
    //how should shop items be displayed?
}
