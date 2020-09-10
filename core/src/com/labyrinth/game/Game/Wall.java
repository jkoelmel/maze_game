package com.labyrinth.game.Game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.labyrinth.game.Actor.BaseActor;

public class Wall extends BaseActor {

    public Wall(float x, float y, float w, float h, Stage s) {
        super(x, y, s);
        loadTexture("textures/wall.jpg");
        setSize(w,h);
        setBoundaryRectangle();
    }
}
