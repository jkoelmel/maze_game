package com.labyrinth.game.Actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Item extends BaseActor{

    public enum Type {SPEED_UP, SPEED_DOWN, HEALTH}

    private Type type;

    public Item(float x, float y, Stage s) {
        super(x, y, s);
        setRandomType();

        setBoundaryRectangle();
        setScale(0.5f, 0.5f);
        addAction(Actions.scaleTo(0.9f, 0.9f, 0.5f));
    }

    public void setType(Type type) {
        this.type = type;

        if (type == Type.HEALTH) {
            loadTexture("textures/health.png");
        } else if (type == Type.SPEED_UP) {
            loadTexture(("textures/speed_up.png"));
        } else if (type == Type.SPEED_DOWN) {
            loadTexture("textures/speed_down.png");
        }
    }

    public void setRandomType() {
        int randomIndex = MathUtils.random(0, Type.values().length - 1);
        Type randomType = Type.values()[randomIndex];
        setType(randomType);
    }

    public Type getType() {
        return this.type;
    }
}
