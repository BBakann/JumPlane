package com.berdanbakan.jumplane;


import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import java.util.Random;

public class LevelManager {

    public int currentLevel = 1;
    public int[] levelTargets = {10,15, 20, 25, 30};
    public boolean levelCompleted = false;
    private long levelCompletedTime;
    public boolean isGameOver = false;
    public boolean gameStarted = false;
    public boolean firstStart = true;
    public boolean winSoundPlayed = false;

    private EnemyManager enemyManager;
    private Random random = new Random();

    private static final int MAX_AMMO = 6;
    private int ammo = MAX_AMMO;
    private float reloadTime = 0.25f;
    private int health = 6;

    private GameScreen gameScreen;

    public LevelManager(EnemyManager enemyManager,GameScreen gameScreen) {
        this.enemyManager=enemyManager;
        this.gameScreen=gameScreen;
    }

    public void checkLevelUp(int killedEnemies) {
        if (killedEnemies >= levelTargets[currentLevel - 1]) {
            gameScreen.isLoading = true;
            levelCompleted = true;
            gameStarted = false;
            firstStart = false;levelCompletedTime = TimeUtils.millis();

            if (currentLevel < 5) {
                currentLevel++;
                enemyManager.setLevel(currentLevel);
            } else {
                isGameOver = true;
            }

            gameScreen.handleLevelCompleted();

            winSoundPlayed = false;
            gameScreen.checkLevelCompleted();
            gameScreen.resetGame();
        }


        if (levelCompleted && TimeUtils.timeSinceMillis(levelCompletedTime) > 3000) {
            levelCompleted = false;
        }
    }

    public void reset() {
        health = 6;
        ammo = MAX_AMMO;
        reloadTime = 0;
        isGameOver = false;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isGameOver) {
                    spawnRandomEnemy();
                    float randomDelay = random.nextFloat() * 7f + 1f;
                    Timer.schedule(this, randomDelay);
                }
            }
        }, 5f);
    }

    private void spawnRandomEnemy() {
        float chance = random.nextFloat();
        if (chance < 0.33f) {
            enemyManager.spawnFlyingEnemy();
        } else if (chance < 0.66f) {enemyManager.spawnCreature();
        } else {
            enemyManager.spawnObstacle();
        }
    }
    public void gameOver(){
        isGameOver = true;
        gameStarted = false;


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
}


