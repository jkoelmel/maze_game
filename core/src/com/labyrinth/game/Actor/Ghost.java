package com.labyrinth.game.Actor;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.labyrinth.game.Game.Room;

import java.util.ArrayList;

public class Ghost extends BaseActor {

    private float speed;

    public Ghost(float x, float y, Stage s) {
        super(x, y, s);

        loadAnimationFromSheet("textures/ghost.png", 1, 3, 0.3f, true);

        this.speed = 80; // pixels per second
        this.setMaxSpeed(150);

    }

    //breadth first search for one player left, overloaded function below this one
    public void findPath(Room startRoom, Room targetRoom) {
        Room currentRoom = startRoom;

        ArrayList<Room> roomList = new ArrayList<>();
        currentRoom.setPreviousRoom(null);
        currentRoom.setVisited(true);
        roomList.add(currentRoom);

        while (roomList.size() > 0) {
            currentRoom = roomList.remove(0);
            for (Room nextRoom : currentRoom.unvisitedPathList()) {
                nextRoom.setPreviousRoom(currentRoom);
                if (nextRoom == targetRoom) {
                    // target found!
                    roomList.clear();
                    break;
                } else {
                    nextRoom.setVisited(true);
                    roomList.add(nextRoom);
                }
            }
        }

        // create list of rooms corresponding to shortest path
        ArrayList<Room> pathRoomList = new ArrayList<>();
        currentRoom = targetRoom;

        while (currentRoom != null) {
            // add current room to beginning of list
            pathRoomList.add(0, currentRoom);
            currentRoom = currentRoom.getPreviousRoom();
        }

        // only move along a few steps of the path;
        //   path will be recalculated when these actions are complete.
        int maxStepCount = 2;

        // to remove the pause between steps, start loop index at 1
        //   but make ghost speed slower to compensate
        for (int i = 0; i < pathRoomList.size(); i++) {
            if (i == maxStepCount)
                break;
            Room nextRoom = pathRoomList.get(i);
            Action move = Actions.moveTo(nextRoom.getX(), nextRoom.getY(), 64 / speed);

            addAction(move);
        }
    }


    //use breadth-first search to find nearest player and track
    public void findPath(Room startRoom, Room targetRoom1, Room targetRoom2) {
        Room currentRoom = startRoom;
        Room targetRoom = null;
        ArrayList<Room> roomList = new ArrayList<>();
        currentRoom.setPreviousRoom(null);
        currentRoom.setVisited(true);
        roomList.add(currentRoom);

        while (roomList.size() > 0) {
            currentRoom = roomList.remove(0);
            for (Room nextRoom : currentRoom.unvisitedPathList()) {
                nextRoom.setPreviousRoom(currentRoom);
                if (nextRoom == targetRoom1) {
                    targetRoom = targetRoom1;
                    // target found!
                    roomList.clear();
                    break;
                } else if (nextRoom == targetRoom2) {
                    targetRoom = targetRoom2;
                    //target found!
                    roomList.clear();
                    break;
                } else {
                    nextRoom.setVisited(true);
                    roomList.add(nextRoom);
                }
            }
        }

        // create list of rooms corresponding to shortest path
        ArrayList<Room> pathRoomList = new ArrayList<>();
        currentRoom = targetRoom;

        while (currentRoom != null) {
            // add current room to beginning of list
            pathRoomList.add(0, currentRoom);
            currentRoom = currentRoom.getPreviousRoom();
        }

        // only move along a few steps of the path;
        // path will be recalculated every few steps.
        int maxStepCount = 2;

        // to remove the pause between steps, start loop index at 1
        //  but make ghost speed slower to compensate
        for (int i = 0; i < pathRoomList.size(); i++) {
            if (i == maxStepCount)
                break;
            Room nextRoom = pathRoomList.get(i);
            Action move = Actions.moveTo(nextRoom.getX(), nextRoom.getY(), 64 / speed);

            addAction(move);
        }
    }

    @Override
    public void setSpeed(float newSpeed) {
        speed = newSpeed;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }
}