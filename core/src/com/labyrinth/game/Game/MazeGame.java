package com.labyrinth.game.Game;

import com.labyrinth.game.Screen.MenuScreen;

public class MazeGame extends BaseGame
{
    private static boolean isPaused;

    @Override
    public void create() 
    {        
        super.create();
        isPaused = false;
        //DEBUG: to test different screens at load, change to desired screen name
        setActiveScreen( new MenuScreen() );
    }

    @Override
    public void render() {
        if (!isPaused) {
            super.render();
        }
    }

    public static void setPaused(boolean paused) {
        isPaused = paused;
    }

    public static boolean isPaused() {
        return isPaused;
    }
}