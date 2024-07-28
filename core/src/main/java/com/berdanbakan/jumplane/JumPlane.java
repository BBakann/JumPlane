package com.berdanbakan.jumplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JumPlane extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Texture[] planeTextures;
    private Texture currentPlaneTexture;
    private Texture enemyPlaneTexture;
    private Texture groundTexture;
    private Texture creatureTexture;
    private Texture obstacleTexture;

    private int currentLevel;
    private float planeWidth;
    private float planeHeight;
    private float enemyPlaneWidth;
    private float enemyPlaneHeight;
    private float groundHeight;

    private List<FlyingEnemy> flyingEnemies;
    private List<GroundEnemy> groundEnemies;
    private List<Creature> creatures;
    private List<Obstacle> obstacles;

    private Random random;

    private float spawnInterval;

    private Rectangle playerPlaneRectangle;

    private float planeX; // Uçağın yatay pozisyonu (sabit)
    private float planeY; // Uçağın dikey pozisyonu

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");

        planeTextures = new Texture[5];
        for (int i = 0; i < planeTextures.length; i++) {
            planeTextures[i] = new Texture("plane" + (i + 1) + ".png");
        }

        enemyPlaneTexture = new Texture("enemyplane.png");
        groundTexture = new Texture("ground.png");
        creatureTexture = new Texture("creature.png");
        obstacleTexture = new Texture("obstacle.png");

        currentPlaneTexture = planeTextures[0];

        planeWidth = currentPlaneTexture.getWidth() / 2;
        planeHeight = currentPlaneTexture.getHeight() / 2;

        enemyPlaneWidth = enemyPlaneTexture.getWidth() / 2;
        enemyPlaneHeight = enemyPlaneTexture.getHeight() / 2;

        groundHeight = groundTexture.getHeight();

        currentLevel = 1;

        flyingEnemies = new ArrayList<>();
        groundEnemies = new ArrayList<>();
        creatures = new ArrayList<>();
        obstacles = new ArrayList<>();
        random = new Random();

        spawnInterval = 2;

        playerPlaneRectangle = new Rectangle();

        // Uçağın başlangıç pozisyonunu ayarla
        planeX = 0; // Ekranın en sol noktası
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2; // Y ekseninde ortala

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnFlyingEnemy();
                spawnGroundEnemy();
                spawnCreature();
                spawnObstacle();
            }
        }, spawnInterval, spawnInterval);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        updatePlaneTexture();

        // Uçak kontrolü
        if (Gdx.input.isTouched()) {
            planeY = Gdx.input.getY() - planeHeight / 2;
        }
        // Uçağın ekran sınırları içinde kalmasını sağla
        if (planeY < 0) planeY = 0;
        if (planeY > Gdx.graphics.getHeight() - planeHeight) planeY = Gdx.graphics.getHeight() - planeHeight;

        // Düşman uçaklarını hareket ettir ve temizle
        List<FlyingEnemy> toRemoveFlyingEnemies = new ArrayList<>();
        for (FlyingEnemy enemy : flyingEnemies) {
            enemy.x -= enemy.speed * Gdx.graphics.getDeltaTime();
            if (enemy.x < -enemy.width) {
                toRemoveFlyingEnemies.add(enemy);
            }
            enemy.rectangle.set(enemy.x, enemy.y, enemy.width, enemy.height);
            if (enemy.rectangle.overlaps(playerPlaneRectangle)) {
                gameOver();
            }
        }
        flyingEnemies.removeAll(toRemoveFlyingEnemies);

        // Zemin düşmanlarını hareket ettir ve temizle
        List<GroundEnemy> toRemoveGroundEnemies = new ArrayList<>();
        for (GroundEnemy enemy : groundEnemies) {
            enemy.x -= enemy.speed * Gdx.graphics.getDeltaTime();
            if (enemy.x < -enemy.width) {
                toRemoveGroundEnemies.add(enemy);
            }
            enemy.rectangle.set(enemy.x, groundHeight, enemy.width, enemy.height); // Y koordinatı groundHeight
            if (enemy.rectangle.overlaps(playerPlaneRectangle)) {
                gameOver();
            }
        }
        groundEnemies.removeAll(toRemoveGroundEnemies);

        // Yaratıkları hareket ettir ve temizle
        List<Creature> toRemoveCreatures = new ArrayList<>();
        for (Creature creature : creatures) {
            creature.x -= creature.speed * Gdx.graphics.getDeltaTime();
            if (creature.x < -creature.width) {
                toRemoveCreatures.add(creature);
            }
            creature.rectangle.set(creature.x, creature.y, creature.width, creature.height);
            if (creature.rectangle.overlaps(playerPlaneRectangle)) {
                gameOver();
            }
        }
        creatures.removeAll(toRemoveCreatures);

        // Engelleri hareket ettir ve temizle
        List<Obstacle> toRemoveObstacles = new ArrayList<>();
        for (Obstacle obstacle : obstacles) {
            obstacle.x -= obstacle.speed * Gdx.graphics.getDeltaTime();
            if (obstacle.x < -obstacle.width) {
                toRemoveObstacles.add(obstacle);
            }
            obstacle.rectangle.set(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            if (obstacle.rectangle.overlaps(playerPlaneRectangle)) {
                gameOver();
            }
        }
        obstacles.removeAll(toRemoveObstacles);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(groundTexture, 0, 0, Gdx.graphics.getWidth(), groundHeight);

        batch.draw(currentPlaneTexture, planeX, planeY, planeWidth, planeHeight);

        // Oyuncu uçağı çarpışma dikdörtgenini güncelle
        playerPlaneRectangle.set(planeX, planeY, planeWidth, planeHeight);

        for (FlyingEnemy enemy : flyingEnemies) {
            batch.draw(enemyPlaneTexture, enemy.x, enemy.y, enemy.width, enemy.height);
        }

        for (GroundEnemy enemy : groundEnemies) {
            batch.draw(enemyPlaneTexture, enemy.x, groundHeight, enemy.width, enemy.height); // Y koordinatı groundHeight
        }

        for (Creature creature : creatures) {
            batch.draw(creatureTexture, creature.x, creature.y, creature.width, creature.height);
        }

        for (Obstacle obstacle : obstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        batch.end();
    }

    private void updatePlaneTexture() {
        if (currentLevel >= 1 && currentLevel <= 5) {
            currentPlaneTexture = planeTextures[currentLevel - 1];
            planeWidth = currentPlaneTexture.getWidth() / 2;
            planeHeight = currentPlaneTexture.getHeight() / 2;
        }
    }

    private void spawnFlyingEnemy() {
        float enemyX = Gdx.graphics.getWidth();
        float enemyY = random.nextFloat() * (Gdx.graphics.getHeight() - enemyPlaneHeight);
        float enemySpeed = 100 + random.nextFloat() * 100;

        FlyingEnemy enemy = new FlyingEnemy(enemyX, enemyY, enemySpeed, enemyPlaneWidth, enemyPlaneHeight);
        flyingEnemies.add(enemy);
    }

    private void spawnGroundEnemy() {
        float enemyX = Gdx.graphics.getWidth();
        float enemySpeed = 100 + random.nextFloat() * 100;

        GroundEnemy enemy = new GroundEnemy(enemyX, enemySpeed, enemyPlaneWidth, enemyPlaneHeight);
        groundEnemies.add(enemy);
    }

    private void spawnCreature() {
        float creatureX = Gdx.graphics.getWidth();
        float creatureY = groundHeight; // Yaratıklar zeminde
        float creatureSpeed = 50 + random.nextFloat() * 50;
        float creatureWidth = creatureTexture.getWidth() / 2;
        float creatureHeight = creatureTexture.getHeight() / 2;

        Creature creature = new Creature(creatureX, creatureY, creatureSpeed, creatureWidth, creatureHeight);
        creatures.add(creature);
    }

    private void spawnObstacle() {
        float obstacleX = Gdx.graphics.getWidth();
        float obstacleY = random.nextFloat() * (Gdx.graphics.getHeight() - obstacleTexture.getHeight());
        float obstacleSpeed = 0; // Engeller sabit

        Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleTexture.getWidth(), obstacleTexture.getHeight(), obstacleSpeed);
        obstacles.add(obstacle);
    }

    private void gameOver() {
        // Oyun bittiğinde yapılacak işlemler
        System.out.println("Çarpışma! Oyun bitti.");
        Gdx.app.exit(); // Oyun durduruluyor
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        for (Texture texture : planeTextures) {
            texture.dispose();
        }
        enemyPlaneTexture.dispose();
        groundTexture.dispose();
        creatureTexture.dispose();
        obstacleTexture.dispose();
    }

    private static class FlyingEnemy {
        float x, y, speed, width, height;
        Rectangle rectangle;

        FlyingEnemy(float x, float y, float speed, float width, float height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.width = width;
            this.height = height;
            this.rectangle = new Rectangle(x, y, width, height);
        }
    }

    private static class GroundEnemy {
        float x, speed, width, height;
        Rectangle rectangle;

        GroundEnemy(float x, float speed, float width, float height) {
            this.x = x;
            this.speed = speed;
            this.width = width;
            this.height = height;
            this.rectangle = new Rectangle(x, 0, width, height);
        }
    }

    private static class Creature {
        float x, y, speed, width, height;
        Rectangle rectangle;

        Creature(float x, float y, float speed, float width, float height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.width = width;
            this.height = height;
            this.rectangle = new Rectangle(x, y, width, height);
        }
    }

    private static class Obstacle {
        float x, y, width, height, speed;
        Rectangle rectangle;

        Obstacle(float x, float y, float width, float height, float speed) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
            this.rectangle = new Rectangle(x, y, width, height);
        }
    }
}
