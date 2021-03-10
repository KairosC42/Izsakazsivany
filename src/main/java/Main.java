import gui.MainWindow;
import levelLayoutGeneration.Coordinate;
import levelLayoutGeneration.Level;

public class Main
{
    public static void main(String[] args)
    {

        MainWindow gui = new MainWindow();

            new Level().generateLevel(3,true);


    }
}
