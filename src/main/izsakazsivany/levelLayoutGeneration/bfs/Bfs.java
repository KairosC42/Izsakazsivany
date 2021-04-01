package levelLayoutGeneration.bfs;

import levelLayoutGeneration.RoomNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * Modification of the following algorithm: https://www.geeksforgeeks.org/shortest-distance-two-cells-matrix-grid/
 *
 * @author Kovács Máté
 */


public class Bfs
{
    public static void minDistance(RoomNode[][] grid, Vector<RoomNode> roomVector, RoomNode startingRoom)
    {
        QItem source = new QItem(0, 0, 0);
        source.row=startingRoom.getCoordinate().i;
        source.col=startingRoom.getCoordinate().j;
        startingRoom.setDistanceFromStart(0);


        // applying BFS on matrix cells starting from source
        Queue<QItem> queue = new LinkedList<>();
        queue.add(new QItem(source.row, source.col, 0));

        boolean[][] visited
                = new boolean[grid.length][grid[0].length];
        visited[source.row][source.col] = true;

        while (queue.isEmpty() == false) {
            QItem p = queue.remove();


            // moving up
            if (isValid(p.row - 1, p.col, grid, visited)) {
                queue.add(new QItem(p.row - 1, p.col,
                        p.dist + 1));
                visited[p.row - 1][p.col] = true;
                grid[p.row - 1][p.col].setDistanceFromStart(p.dist+1);
            }

            // moving down
            if (isValid(p.row + 1, p.col, grid, visited)) {
                queue.add(new QItem(p.row + 1, p.col,
                        p.dist + 1));
                visited[p.row + 1][p.col] = true;
                grid[p.row + 1][p.col].setDistanceFromStart(p.dist+1);
            }

            // moving left
            if (isValid(p.row, p.col - 1, grid, visited)) {
                queue.add(new QItem(p.row, p.col - 1,
                        p.dist + 1));
                visited[p.row][p.col - 1] = true;

                grid[p.row][p.col - 1].setDistanceFromStart(p.dist+1);
            }

            // moving right
            if (isValid(p.row, p.col + 1, grid,
                    visited)) {
                queue.add(new QItem(p.row, p.col + 1,
                        p.dist + 1));
                visited[p.row][p.col + 1] = true;

                grid[p.row ][p.col + 1].setDistanceFromStart(p.dist+1);

            }
        }

        for (RoomNode room:roomVector)
        {
            room.setDistanceFromStart(grid[room.getCoordinate().i][room.getCoordinate().j].getDistanceFromStart());
        }

    }

    // checking where it's valid or not
    private static boolean isValid(int x, int y,
                                   RoomNode[][] grid,
                                   boolean[][] visited)
    {
        if (x >= 0 && y >= 0 && x < grid.length
                && y < grid[0].length && grid[x][y] != null
                && visited[x][y] == false) {
            return true;
        }
        return false;
    }


}
