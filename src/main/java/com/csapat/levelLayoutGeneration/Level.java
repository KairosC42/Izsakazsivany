package com.csapat.levelLayoutGeneration;

import com.csapat.levelLayoutGeneration.bfs.Bfs;
import com.csapat.rooms.*;
import java.lang.Math;
import java.util.Vector;
import java.util.Random;


/**
 * Level class
 * Level Generator class. Generate a level layout with placed down com.csapat.rooms.
 * Also dictates place of the special com.csapat.rooms:
 *               Boss room: furthest from start room
 *               Shop Room: second furthest from the start room
 *               Item room: closest to the start room
 *               Combat room: any other room
 *
 * The generated room layout is in a RoomNode[][] matrix. You can access it with the getRoomMatrix() method
 * For the starting room you can use the getStartingRoom() method
 *
 *
 *
 * @author  Kovács Máté
 */

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
    private Random rand = new Random();
    private int levelDepth;





    /**
     * The constructor will automatically generate a level.
     *
     * @param levelDepth The depth of the current level eq. 1st floor, 2nd floor, 3rd flot etc.
     *                   The number must be a positive integer
     *
     */
    public Level(int levelDepth)
    {
        this.levelDepth=levelDepth;
        generateLevel(levelDepth);
    }



    public void generateDoors()
    {
        for(RoomNode room : roomVector )
        {
            int i = room.getCoordinate().i;
            int j = room.getCoordinate().j;

            Tile[][] layout;
            Tile type = Tile.DOOR_OPEN;
            int N = room.getRoom().getN();
            int M = room.getRoom().getM();

            //northern neighbour:
            try
            {
                if(roomMatrix[i-1][j]!=null)
                {
                    switch (roomMatrix[i-1][j].getRoomType())
                    {
                        case BOSSROOM:
                            type = Tile.BOSSDOOR_OPEN;
                            break;
                        case SHOP:
                            type = Tile.SHOPDOOR_OPEN;
                            break;
                        case ITEMROOM:
                            type = Tile.ITEMDOOR_OPEN;
                            break;
                        default:
                            type = Tile.DOOR_OPEN;
                            break;
                    }
                    layout=room.getRoom().getLayout();
                    layout[0][M/2-1] = type;
                    roomMatrix[i][j].getRoom().setLayout(layout);
                    room.getRoom().setLayout(layout);
                }
            }
            catch(IndexOutOfBoundsException e){}

            //southern neighbour
            try
            {
                if(roomMatrix[i+1][j]!=null)
                {
                    switch (roomMatrix[i+1][j].getRoomType())
                    {
                        case BOSSROOM:
                            type = Tile.BOSSDOOR_OPEN;
                            break;
                        case SHOP:
                            type = Tile.SHOPDOOR_OPEN;
                            break;
                        case ITEMROOM:
                            type = Tile.ITEMDOOR_OPEN;
                            break;
                        default:
                            type = Tile.DOOR_OPEN;
                            break;
                    }

                    layout=room.getRoom().getLayout();
                    layout[N-1][M/2-1] = type;
                    roomMatrix[i][j].getRoom().setLayout(layout);
                    room.getRoom().setLayout(layout);
                }
            }
            catch(IndexOutOfBoundsException e){}

            //eastern neighbour
            try
            {
                if(roomMatrix[i][j+1]!=null)
                {
                    switch (roomMatrix[i][j+1].getRoomType())
                    {
                        case BOSSROOM:
                            type = Tile.BOSSDOOR_OPEN;
                            break;
                        case SHOP:
                            type = Tile.SHOPDOOR_OPEN;
                            break;
                        case ITEMROOM:
                            type = Tile.ITEMDOOR_OPEN;
                            break;
                        default:
                            type = Tile.DOOR_OPEN;
                            break;
                    }
                    layout=room.getRoom().getLayout();
                    layout[N/2-1][M-1] = type;
                    roomMatrix[i][j].getRoom().setLayout(layout);
                    room.getRoom().setLayout(layout);
                }
            }
            catch(IndexOutOfBoundsException e){}

            //western neighbour
            try
            {
                if(roomMatrix[i][j-1]!=null)
                {
                    switch (roomMatrix[i][j-1].getRoomType())
                    {
                        case BOSSROOM:
                            type = Tile.BOSSDOOR_OPEN;
                            break;
                        case SHOP:
                            type = Tile.SHOPDOOR_OPEN;
                            break;
                        case ITEMROOM:
                            type = Tile.ITEMDOOR_OPEN;
                            break;
                        default:
                            type = Tile.DOOR_OPEN;
                            break;
                    }
                    layout=room.getRoom().getLayout();
                    layout[N/2-1][0] = type;
                    roomMatrix[i][j].getRoom().setLayout(layout);
                    room.getRoom().setLayout(layout);
                }
            }
            catch(IndexOutOfBoundsException e){}

        }
    }

    /**
     * If you want to generate a new level invoke this method
     *
     * @param levelDepth The depth of the current level eq. 1st floor, 2nd floor, 3rd flot etc.
     *                   The number must be a positive integer
     *
     * The number of com.csapat.rooms will be: 3 * levelDepth + 6 to 8
     */
    public void generateLevel(int levelDepth)
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
            placeRoom(selectNextPlace());
            currentNumberOfRooms--;
        }



        Bfs.minDistance(roomMatrix,roomVector,startingRoom);
        placeSpecialRooms();



        //A base room layout contains no doors, as that depends on the level layout
        //which a room cannot access from within itself, so doors are created here
        /*
            process:
                -iterate roomVector
                -for every room in roomVector assign a door based on type of room its next to, or nothing in case of null.
                -checking the type of adjacent room is done via the switch-cases
            sadly contains a lot of duped code, but it's not worth making it a function as its single use
            for now at least xd
         */
        generateDoors();
        shrinkLevelMatrix();

    }

    private void placeRoom(Coordinate newRoomCoordinate)
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
        Vector<Coordinate> possiblePositions = new Vector<>();
        while(possiblePositions.size()==0)
        {
            Bfs.minDistance(roomMatrix, roomVector, startingRoom);
            int sumOfBiases = 0;
            //egyenlőre a biasok számítása elég sok feleseleges szorzást végez el
            //TODO Make it better
            //!!!!!kvadratikus  biasra való átállás, ha kiakarod kapcsolni írd át erre: sumOfBiases+=r.bias
            for (RoomNode r : roomVector)
            {
                sumOfBiases += (Math.pow(r.bias, 4) + Math.pow(r.distanceFromStart, 2));
            }
            int randBias = rand.nextInt(sumOfBiases) + 1;
            int prevBiasSum = 0;
            RoomNode selectedOriginRoomNode = null;
            //System.out.println("------randBias:" +randBias +"---------sumOfBias:" + sumOfBiases);
            for (RoomNode r : roomVector)
            {
                //!!!!!kvadratikus biasra való átállás, ha kiakarod kapcsolni írd át erre: prevBiasSum+=roomVector.get(i).bia
                prevBiasSum += (Math.pow(r.bias, 4) + Math.pow(r.distanceFromStart, 2));
                if (prevBiasSum + r.bias >= randBias)
                {

                    selectedOriginRoomNode = r;
                    break;
                }

            }


            possiblePositions = new Vector<>();

            try
            {
                if (roomMatrix[selectedOriginRoomNode.coordinate.i][selectedOriginRoomNode.coordinate.j - 1] == null)
                {
                    possiblePositions.add(new Coordinate(selectedOriginRoomNode.coordinate.i, selectedOriginRoomNode.coordinate.j - 1));
                }

            } catch (IndexOutOfBoundsException e)
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


        }
        return possiblePositions.get(rand.nextInt(possiblePositions.size()));
    }

    public void printLevel()
    {
        System.out.println("\n\n\n");

        for (int i = 0; i < roomMatrix.length; i++)
        { //this equals to the row in our matrix.
            for (int j = 0; j < roomMatrix[0].length; j++)
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
        System.out.println("\nDistances from the start room");
        System.out.println("\n\n\n");

        for (int i = 0; i < roomMatrix.length; i++)
        { //this equals to the row in our matrix.
            for (int j = 0; j < roomMatrix[0].length; j++)
            { //this equals to the column in each row.
                if (roomMatrix[i][j] == null)
                {
                    System.out.print("\u001B[40m" + "N" + " ");
                } else
                {
                    if (roomMatrix[i][j].roomType == RoomType.COMBATROOM)
                    {
                        System.out.print("\u001B[45m" + "C" + " " + "\u001B[40m");
                    }
                    if(roomMatrix[i][j].roomType == RoomType.ITEMROOM)
                    {
                        System.out.print("\u001B[103m" + "I" + " " + "\u001B[40m");
                    }
                    if(roomMatrix[i][j].roomType == RoomType.SHOP)
                    {
                        System.out.print("\u001B[106m" + "S" + " " + "\u001B[40m");
                    }
                    if(roomMatrix[i][j].roomType == RoomType.BOSSROOM )
                    {
                        System.out.print("\u001B[41m" + "B" + " " + "\u001B[40m");
                    }
                    if(roomMatrix[i][j].roomType == RoomType.STARTROOM)
                    {
                        System.out.print("\u001B[42m" + "O" + " " + "\u001B[40m");
                    }
                }
            }
            System.out.println(); //change line on console as row comes to end in the matrix.
        }
        System.out.println("\u001B[42m" +"\u001B[30m" +"O = Start Room"+ "\u001B[40m");
        System.out.println("\u001B[103m" + "I = Item Room is the closest to the start room"+ "\u001B[40m");
        System.out.println("\u001B[106m" + "S = Shop Room is the second furthest from the start room"+ "\u001B[40m");
        System.out.println("\u001B[41m" + "B = Boss Room is the furthest from the start room"+ "\u001B[40m");
        System.out.println("\u001B[45m" + "C = Combat Room is all the other not special room"+ "\u001B[40m");
        System.out.println("\u001B[0m");

    }

    public void printLevelWithPlayerPos(RoomNode currentRoomNode)
    {
        System.out.println("\n\n\n");

        for (int i = 0; i < roomMatrix.length; i++)
        { //this equals to the row in our matrix.
            for (int j = 0; j < roomMatrix[0].length; j++)
            { //this equals to the column in each row.
                if (roomMatrix[i][j] == null)
                {
                    System.out.print("\u001B[40m" + "N" + " ");
                } else
                {
                    if(i == currentRoomNode.getCoordinate().i && j==currentRoomNode.getCoordinate().j)
                    {
                        System.out.print("\u001B[44m" + "P" + " " + "\u001B[40m");
                    }
                    else if (roomMatrix[i][j].roomType == RoomType.COMBATROOM)
                    {
                        System.out.print("\u001B[45m" + "C" + " " + "\u001B[40m");
                    }
                    else if(roomMatrix[i][j].roomType == RoomType.ITEMROOM)
                    {
                        System.out.print("\u001B[103m" + "I" + " " + "\u001B[40m");
                    }
                    else if(roomMatrix[i][j].roomType == RoomType.SHOP)
                    {
                        System.out.print("\u001B[106m" + "S" + " " + "\u001B[40m");
                    }
                    else if(roomMatrix[i][j].roomType == RoomType.BOSSROOM )
                    {
                        System.out.print("\u001B[41m" + "B" + " " + "\u001B[40m");
                    }
                    else if(roomMatrix[i][j].roomType == RoomType.STARTROOM)
                    {
                        System.out.print("\u001B[42m" + "O" + " " + "\u001B[40m");
                    }

                }
            }
            System.out.println(); //change line on console as row comes to end in the matrix.
        }
    }

    private void placeSpecialRooms()
    {
        roomVector.sort(new RoomNode());

        for (int i = 0; i < roomVector.size(); i++)
        {
            if(!(i == 0||i == 1|| i == roomVector.size()-2|| i == roomVector.size()-1))
            {
                CombatRoom r=new CombatRoom(levelDepth);
                roomVector.get(i).room = r;
                roomVector.get(i).roomType=RoomType.COMBATROOM;
                roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].room=r;
                roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].roomType=RoomType.COMBATROOM;

            }
            else
            {
                if (i == 0)
                {
                    BossRoom r = new BossRoom(levelDepth);
                    roomVector.get(i).room = r;
                    roomVector.get(i).roomType=RoomType.BOSSROOM;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].room = r;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].roomType=RoomType.BOSSROOM;
                }
                if (i == roomVector.size()-2)
                {
                    ItemRoom r = new ItemRoom(levelDepth);
                    roomVector.get(i).room = r;
                    roomVector.get(i).roomType=RoomType.ITEMROOM;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].room = r;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].roomType=RoomType.ITEMROOM;
                }
                if (i == 1)
                {
                    Shop r = new Shop(levelDepth);
                    roomVector.get(i).room = r;
                    roomVector.get(i).roomType=RoomType.SHOP;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].room = r;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].roomType=RoomType.SHOP;
                }
                if (i == roomVector.size()-1)
                {
                    StartRoom r = new StartRoom();
                    roomVector.get(i).room = r;
                    roomVector.get(i).roomType=RoomType.STARTROOM;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].room = r;
                    roomMatrix[roomVector.get(i).coordinate.i][roomVector.get(i).coordinate.j].roomType=RoomType.STARTROOM;
                }

            }
        }

    }

    /**
     * Warning after this method is called the roomVector is no longer usable,
     * because the RoomNode coordinates are different!
     *
     * Shrink down the RoomMatrix for easier use and display
     */

    private void shrinkLevelMatrix()
    {
        Coordinate north=null;
        Coordinate east=null;
        Coordinate south=null;
        Coordinate west=null;
        boolean found=false;
        for (int i=0;i<roomMatrix.length;i++)
        {
            for (int j=0;j<roomMatrix[0].length;j++)
            {
                if(roomMatrix[i][j]!=null)
                {
                    north=roomMatrix[i][j].getCoordinate();
                    found=true;
                    break;
                }
            }
            if (found) break;;
        }

        found=false;
        for (int j=0;j<roomMatrix[0].length;j++)
        {
            for (int i=0;i<roomMatrix.length;i++)
            {
                if(roomMatrix[i][j]!=null)
                {
                    west=roomMatrix[i][j].getCoordinate();
                    found=true;
                    break;
                }
            }
            if (found) break;;
        }

        found=false;
        for (int i=roomMatrix.length-1;i>=0;i--)
        {
            for (int j=roomMatrix[0].length-1;j>=0;j--)
            {
                if(roomMatrix[i][j]!=null)
                {
                    south=roomMatrix[i][j].getCoordinate();
                    found=true;
                    break;
                }
            }
            if (found) break;;
        }

        found=false;
        for (int j=roomMatrix[0].length-1;j>=0;j--)
        {
            for (int i=roomMatrix.length-1;i>=0;i--)
            {
                if(roomMatrix[i][j]!=null)
                {
                    east=roomMatrix[i][j].getCoordinate();
                    found=true;
                    break;
                }
            }
            if (found) break;;
        }


        //System.out.println(north);
        //System.out.println(east);
        //System.out.println(south);
        //System.out.println(west);
        //System.out.println("i range: "+north.i+" to: "+south.i);
        //System.out.println("j range: "+west.j+" to: "+east.j);
        RoomNode[][] newRoomMatrix=new RoomNode[(south.i-north.i)+1][(east.j-west.j)+1];

        int k=0;
        int l;
        for (int i = north.i; i <=south.i ; i++)
        {
            l=0;
            for (int j = west.j; j <=east.j ; j++)
            {
                newRoomMatrix[k][l]=roomMatrix[i][j];
                if(newRoomMatrix[k][l]!=null)
                {


                    newRoomMatrix[k][l].coordinate=new Coordinate(k,l);
                    if(newRoomMatrix[k][l].roomType==RoomType.STARTROOM)
                    {
                        System.out.println("start");
                        startingRoom=newRoomMatrix[k][l];
                        System.out.println(startingRoom.getCoordinate());
                    }
                }


                l++;
            }
            k++;
        }
        roomMatrix=newRoomMatrix;



        System.out.println(startingRoom.coordinate);



    }


    public RoomNode[][] getRoomMatrix()
    {
        return roomMatrix;
    }

    public RoomNode getStartingRoom()
    {
        return startingRoom;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setRoomMatrix(RoomNode[][] roomMatrix){this.roomMatrix=roomMatrix;}

    public void setRoomVector(Vector<RoomNode> roomVector){this.roomVector=roomVector;}

    public void setStartingRoom(RoomNode startingRoom){this.startingRoom=startingRoom;}
}
