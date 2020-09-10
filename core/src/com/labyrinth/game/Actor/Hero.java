package com.labyrinth.game.Actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class Hero extends BaseActor {

    private final Animation<TextureRegion> north;
    private final Animation<TextureRegion> south;
    private final Animation<TextureRegion> east;
    private final Animation<TextureRegion> west;

    private int health;
    private String direction;
    private int heroCoins;
    private float heroHitTimer;
    private float heroCoolDown;
    private boolean heroDied;

    public Hero(float x, float y, Stage s) {

        super(x, y, s);

        String fileName = "textures/hero.png";

        int rows = 4;
        int cols = 3;
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        float frameDuration = 0.2f;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();
        for (int i = 0; i < cols; i++) {
            textureArray.add(temp[0][i]);
        }
        south = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int i = 0; i < cols; i++) {
            textureArray.add(temp[1][i]);
        }
        west = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int i = 0; i < cols; i++) {
            textureArray.add(temp[2][i]);
        }
        east = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int i = 0; i < cols; i++) {
            textureArray.add(temp[3][i]);
        }
        north = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        setAnimation(south);

        //set after animation established
        setBoundaryPolygon(8);

        setAcceleration(800);
        setMaxSpeed(150);
        setDeceleration(800);

        health = 100;
        heroCoins = 0;
        heroHitTimer = 0;
        heroCoolDown = 0;
        heroDied = false;
    }

    public void act(float dt) {
        super.act(dt);

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            accelerateAtAngle(180);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            accelerateAtAngle(0);
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            accelerateAtAngle(90);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            accelerateAtAngle(270);
        }

        if (getSpeed() == 0) {
            setAnimationPaused(true);
        } else {
            setAnimationPaused(false);
            //set direction animation
            float angle = getMotionAngle();
            if (angle >= 45 && angle <= 135) {
                setAnimation(north);
                direction = "north";
            } else if (angle > 135 && angle < 225) {
                setAnimation(west);
                direction = "west";
            } else if (angle >= 225 && angle <= 315) {
                setAnimation(south);
                direction = "south";
            } else {
                setAnimation(east);
                direction = "east";
            }
        }

        applyPhysics(dt);
        boundToWorld();
    }

    public boolean hasHealth() {
        return health > 1;
    }

    public void shoot() {
        if (getStage() == null) {
            return;
        }

        Fireball fireball = new Fireball(0, 0, this.getStage());
        fireball.centerAtActor(this);

        switch (direction) {
            case "east":
                fireball.setRotation(90);
                fireball.setMotionAngle(0);
                break;
            case "north":
                fireball.setRotation(180);
                fireball.setMotionAngle(90);
                break;
            case "west":
                fireball.setRotation(270);
                fireball.setMotionAngle(180);
                break;
            default:
                fireball.setRotation(0);
                fireball.setMotionAngle(270);
                break;
        }
    }
    
    //general setters and getters
    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHeroCoins(int coin) {
        heroCoins += coin;
    }

    public int getHeroCoins() {
        return this.heroCoins;
    }

    public void setHeroHitTimer(float update) {
        heroHitTimer = update;
    }

    public float getHeroHitTimer() {
        return this.heroHitTimer;
    }

    public void setHeroCoolDown(float update) {
        heroCoolDown = update;
    }

    public float getHeroCoolDown() {
        return this.heroCoolDown;
    }

    public void setHeroDied(boolean update) {
        heroDied = update;
    }

    public boolean getHeroDied() {
        return heroDied;
    }
}
