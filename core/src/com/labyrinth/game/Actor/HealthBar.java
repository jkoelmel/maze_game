package com.labyrinth.game.Actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class HealthBar extends BaseActor {

    public HealthBar(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("textures/healthbar.png");
    }
}
