package com.labyrinth.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.labyrinth.game.Actor.BaseActor;
import com.labyrinth.game.Game.BaseGame;
import com.labyrinth.game.Game.MazeGame;


public class EndScreen extends BaseScreen {

    private Music endGameMusic;

    @Override
    public void initialize() {
        BaseActor end = new BaseActor(0, 0, mainStage);
        end.loadTexture("backgrounds/gameOver.png");
        end.centerAtPosition(800, 540);

        endGameMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/endGame.mp3"));
        TextButton replayButton = new TextButton("Replay (ENTER)", BaseGame.textButtonStyle);

        replayButton.addListener(
            (Event e) ->
            {
                if(!(e instanceof InputEvent) ||
                !((InputEvent) e).getType().equals(Type.touchDown)){
                    return false;
                }
                endGameMusic.stop();
                MazeGame.setActiveScreen(new LevelScreen());
                return false;
            }
        );

        TextButton quitButton = new TextButton("Quit (ESC)", BaseGame.textButtonStyle);

        quitButton.addListener(
                (Event e) ->
                {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown)){
                        return false;
                    }
                    Gdx.app.exit();
                    return false;
                }
        );

        uiTable.add(replayButton).center().padRight(50f);
        uiTable.add(quitButton).center();
        endGameMusic.play();
    }

    @Override
    public void update(float dt) { }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Keys.ENTER)) {
            endGameMusic.stop();
            MazeGame.setActiveScreen(new LevelScreen());
        }
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        return false;
    }
}
