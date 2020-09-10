package com.labyrinth.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.labyrinth.game.Actor.BaseActor;
import com.labyrinth.game.Game.BaseGame;
import com.labyrinth.game.Game.MazeGame;

public class InstructionScreen extends BaseScreen {

    @Override
    public void initialize() {

        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("backgrounds/stones.png");
        background.setSize(1600, 1080);
        background.centerAtPosition(800, 540);

        Label menuReturn = new Label("ESC or click icon for main menu", BaseGame.labelStyle);
        menuReturn.setColor(Color.RED);
        Label player1 = new Label("Player 1 Controls: ", BaseGame.labelStyle);
        player1.setColor(Color.WHITE);
        Label p1KeyMap = new Label("Arrow keys for direction, R-CTRL to shoot", BaseGame.labelStyle);
        p1KeyMap.setColor(Color.WHITE);
        Label player2 = new Label("Player 2 Controls: ", BaseGame.labelStyle);
        player2.setColor(Color.WHITE);
        Label p2KeyMap = new Label("WASD keys for direction, SPACE to shoot", BaseGame.labelStyle);
        p2KeyMap.setColor(Color.WHITE);
        Label ghost = new Label("Ghost chases players: ", BaseGame.labelStyle);
        ghost.setColor(Color.WHITE);
        Label ghostExplain = new Label("After contact, he appears randomly elsewhere", BaseGame.labelStyle);
        Label basicControls = new Label("Generic Controls: ", BaseGame.labelStyle);
        basicControls.setColor(Color.WHITE);
        Label basicKeyMap = new Label("R - New Maze, M - toggle music, P: Pause, ESC: Main Menu", BaseGame.labelStyle);
        basicKeyMap.setColor(Color.WHITE);
        Label powerUpExplain = new Label("Heart = +Health; Ice = Slow Opponent; Up Arrow = Speed Up Player",
                BaseGame.labelStyle);

        ButtonStyle heroStyle = new ButtonStyle();
        Texture heroTex = new Texture(Gdx.files.internal("icon/hero.png"));
        TextureRegion heroRegion = new TextureRegion(heroTex);
        heroStyle.up = new TextureRegionDrawable(heroRegion);
        Button heroIcon = new Button(heroStyle);
        heroIcon.setColor(Color.WHITE);

        ButtonStyle antiheroStyle = new ButtonStyle();
        Texture antiheroTex = new Texture(Gdx.files.internal("icon/antihero.png"));
        TextureRegion antiheroRegion = new TextureRegion(antiheroTex);
        antiheroStyle.up = new TextureRegionDrawable(antiheroRegion);
        Button antiHeroIcon = new Button(antiheroStyle);
        antiHeroIcon.setColor(Color.WHITE);

        ButtonStyle ghostStyle = new ButtonStyle();
        Texture ghostTex = new Texture(Gdx.files.internal("icon/ghost.png"));
        TextureRegion ghostRegion = new TextureRegion(ghostTex);
        ghostStyle.up = new TextureRegionDrawable(ghostRegion);
        Button ghostIcon = new Button(ghostStyle);
        ghostIcon.setColor(Color.WHITE);

        ButtonStyle buttonIconStyle = new ButtonStyle();
        Texture buttonIconTex = new Texture(Gdx.files.internal("icon/key_arrow.png"));
        TextureRegion buttonIconRegion = new TextureRegion(buttonIconTex);
        buttonIconStyle.up = new TextureRegionDrawable(buttonIconRegion);
        Button buttonIcon = new Button(buttonIconStyle);
        buttonIcon.setColor(Color.WHITE);

        ButtonStyle powerStyle = new ButtonStyle();
        Texture powerTex = new Texture(Gdx.files.internal("icon/powerups.png"));
        TextureRegion powerRegion = new TextureRegion(powerTex);
        powerStyle.up = new TextureRegionDrawable(powerRegion);
        Button powerUpIcon = new Button(powerStyle);
        powerUpIcon.setColor(Color.WHITE);

        uiTable.add(heroIcon).row();
        uiTable.add(player1);
        uiTable.add(p1KeyMap).row();
        uiTable.add(antiHeroIcon).row();
        uiTable.add(player2);
        uiTable.add(p2KeyMap).row();
        uiTable.add(ghostIcon).row();
        uiTable.add(ghost);
        uiTable.add(ghostExplain).row();
        uiTable.add(buttonIcon).row();
        uiTable.add(basicControls);
        uiTable.add(basicKeyMap).row();
        uiTable.add(powerUpIcon).row();
        uiTable.add(powerUpExplain).colspan(2).row();
        uiTable.add(menuReturn).colspan(2);

        ButtonStyle buttonStyle = new ButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("icon/back.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);

        Button returnButton = new Button(buttonStyle);
        returnButton.setColor(Color.WHITE);
        returnButton.setPosition(10, 920);

        uiStage.addActor(returnButton);

        returnButton.addListener(
                (Event e) ->
                {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    MazeGame.setActiveScreen(new MenuScreen());
                    return false;
                }
        );
    }

    @Override
    public void update(float dt) { }

    @Override
    public boolean keyDown(int keyCode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            MazeGame.setActiveScreen(new MenuScreen());
        }
        return false;
    }
}
