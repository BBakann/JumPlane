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
    public boolean firstStart=true;


    private EnemyManager enemyManager;

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

    public LevelManager(EnemyManager enemyManager) {
        this.enemyManager=enemyManager;

        flyingEnemies = new ArrayList<>();
        creatures = new ArrayList<>();
        obstacles = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    public void checkLevelUp(int killedEnemies) {
        if (killedEnemies >= levelTargets[currentLevel - 1]) {
            levelCompleted = true;
            gameStarted=false;
            levelCompletedTime = TimeUtils.millis();
            if (currentLevel < 5) {
                levelUp(); // Level atlama işlemlerini bu metoda taşı
            } else {
                // Oyunu tamamla
                isGameOver = true;
            }
        }
        if (levelCompleted && TimeUtils.timeSinceMillis(levelCompletedTime) > 3000) {levelCompleted = false;
        }
    }

    public void levelUp() {
        currentLevel++;
        enemyManager.reset();
        killedEnemies=0;
        gameStarted=false;
        health=6;

    }

    public void reset() {
        flyingEnemies.clear();
        creatures.clear();
        obstacles.clear();
        bullets.clear();

        // Oyuncu uçağını sıfırla
        planeX = 50;
        planeY = Gdx.graphics.getHeight() / 2- planeHeight / 2;
        health = 6;
        ammo = MAX_AMMO;
        reloadTime = 0;
        isGameOver = false;
        killedEnemies = 0;
        currentLevel = 1;

        Random random = new Random();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isGameOver) {
                    float randomDelay = random.nextFloat() * 7f + 1f; // 1 ile 7saniye arasında rastgele gecikme

                    float chance = random.nextFloat();
                    if (chance < 0.33f) {
                        enemyManager.spawnFlyingEnemy();
                    } else if (chance < 0.66f) {
                        enemyManager.spawnCreature();
                    } else {
                        enemyManager.spawnObstacle();
                    }

                    // Zamanlayıcıyı yeni gecikme değeri ile yeniden başlat
                    Timer.schedule(this, randomDelay);
                }
            }
        }, 2f); // İlk gecikme 2 saniye
    }

    public void gameOver() {
        isGameOver = true;
        gameStarted=false;


        killedEnemies=0;
        health=6;


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
