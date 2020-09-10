package com.labyrinth.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.labyrinth.game.Actor.*;
import com.labyrinth.game.Game.*;

public class LevelScreen extends BaseScreen {

    private Maze maze;
    private Hero hero;
    private float heroSpeed;
    private float heroRemoveTimer;
    private AntiHero antiHero;
    private float antiHeroSpeed;
    private float antiHeroRemoveTimer;
    private Ghost ghost;
    private float ghostHitTimer;

    private Label playerOneCoins;
    private HealthBar playerOneHP;
    private Label playerTwoCoins;
    private HealthBar playerTwoHP;
    private Label coinsLabel;
    private Label messageLabel;

    private Sound explosionSound;
    private Sound impactSound;
    private Sound coinSound;
    private Music windMusic;
    private Music levelMusic;

    private float endGameTimer;


    @Override
    public void initialize() {

        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("textures/white.png");
        background.setColor(Color.GRAY);
        background.setSize(1600, 894);
        BaseActor.setWorldBounds(background);

        BaseActor uiBackground = new BaseActor(0, 896, mainStage);
        uiBackground.loadTexture("backgrounds/ui_background.png");
        uiBackground.setSize(1600, 184);

        BaseActor heroHealth = new BaseActor(200, 995, mainStage);
        heroHealth.loadTexture("textures/healthbar_background.png");
        heroHealth.setSize(200, 20);

        BaseActor antiHeroHealth = new BaseActor(1200, 995, mainStage);
        antiHeroHealth.loadTexture("textures/healthbar_background.png");
        antiHeroHealth.setSize(200, 20);

        maze = new Maze(mainStage);

        hero = new Hero(0, 0, mainStage);
        hero.centerAtActor(maze.getRoom(0, 0));

        antiHero = new AntiHero(0, 0, mainStage);
        antiHero.centerAtActor(maze.getRoom(0, 13));

        ghost = new Ghost(0, 0, mainStage);
        ghost.centerAtActor(maze.getRoom(24, 13));

        //fill rooms with coins
        for (BaseActor room : BaseActor.getList(mainStage, Room.class.getName())) {
            Coin coin = new Coin(0, 0, mainStage);
            coin.centerAtActor(room);

            //DEBUG: uncomment if-statement to test quicker, comment out two lines above
            //Even number allows for testing of player 1 win, player 2 win, or tie
            //If testing from JAR, must clean and rebuild from artifacts
//            if (BaseActor.count(mainStage, Coin.class.getName()) < 6) {
//                Coin coin = new Coin(0, 0, mainStage);
//                coin.centerAtActor(room);
//            }
        }

        //place randomized power ups, change loop bounds as desired to populate more or less
        for (int i = 0; i < 20; i++) {
            Item powerUp = new Item(0, 0, mainStage);
            powerUp.centerAtActor(maze.getRoom(MathUtils.random(1, 24), MathUtils.random(0, 13)));
            powerUp.toFront();
            powerUp.addAction(Actions.scaleTo(0.75f, 0.75f, 1.25f));
        }

        ghost.toFront();

        heroSpeed = 150;
        heroRemoveTimer = 0;
        antiHeroSpeed = 150;
        antiHeroRemoveTimer = 0;

        playerOneCoins = new Label("Player 1 Coins: ", BaseGame.labelStyle);
        playerOneCoins.setColor(Color.WHITE);
        playerTwoCoins = new Label("Player 2 Coins: ", BaseGame.labelStyle);
        playerTwoCoins.setColor(Color.WHITE);

        Label playerOneHealth = new Label("Health: ", BaseGame.labelStyle);
        playerOneHealth.setColor(Color.RED);
        Label playerTwoHealth = new Label(" :Health", BaseGame.labelStyle);
        playerTwoHealth.setColor(Color.RED);

        coinsLabel = new Label("Coins left: ", BaseGame.labelStyle);
        coinsLabel.setColor(Color.GOLD);

        messageLabel = new Label(".", BaseGame.labelStyle);
        messageLabel.setColor(Color.GOLDENROD);
        messageLabel.setFontScale(2);
        messageLabel.setVisible(false);

        playerOneHP = new HealthBar(200, 915, mainStage);
        playerTwoHP = new HealthBar(1190, 915, mainStage);
        playerTwoHP.rotateBy(180);

        uiTable.top();
        uiTable.add(playerOneCoins).padLeft(25);
        uiTable.add(coinsLabel).expandX();
        uiTable.add(playerTwoCoins).padRight(25).row();
        uiTable.add(playerOneHealth);
        uiTable.add().expandX();
        uiTable.add(playerTwoHealth).row();
        uiTable.add(messageLabel).colspan(3);

        coinSound = Gdx.audio.newSound(Gdx.files.internal("audio/coin.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/explosion.mp3"));
        impactSound = Gdx.audio.newSound(Gdx.files.internal("audio/impact.mp3"));
        windMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/wind.mp3"));
        windMusic.setLooping(true);
        windMusic.setVolume(0.05f);
        windMusic.play();

        levelMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/levelMusic.mp3"));
        levelMusic.setVolume(1f);
        levelMusic.setLooping(true);

        ghostHitTimer = 0;
        endGameTimer = 0;
        levelMusic.play();
        //allows for breaking outer walls but player still can't escape
        hero.boundToWorld();
        antiHero.boundToWorld();
    }

