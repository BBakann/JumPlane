package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

public class LevelManager {

    public int currentLevel = 1;
    public int[] levelTargets = {10,15,20, 25, 1};
    public int[] levelCoinTargets = {5, 10,10 ,15,1};
    public boolean levelCompleted = false;
    private long levelCompletedTime;
    public boolean isGameOver = false;
    public boolean gameStarted = false;
    public boolean winSoundPlayed = false;

    private EnemyManager enemyManager;
    private GameScreen gameScreen;

    private static final int MAX_AMMO = 6;
    private int ammo = MAX_AMMO;
    private float reloadTime= 0.25f;
    private int health = 6;

    private Sound winSound;
    private Sound gameOverSound;

    public boolean firstStart = true;
    private SpriteBatch batch;

    public float[] levelTimes = {60f, 75f, 90f, 105f, 120f,180f}; // Level süreleri
    public float currentTime;

    public LevelManager(EnemyManager enemyManager,GameScreen gameScreen,SpriteBatch batch) {
        this.enemyManager=enemyManager;
        this.gameScreen=gameScreen;
        this.batch = batch;

        winSound = Gdx.audio.newSound(Gdx.files.internal("winsound.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameoversound.mp3"));
    }

    public void checkLevelUp(int killedEnemies, int collectedCoins) {
        if (levelCompleted) {
            return;
        }

        if (currentLevel < 6 && killedEnemies >= levelTargets[currentLevel - 1] && collectedCoins >= levelCoinTargets[currentLevel - 1]) {
            gameScreen.isLoading = true;
            levelCompleted = true;
            gameStarted = false;
            levelCompletedTime = TimeUtils.millis();

            currentLevel++;
            enemyManager.setLevel(currentLevel);
            gameScreen.handleLevelCompleted();

            winSoundPlayed = false;
            gameScreen.checkLevelCompleted();
            gameScreen.resetGame();
        } else if (currentLevel == 6) {
            // Free Level'da süre dolduğunda ana menüyedön
            if (currentTime <= 0) {
                gameScreen.game.setScreen(new MainMenuScreen(gameScreen.game, batch));
            }
        }

        if (levelCompleted && TimeUtils.timeSinceMillis(levelCompletedTime) > 3000) {
            levelCompleted = false;
            gameScreen.isLoading = false; // Yükleme ekranını kapat
        }
    }

    public void reset() {
        health = 6;
        ammo = MAX_AMMO;
        reloadTime = 0;
        isGameOver = false;
    }

    public void gameOver(){
        isGameOver = true;
        gameStarted = false;
        gameOverSound.play();
        firstStart = true;

        Timer.instance().clear();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                reset();
                gameScreen.resetGame();
                gameStarted = true;
                isGameOver = false;
            }
        }, 2f);
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(float reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public void dispose() {
        winSound.dispose();
        gameOverSound.dispose();
    }
}