package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;public class LevelManager {
    public int currentLevel = 1;
    public int[] levelTargets = {10, 15, 20, 25, 30};
    public boolean levelCompleted = false;
    private long levelCompletedTime;
    public boolean isGameOver = false;
    public boolean gameStarted=false;
    public float flyingEnemySpawnInterval = 12f;
    public float creatureSpawnInterval = 14f;
    public float obstacleSpawnInterval = 15f;

    private List<FlyingEnemy> flyingEnemies;
    private List<Creature>creatures;
    private List<Obstacle> obstacles;
    private List<Bullet> bullets;

    private float planeHeight;

    private static final int MAX_AMMO = 6;
    private int ammo = MAX_AMMO;
    private float reloadTime = 0.25f;

    private int health = 6;
    private int killedEnemies = 0;

    private float planeX; // Uçağın yatay pozisyonu (sabit)
    private float planeY; // Uçağın dikey pozisyonu

    public LevelManager() {
        flyingEnemies = new ArrayList<>();
        creatures = new ArrayList<>();
        obstacles = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    public void checkLevelUp(int killedEnemies) {
        if (killedEnemies >= levelTargets[currentLevel - 1]) {
            levelCompleted = true;
            levelCompletedTime = TimeUtils.millis();
            currentLevel++;
            if (currentLevel > levelTargets.length) {
                currentLevel = levelTargets.length; // Maksimum seviyeyi aşma
            }
            levelUp();
        }if (levelCompleted && TimeUtils.timeSinceMillis(levelCompletedTime) > 3000) {
            levelCompleted = false;
        }
    }

    public void levelUp() {
        flyingEnemySpawnInterval *= 0.9f;
        // Diğer levelUp işlemleri
    }

    public void reset() {
        flyingEnemies.clear();
        creatures.clear();
        obstacles.clear();
        bullets.clear();

        // Oyuncu uçağını sıfırla
        planeX = 50;
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2;
        health = 6;
        ammo = MAX_AMMO;
        reloadTime = 0;
        isGameOver = false;
        killedEnemies = 0;
        currentLevel = 1;

        // Düşman uçakları oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnFlyingEnemy();
            }
        }, flyingEnemySpawnInterval, flyingEnemySpawnInterval);

        // Yaratıklar oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnCreature();
            }
        }, creatureSpawnInterval, creatureSpawnInterval);

        // Engeller oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnObstacle();
            }
        }, obstacleSpawnInterval, obstacleSpawnInterval);
    }

    public void gameOver() {
        isGameOver = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                reset();
            }
        }, 2);
    }

    public void spawnFlyingEnemy() {
        float enemyX = Gdx.graphics.getWidth();
        float enemyY = new Random().nextFloat() * (Gdx.graphics.getHeight() - planeHeight);
        float enemySpeed = 300 + new Random().nextFloat() * 100;

        FlyingEnemy enemy = new FlyingEnemy(enemyX, enemyY,enemySpeed, 50, 50); // Örnek değerler
        flyingEnemies.add(enemy);
    }

    public void spawnCreature() {
        float creatureX = Gdx.graphics.getWidth();
        float creatureY = 150; // Yaratıklar zeminde
        float creatureSpeed = 100 + new Random().nextFloat() * 50;

        Creature creature = new Creature(creatureX, creatureY, creatureSpeed, 50, 50); // Örnek değerler
        creatures.add(creature);
    }

    public void spawnObstacle() {
        float obstacleX = Gdx.graphics.getWidth();
        float obstacleY = new Random().nextFloat() * (Gdx.graphics.getHeight() - 200) + 200;
        float obstacleSpeed = 100 + new Random().nextFloat() * 50;

        Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleSpeed, 50, 50); // Örnek değerler
        obstacles.add(obstacle);
    }
}
