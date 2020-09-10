package com.labyrinth.game.Game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.labyrinth.game.Actor.BaseActor;

import java.util.ArrayList;

public class Maze
{
    private final Room[][] roomGrid;

    // maze size constants
    private final int ROOM_COUNT_X = 25;
    private final int ROOM_COUNT_Y = 14;
    private final int ROOM_WIDTH  = 64;
    private final int ROOM_HEIGHT = 64;

    public Maze(Stage s)
    {
        long startTime = System.currentTimeMillis();

        roomGrid = new Room[ROOM_COUNT_X][ROOM_COUNT_Y];

        for (int gridY = 0; gridY < ROOM_COUNT_Y; gridY++)
        {
            for (int gridX = 0; gridX < ROOM_COUNT_X; gridX++)
            {
                float pixelX = gridX * ROOM_WIDTH;
                float pixelY = gridY * ROOM_HEIGHT;
                Room room = new Room( pixelX, pixelY, s );
                roomGrid[gridX][gridY] = room;
            }
        }

        // neighbor relations
        for (int gridY = 0; gridY < ROOM_COUNT_Y; gridY++)
        {
            for (int gridX = 0; gridX < ROOM_COUNT_X; gridX++)
            {
                Room room = roomGrid[gridX][gridY];
                if (gridY > 0)
                    room.setNeighbor( Room.SOUTH, roomGrid[gridX][gridY-1] );
                if (gridY < ROOM_COUNT_Y - 1)
                    room.setNeighbor( Room.NORTH, roomGrid[gridX][gridY+1] );
                if (gridX > 0)
                    room.setNeighbor( Room.WEST, roomGrid[gridX-1][gridY] );
                if (gridX < ROOM_COUNT_X - 1)
                    room.setNeighbor( Room.EAST, roomGrid[gridX+1][gridY] );
            }
        }

        ArrayList<Room> activeRoomList = new ArrayList<>();

        Room currentRoom = roomGrid[0][0];
        currentRoom.setConnected(true);
        activeRoomList.add(0, currentRoom);

        // chance of returning to an already visited room
        //  to create a branching path from that room
        // adjust down for longer passages; adjust up for more open mazes
        // range: 0 - 1
        float branchProbability = 0.5f;

        while (activeRoomList.size() > 0)
        {
            if (Math.random() < branchProbability)
            {
                // get random previously visited room
                int roomIndex = (int)(Math.random() * activeRoomList.size());
                currentRoom = activeRoomList.get(roomIndex);
            }
            else
            {
                // get the most recently visited room
                currentRoom = activeRoomList.get( activeRoomList.size() - 1 );
            }

            if ( currentRoom.hasUnconnectedNeighbor() )
            {
                Room nextRoom = currentRoom.getRandomUnconnectedNeighbor();
                currentRoom.removeWallsBetween(nextRoom);
                nextRoom.setConnected( true );
                activeRoomList.add(0, nextRoom);
            }
            else
            {
                // this room has no more adjacent unconnected rooms
                //   so there is no reason to keep it in the list
                activeRoomList.remove( currentRoom );
            }
        }

        // remove additional walls at random, adjust up for more open, down for closed off
        int wallsToRemove = 50;

        while (wallsToRemove > 0)
        {
            int gridX = (int)Math.floor( Math.random() * ROOM_COUNT_X );
            int gridY = (int)Math.floor( Math.random() * ROOM_COUNT_Y );
            int direction = (int)Math.floor( Math.random() * 4 );
            Room room = roomGrid[gridX][gridY];

            if ( room.hasNeighbor(direction) && room.hasWall(direction) )
            {
                room.removeWalls(direction);
                wallsToRemove--;
            }
        }

        long finishTime = System.currentTimeMillis();
        System.out.println("Time to generate maze: " + (finishTime - startTime) + " milliseconds" );

    }

    public Room getRoom(int gridX, int gridY)
    {  return roomGrid[gridX][gridY];  }

    public Room getRoom(BaseActor actor)
    {
        int gridX = Math.round(actor.getX() / ROOM_WIDTH);
        int gridY = Math.round(actor.getY() / ROOM_HEIGHT);
        return getRoom(gridX, gridY);
    }

    public void resetRooms()
    {
        for (int gridY = 0; gridY < ROOM_COUNT_Y; gridY++)
        {
            for (int gridX = 0; gridX < ROOM_COUNT_X; gridX++)
            {
                roomGrid[gridX][gridY].setVisited( false );
                roomGrid[gridX][gridY].setPreviousRoom( null );
            }
        }
    }
}