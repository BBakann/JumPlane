package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;public class LevelManager {

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



    public LevelManager(EnemyManager enemyManager) {
        this.enemyManager=enemyManager;

    }

    public void checkLevelUp(int killedEnemies) {
        if (killedEnemies >= levelTargets[currentLevel - 1]) {
            levelCompleted = true;
            gameStarted = false;
            firstStart = false;
            levelCompletedTime = TimeUtils.millis();

            if (currentLevel < 5) {
                currentLevel++;
            } else {
                isGameOver = true;
            }

            winSoundPlayed = false;
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
        currentLevel = 1;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isGameOver) {
                    spawnRandomEnemy();
                    float randomDelay = random.nextFloat() * 7f + 1f;
                    Timer.schedule(this, randomDelay);
                }
            }
        }, 2f);
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

    public void gameOver() {
        isGameOver = true;
        gameStarted = false;
        health = 6;
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


