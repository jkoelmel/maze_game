package com.labyrinth.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.labyrinth.game.Game.MazeGame;

public class DesktopLauncher {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        //bind screen size to min map size to retain scaling
        //adjusting scaling requires shifting static location of health bars
        //in level screen initialize method
        config.setWindowSizeLimits(1600, 1000, 1600, 1000);

        config.setTitle("Labyrinth");
//        MazeGame launcher = new MazeGame();
        new Lwjgl3Application(new MazeGame(), config);
    }
}