package com.labyrinth.game.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class ParticleActor extends Group{

    private ParticleEffect effect;
    private ParticleRenderer renderingActor;

    //private inner class to handle the particle effects when fireballs collide
    private class ParticleRenderer extends Actor {
        private ParticleEffect effect;

        ParticleRenderer(ParticleEffect e) {
            effect = e;
        }

        public void draw(Batch batch, float parentAlpha) {
            effect.draw(batch);
        }
    }

    //Actual instance created as a spawn point for effects
    public ParticleActor(String pfxFile, String imageDir) {
        super();
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(pfxFile), Gdx.files.internal(imageDir));
        renderingActor = new ParticleRenderer(effect);
        this.addActor(renderingActor);
    }

    public void start() {
        effect.start();
    }

    //pauses continuous emitters
    public void stop() {
        effect.allowCompletion();
    }

    public boolean isRunning() {
        return !effect.isComplete();
    }

    public void centerAtActor(Actor other) {
        setPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        effect.update(dt);

        if (effect.isComplete() && !effect.getEmitters().first().isContinuous()) {
            effect.dispose();
            this.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

}
