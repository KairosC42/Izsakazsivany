import gui.MainWindow;
import levelLayoutGeneration.Level;

public class Main
{
    public static void main(String[] args)
    {

        MainWindow gui = new MainWindow();

        Level level=new Level(1);
        level.getRoomMatrix()[level.getStartingRoom().getCoordinate().i][level.getStartingRoom().getCoordinate().i].getRoom();


    }
}