    @Override
    public void update(float dt) {

        int coins = BaseActor.count(mainStage, Coin.class.getName());
        //adjust all timers on update
        hero.setHeroCoolDown(hero.getHeroCoolDown() + dt);
        antiHero.setAntiHeroCoolDown(antiHero.getAntiHeroCoolDown() + dt);
        hero.setHeroHitTimer(hero.getHeroHitTimer() - dt);
        antiHero.setAntiHeroHitTimer(antiHero.getAntiHeroHitTimer() - dt);
        ghostHitTimer -= dt;

        //start collision detection
        for (BaseActor wall : BaseActor.getList(mainStage, Wall.class.getName())) {
            hero.preventOverlap(wall);
            antiHero.preventOverlap(wall);
        }

        for (BaseActor fireball : BaseActor.getList(mainStage, Fireball.class.getName())) {

            boolean hitEnemy = false;

            for (BaseActor wall : BaseActor.getList(mainStage, Wall.class.getName())) {
                if (fireball.overlaps(wall)) {
                    ExplosionEffect boom = new ExplosionEffect();
                    boom.centerAtActor(fireball);
                    boom.start();
                    mainStage.addActor(boom);
                    explosionSound.play(0.75f);

                    fireball.remove();
                    wall.remove();
                }
            }

            for (BaseActor antihero : BaseActor.getList(mainStage, AntiHero.class.getName())) {

                if (fireball.overlaps(antihero)) {
                    fireball.remove();
                    //remove ability to shoot and freeze for one second
                    antiHero.setAntiHeroHitTimer(1f);
                    antiHero.setAntiHeroCoolDown(1f);
                    hitEnemy = true;
                    impactSound.play(0.75f);
                }
            }

            for (BaseActor antifireball : BaseActor.getList(mainStage, AntiFireball.class.getName())) {
                if (fireball.overlaps(antifireball)) {
                    ExplosionEffect boom = new ExplosionEffect();
                    boom.centerAtActor(fireball);
                    boom.start();
                    mainStage.addActor(boom);
                    explosionSound.play(0.75f);

                    fireball.remove();
                    antifireball.remove();
                }
            }

            for (BaseActor ghost : BaseActor.getList(mainStage, Ghost.class.getName())) {
                if (fireball.overlaps(ghost)) {
                    fireball.remove();
                    ghostHitTimer = 1f;
                    impactSound.play(0.75f);
                }
            }

            if (hitEnemy) {
                antiHero.setHealth(antiHero.getHealth() - 20);
                playerTwoHP.setWidth(playerTwoHP.getWidth() - 40);
                antiHero.setAntiHeroCoolDown(1);
                antiHero.setAntiHeroHitTimer(1);
                if (!antiHero.hasHealth()) {
                    antiHero.addAction(Actions.fadeOut(1.5f));
                    antiHero.addAction(Actions.after(Actions.removeActor()));
                    antiHero.setAntiHeroDied(true);
                }
            }
            //remove at max range
            if(!fireball.isWithinDistance(1000, hero)){
                fireball.addAction(Actions.fadeOut(0.5f));
                fireball.addAction(Actions.after(Actions.removeActor()));
            }
        }

        for (BaseActor antifireball : BaseActor.getList(mainStage, AntiFireball.class.getName())) {

            boolean hitEnemy = false;

            for (BaseActor wall : BaseActor.getList(mainStage, Wall.class.getName())) {
                if (antifireball.overlaps(wall)) {
                    ExplosionEffect boom = new ExplosionEffect();
                    boom.centerAtActor(antifireball);
                    boom.start();
                    mainStage.addActor(boom);
                    explosionSound.play(0.75f);

                    antifireball.remove();
                    wall.remove();
                }
            }

            for (BaseActor hero : BaseActor.getList(mainStage, Hero.class.getName())) {
                if (antifireball.overlaps(hero)) {
                    antifireball.remove();
                    hitEnemy = true;
                    impactSound.play(0.75f);
                }
            }

            for (BaseActor ghost : BaseActor.getList(mainStage, Ghost.class.getName())) {
                if (antifireball.overlaps(ghost)) {
                    antifireball.remove();
                    ghostHitTimer = 1f;
                    impactSound.play(0.75f);
                }
            }
            if (hitEnemy) {
                hero.setHealth(hero.getHealth() - 20);
                playerOneHP.setWidth(playerOneHP.getWidth() - 40);
                hero.setHeroHitTimer(1);
                hero.setHeroCoolDown(1);
                if (!hero.hasHealth()) {
                    hero.addAction(Actions.fadeOut(1.5f));
                    hero.addAction(Actions.after(Actions.removeActor()));
                    hero.setHeroDied(true);
                }
            }
            //remove at max range
            if(!antifireball.isWithinDistance(1000, antiHero)){
                antifireball.addAction(Actions.fadeOut(0.5f));
                antifireball.addAction(Actions.after(Actions.removeActor()));
            }
        }
        //end basic collision

        //start ghost pathfinding
        if (ghost.getActions().size == 0) {
            maze.resetRooms();
            if (hero.getHeroDied()) {
                ghost.findPath(maze.getRoom(ghost), maze.getRoom(antiHero));
            } else if (antiHero.getAntiHeroDied()) {
                ghost.findPath(maze.getRoom(ghost), maze.getRoom(hero));
            } else {
                ghost.findPath(maze.getRoom(ghost), maze.getRoom(hero), maze.getRoom(antiHero));
            }
        }
        //end ghost pathfinding

        //start coin and item collection
        for (BaseActor coin : BaseActor.getList(mainStage, Coin.class.getName())) {
            if (hero.overlaps(coin)) {
                hero.setHeroCoins(1);
                coin.remove();
                //increase ghost speed with each coin picked up
                ghost.setSpeed(ghost.getSpeed() * 1.005f);
                //prevent coin sound from playing constantly, every 5th coin
                if (coins % 5 == 0) {
                    coinSound.play(0.25f);
                }
            }
            if (antiHero.overlaps(coin)) {
                antiHero.setAntiHeroCoins(1);
                coin.remove();
                ghost.setSpeed(ghost.getSpeed() * 1.005f);
                if (coins % 5 == 0) {
                    coinSound.play(0.25f);
                }
            }
        }

        for (BaseActor item : BaseActor.getList(mainStage, Item.class.getName())) {
            if (hero.overlaps(item)) {
                Item type = (Item) item;
                if (type.getType() == Item.Type.HEALTH) {
                    hero.setHealth(hero.getHealth() + 20);
                    playerOneHP.setWidth(playerOneHP.getWidth() + 40);
                } else if (type.getType() == Item.Type.SPEED_DOWN) {
                    if(antiHero.getMaxSpeed() > 100) {
                        antiHero.setMaxSpeed(antiHero.getMaxSpeed() - 25);
                        antiHeroSpeed = antiHero.getMaxSpeed();
                    }
                } else if (type.getType() == Item.Type.SPEED_UP) {
                    if (hero.getMaxSpeed() < 200)
                        heroSpeed = hero.getMaxSpeed() + 25;
                }
                item.remove();
            } else if (antiHero.overlaps(item)) {
                Item type = (Item) item;
                if (type.getType() == Item.Type.HEALTH) {
                    antiHero.setHealth(antiHero.getHealth() + 20);
                    playerTwoHP.setWidth(playerTwoHP.getWidth() + 40);
                } else if (type.getType() == Item.Type.SPEED_DOWN) {
                    if(hero.getMaxSpeed() > 100) {
                        hero.setMaxSpeed(hero.getMaxSpeed() -25);
                        heroSpeed = hero.getMaxSpeed();
                    }
                } else if (type.getType() == Item.Type.SPEED_UP) {
                    if (antiHero.getMaxSpeed() < 200)
                        antiHeroSpeed = antiHero.getMaxSpeed() + 25;
                }
                item.remove();
            }
        }
        //end coin and item collecting

        //update coins before labeling
        coins = BaseActor.count(mainStage, Coin.class.getName());
        coinsLabel.setText("Coins left: " + coins);
        playerOneCoins.setText("Player 1: " + hero.getHeroCoins());
        playerTwoCoins.setText("Player 2: " + antiHero.getAntiHeroCoins());

        //check end-game status and ghost collision
        if (coins == 0) {
            endGameTimer += dt;
            ghost.setPosition(-1000, -1000);
            if (hero.getHeroCoins() > antiHero.getAntiHeroCoins()) {
                messageLabel.setText("Player One Wins!");
                messageLabel.setColor(Color.GOLD);
                messageLabel.setPosition(500, 540);
                messageLabel.setVisible(true);
            } else if (antiHero.getAntiHeroCoins() > hero.getHeroCoins()) {
                messageLabel.setText("Player Two Wins!");
                messageLabel.setColor(Color.GOLD);
                messageLabel.setPosition(500, 540);
                messageLabel.setVisible(true);
            } else {
                messageLabel.setText("It's a tie!");
                messageLabel.setColor(Color.RED);
                messageLabel.setPosition(645, 540);
                messageLabel.setVisible(true);
            }

            if (endGameTimer > 5) {
                levelMusic.stop();
                windMusic.stop();
                MazeGame.setActiveScreen(new EndScreen());
            }
        }

        if (hero.overlaps(ghost)) {

            hero.setHealth(hero.getHealth() - 20);
            playerOneHP.setWidth(playerOneHP.getWidth() - 40);
            if (!hero.hasHealth()) {
                hero.addAction(Actions.fadeOut(1.5f));
                hero.addAction(Actions.after(Actions.removeActor()));
                hero.setHeroDied(true);
            }
            ghost.remove();
            ghost = new Ghost(0, 0, mainStage);
            ghost.centerAtActor(maze.getRoom(MathUtils.random(0, 24), MathUtils.random(0, 13)));
        }

        if (antiHero.overlaps(ghost)) {

            antiHero.setHealth(antiHero.getHealth() - 20);
            playerTwoHP.setWidth(playerTwoHP.getWidth() - 40);
            if (!antiHero.hasHealth()) {
                antiHero.addAction(Actions.fadeOut(1.5f));
                antiHero.addAction(Actions.after(Actions.removeActor()));
                antiHero.setAntiHeroDied(true);
            }
            ghost.remove();
            ghost = new Ghost(0, 0, mainStage);
            ghost.centerAtActor(maze.getRoom(MathUtils.random(0, 24), MathUtils.random(0, 13)));
        }

        //adjust wind noise based on proximity of ghost while game is active
        if (!messageLabel.isVisible()) {
            float distance1 = new Vector2(hero.getX() - ghost.getX(), hero.getY() - ghost.getY()).len();
            float distance2 = new Vector2(antiHero.getX() - ghost.getX(), antiHero.getY() - ghost.getY()).len();
            //tailor sound scaling to closest hero
            float distance = Math.min(distance1, distance2);
            float volume = -(distance - 64) / (300 - 64) + 1;
            volume = MathUtils.clamp(volume, 0.05f, 1.00f);
            windMusic.setVolume(volume);
        }

        //check all timers and respond
        if (hero.getHeroHitTimer() > 0) {
            hero.setMaxSpeed(0);
        } else {
            hero.setMaxSpeed(heroSpeed);
        }

        if (antiHero.getAntiHeroHitTimer() > 0) {
            antiHero.setMaxSpeed(0);
        } else {
            antiHero.setMaxSpeed(antiHeroSpeed);
        }

        if (ghostHitTimer > 0) {
            ghost.addAction(Actions.delay(1f));
        }

        //death checks
        if (hero.getHeroDied()) {
            heroRemoveTimer += dt;
            if (heroRemoveTimer > 3) {
                //remove from map after actions play
                hero.setPosition(-1200, -1200);
            }
        }

        if (antiHero.getAntiHeroDied()) {
            antiHeroRemoveTimer += dt;
            if (antiHeroRemoveTimer > 3) {
                //remove from map after actions play
                antiHero.setPosition(-1100, -1100);
            }
        }

        if (hero.getHeroDied() && antiHero.getAntiHeroDied()) {
            ghost.clearActions();
            ghost.addAction(Actions.forever(Actions.delay(1)));
            ghost.remove();
            messageLabel.setText("Game Over");
            messageLabel.setPosition(560, 540);
            messageLabel.setColor(Color.RED);
            messageLabel.setVisible(true);
            endGameTimer += dt;

            if (endGameTimer > 5) {
                levelMusic.stop();
                windMusic.stop();
                MazeGame.setActiveScreen(new EndScreen());
            }
        }
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Keys.R) {
            levelMusic.stop();
            windMusic.stop();
            MazeGame.setActiveScreen(new LevelScreen());
        }
        if (keyCode == Keys.SPACE && antiHero.getAntiHeroCoolDown() > 2) {
            antiHero.shoot();
            antiHero.setAntiHeroCoolDown(0f);
        }
        if (keyCode == Keys.CONTROL_RIGHT && hero.getHeroCoolDown() > 2) {
            hero.shoot();
            hero.setHeroCoolDown(0f);
        }
        if (keyCode == Keys.M) {
            if (levelMusic.getVolume() == 1) {
                levelMusic.setVolume(0);
            } else {
                levelMusic.setVolume(1);
            }
        }
        if (keyCode == Keys.P) {
            if (MazeGame.isPaused()) {
                MazeGame.setPaused(false);
                windMusic.play();
                levelMusic.play();
            } else {
                levelMusic.pause();
                windMusic.pause();
                MazeGame.setPaused(true);
            }
        }
        if (keyCode == Keys.ESCAPE) {
            levelMusic.stop();
            windMusic.stop();
            MazeGame.setActiveScreen(new MenuScreen());
        }

        return false;
    }

}