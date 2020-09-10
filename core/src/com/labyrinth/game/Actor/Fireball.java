package com.labyrinth.game.Actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Fireball extends BaseActor {

    public Fireball(float x, float y, Stage s) {
        super(x, y, s);

        loadAnimationFromSheet("textures/fireball.png", 1, 5, 0.2f, true);

        setSpeed(400);
        setMaxSpeed(400);
        setDeceleration(0);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
    }
}
