package com.labyrinth.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.labyrinth.game.Actor.BaseActor;
import com.labyrinth.game.Game.BaseGame;
import com.labyrinth.game.Game.MazeGame;

public class MenuScreen extends BaseScreen {

    private Sound startSound;
    private Music menuMusic;

    @Override
    public void initialize() {

        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("backgrounds/castle_background.png");
        background.setSize(1600, 1080);
        background.centerAtPosition(800, 540);

        BaseActor castle = new BaseActor(0,0, mainStage);
        castle.loadTexture( "backgrounds/castle.png" );
        castle.setSize(1261,1080);
        castle.centerAtPosition(800, 540);

        BaseActor title = new BaseActor(0,0, mainStage);
        title.loadTexture( "icon/labyrinth.png" );
        title.setColor(Color.WHITE);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/startMenu.mp3"));
        startSound = Gdx.audio.newSound(Gdx.files.internal("audio/startButton.mp3"));

        TextButton startButton = new TextButton("Start (ANY KEY)", BaseGame.textButtonStyle);

        startButton.addListener(
            (Event e) ->
            {
                if (!(e instanceof InputEvent) ||
                        !((InputEvent) e).getType().equals(Type.touchDown)) {
                    return false;
                }
                startSound.play(0.60f);
                menuMusic.stop();
                MazeGame.setActiveScreen(new LevelScreen());
                return false;
            }
        );

        TextButton quitButton = new TextButton("Quit (ESC)", BaseGame.textButtonStyle);

        quitButton.addListener(
            (Event e) ->
            {
                if (!(e instanceof InputEvent) ||
                        !((InputEvent) e).getType().equals(Type.touchDown)) {
                    return false;
                }
                Gdx.app.exit();
                return false;
            }
        );

        TextButton howTo = new TextButton("How to Play (H)", BaseGame.textButtonStyle);

        howTo.addListener(
                (Event e) ->
                {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown)) {
                        return false;
                    }
                    MazeGame.setActiveScreen(new InstructionScreen());
                    return false;
                }
        );

        uiTable.add(title).colspan(2);
        uiTable.row();
        uiTable.add(startButton);
        uiTable.add(quitButton);
        uiTable.row();
        uiTable.add(howTo).colspan(2);
        menuMusic.setVolume(0.5f);
        menuMusic.play();
    }

    @Override
    public void update(float dt) { }

    @Override
    public boolean keyDown(int keyCode) {
        if(Gdx.input.isKeyPressed(Keys.H)) {
            MazeGame.setActiveScreen(new InstructionScreen());
        }
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Keys.ANY_KEY)) {
            startSound.play(0.25f);
            menuMusic.stop();
            MazeGame.setActiveScreen(new LevelScreen());
        }
        return false;
    }
}
