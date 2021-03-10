package levelLayoutGeneration;

import levelLayoutGeneration.bfs.Bfs;

import java.lang.Math;
import java.util.Arrays;
import java.util.Vector;

import java.util.Random;

public class Level
{
    //mSize must be a pair
    private final int mSize = 20;
    private final int min = 6;
    private final int max = 8;
    private int numberOfRooms;
    private Vector<RoomNode> roomVector;
    private RoomNode[][] roomMatrix;
    private RoomNode startingRoom;

    Random rand = new Random();

    public Level()
    {


    }


    //levelDepth is essentially dictates, the number os possible room in a level, by the following fromula: 3 * levelDepth + 6 to 8
    //must be a positive integer!!!
    // printOutLevel if true will print the layout of the level in the console, only for testing purposes
    public void generateLevel(int levelDepth, boolean printOutLevel)
    {
        roomVector = new Vector<>();
        roomMatrix = new RoomNode[mSize][mSize];

        numberOfRooms = 3 * levelDepth + rand.nextInt((max - min) + 1) + min;
        int currentNumberOfRooms = numberOfRooms;

        startingRoom = new RoomNode(mSize / 2 - 1, mSize / 2 - 1, 4);
        roomVector.add(startingRoom);
        roomMatrix[mSize / 2 - 1][mSize / 2 - 1] = startingRoom;

        currentNumberOfRooms--;

        while (currentNumberOfRooms > 0)
        {


            plcaeRoom(selectNextPlace());

            currentNumberOfRooms--;
        }



        Bfs.minDistance(roomMatrix,roomVector,startingRoom);

        if (printOutLevel)
        {
            System.out.println("\n\n\n\n\n");

            for (int i = 0; i < mSize; i++)
            { //this equals to the row in our matrix.
                for (int j = 0; j < mSize; j++)
                { //this equals to the column in each row.
                    if (roomMatrix[i][j] == null)
                    {
                        System.out.print("\u001B[40m" + "N" + " ");
                    } else
                    {
                        if (i == startingRoom.coordinate.i && j == startingRoom.coordinate.j)
                        {
                            System.out.print("\u001B[41m" + 'S' + " " + "\u001B[40m");
                        } else
                        {
                            System.out.print("\u001B[44m" + roomMatrix[i][j].distanceFromStart + " " + "\u001B[40m");
                        }

                    }

                }
                System.out.println(); //change line on console as row comes to end in the matrix.
            }

        }

    }

    private void plcaeRoom(Coordinate newRoomCoordinate)
    {
        int newRoomBias = 4;


        try
        {
            if (roomMatrix[newRoomCoordinate.i][newRoomCoordinate.j - 1] != null)
            {

                roomMatrix[newRoomCoordinate.i][newRoomCoordinate.j - 1].bias--;
                newRoomBias--;
            }

        } catch (IndexOutOfBoundsException e)
        {

            newRoomBias--;

        }

        try
        {
            if (roomMatrix[newRoomCoordinate.i - 1][newRoomCoordinate.j] != null)
            {
                roomMatrix[newRoomCoordinate.i - 1][newRoomCoordinate.j].bias--;
                newRoomBias--;
            }

        } catch (IndexOutOfBoundsException e)
        {
            newRoomBias--;
        }

        try
        {
            if (roomMatrix[newRoomCoordinate.i][newRoomCoordinate.j + 1] != null)
            {
                roomMatrix[newRoomCoordinate.i][newRoomCoordinate.j + 1].bias--;
                newRoomBias--;
            }

        } catch (IndexOutOfBoundsException e)
        {
            newRoomBias--;
        }

        try
        {
            if (roomMatrix[newRoomCoordinate.i + 1][newRoomCoordinate.j] != null)
            {
                roomMatrix[newRoomCoordinate.i + 1][newRoomCoordinate.j].bias--;
                newRoomBias--;
            }

        } catch (IndexOutOfBoundsException e)
        {
            newRoomBias--;
        }

        RoomNode newRoom = new RoomNode(newRoomCoordinate, newRoomBias);
        roomMatrix[newRoomCoordinate.i][newRoomCoordinate.j] = newRoom;
        roomVector.add(newRoom);


    }


    private Coordinate selectNextPlace()
    {
        int sumOfBiases = 0;
        //egyenlőre a biasok számítása elég sok feleseleges szorzást végez el
        //TODO Make it better
        //!!!!!kvadratikus  biasra való átállás, ha kiakarod kapcsolni írd át erre: sumOfBiases+=r.bias
        for (RoomNode r : roomVector)
        {
            sumOfBiases += (Math.pow(r.bias, 4));
        }
        int randBias = rand.nextInt(sumOfBiases) + 1;
        int prevBiasSum = 0;
        RoomNode selectedOriginRoomNode = null;
        //System.out.println("------randBias:" +randBias +"---------sumOfBias:" + sumOfBiases);
        for (int i = 0; i < roomVector.size(); i++)
        {
            //!!!!!kvadratikus biasra való átállás, ha kiakarod kapcsolni írd át erre: prevBiasSum+=roomVector.get(i).bia
            prevBiasSum += Math.pow(roomVector.get(i).bias, 4);
            if (prevBiasSum + roomVector.get(i).bias >= randBias)
            {
                selectedOriginRoomNode = roomVector.get(i);
                break;
            }
        }




        Vector<Coordinate> possiblePositions = new Vector<>();

        try
        {
            if (roomMatrix[selectedOriginRoomNode.coordinate.i][selectedOriginRoomNode.coordinate.j - 1] == null)
            {
                possiblePositions.add(new Coordinate(selectedOriginRoomNode.coordinate.i, selectedOriginRoomNode.coordinate.j - 1));
            }

        }
        catch (IndexOutOfBoundsException e )
        {
        }


        try
        {
            if (roomMatrix[selectedOriginRoomNode.coordinate.i - 1][selectedOriginRoomNode.coordinate.j] == null)
            {
                possiblePositions.add(new Coordinate(selectedOriginRoomNode.coordinate.i - 1, selectedOriginRoomNode.coordinate.j));
            }

        } catch (IndexOutOfBoundsException e)
        {
        }

        try
        {
            if (roomMatrix[selectedOriginRoomNode.coordinate.i][selectedOriginRoomNode.coordinate.j + 1] == null)
            {
                possiblePositions.add(new Coordinate(selectedOriginRoomNode.coordinate.i, selectedOriginRoomNode.coordinate.j + 1));
            }

        } catch (IndexOutOfBoundsException e)
        {
        }

        try
        {
            if (roomMatrix[selectedOriginRoomNode.coordinate.i + 1][selectedOriginRoomNode.coordinate.j] == null)
            {
                possiblePositions.add(new Coordinate(selectedOriginRoomNode.coordinate.i + 1, selectedOriginRoomNode.coordinate.j));
            }

        } catch (IndexOutOfBoundsException e)
        {
        }


        //System.out.println(possiblePositions.size());


        return possiblePositions.get(rand.nextInt(possiblePositions.size()));
    }




}
