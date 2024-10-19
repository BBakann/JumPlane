package com.berdanbakan.jumplane;

import static com.berdanbakan.jumplane.FlyingEnemy.enemyBulletTexture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnemyManager {
    private List<FlyingEnemy> flyingEnemies;
    private List<Creature> creatures;
    private List<Obstacle> obstacles;
    public int killedEnemies = 0;
    private Random random;
    private List<Explosion> explosions;
    private Sound explosionSound;
    private boolean soundPlayed;
    private Sound crashSound;
    private int currentLevel = 1;

    private Map<Integer, Vector2> flyingEnemySizes;

    private float creatureSpawnTimer;
    private float creatureSpawnDelay;

    private float flyingEnemySpawnTimer;
    private float flyingEnemySpawnDelay;

    private float obstacleSpawnTimer;
    private float obstacleSpawnDelay;

    private  Player player;

    public EnemyManager() {

        flyingEnemies = new ArrayList<>();
        creatures = new ArrayList<>();
        obstacles = new ArrayList<>();
        random = new Random();
        explosions = new ArrayList<>();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion01.wav"));
        crashSound = Gdx.audio.newSound(Gdx.files.internal("crash.mp3"));


        flyingEnemySizes = new HashMap<>();
        flyingEnemySizes.put(1, new Vector2(273 / 1.1f, 282 / 1.1f)); // enemyplane1_1
        flyingEnemySizes.put(2, new Vector2(958 / 3f, 586 / 3f)); // enemyplane2_1
        flyingEnemySizes.put(3, new Vector2(473 / 2f, 468 / 2f)); // enemyplane3_1
        flyingEnemySizes.put(4, new Vector2(401 / 1.1f, 249 / 1.1f)); // enemyplane4_1
        flyingEnemySizes.put(5, new Vector2(641 / 3f, 546 / 3f)); // enemyplane5_1

    }

    public void setLevel(int level) {
        this.currentLevel = level;

        // Level'e göre gecikme süresi ayarı
        switch (level) {
            case 1:
                creatureSpawnDelay = 6f;
                flyingEnemySpawnDelay = 5f;
                obstacleSpawnDelay = 8f;
                break;
            case 2:
                creatureSpawnDelay = 5.5f;
                flyingEnemySpawnDelay = 4.5f;
                obstacleSpawnDelay = 7.25f;
                break;
            case 3:
                creatureSpawnDelay = 4.75f;
                flyingEnemySpawnDelay = 4f;
                obstacleSpawnDelay = 6.5f;
                break;
            case 4:
                creatureSpawnDelay = 4f;
                flyingEnemySpawnDelay = 3.4f;
                obstacleSpawnDelay = 5.75f;
                break;
            case 5:
                creatureSpawnDelay = 3.25f;
                flyingEnemySpawnDelay = 3f;
                obstacleSpawnDelay = 5.25f;
                break;
            default:

                creatureSpawnDelay = 6f;
                flyingEnemySpawnDelay = 5f;
                obstacleSpawnDelay = 8f;
                break;
        }
    }

    public void update(Player player, LevelManager levelManager) {
        this.player = player;
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (creatureSpawnTimer >= creatureSpawnDelay) {
            spawnCreature();
            creatureSpawnTimer = 0;
        } else {
            creatureSpawnTimer += Gdx.graphics.getDeltaTime();
        }

        if (flyingEnemySpawnTimer >= flyingEnemySpawnDelay) {
            spawnFlyingEnemy();
            flyingEnemySpawnTimer = 0;
        } else {
            flyingEnemySpawnTimer += Gdx.graphics.getDeltaTime();
        }

        if (obstacleSpawnTimer >= obstacleSpawnDelay) {
            spawnObstacle();
            obstacleSpawnTimer = 0;
        } else {
            obstacleSpawnTimer += Gdx.graphics.getDeltaTime();
        }

        Iterator<FlyingEnemy> flyingEnemyIterator = flyingEnemies.iterator();
        while (flyingEnemyIterator.hasNext()) {
            FlyingEnemy enemy = flyingEnemyIterator.next();
            enemy.update(Gdx.graphics.getDeltaTime());
            if (enemy.x < -enemy.width) {
                enemy.dispose();
                flyingEnemyIterator.remove();
                if (killedEnemies>0){
                    killedEnemies--;
                }
            }
        }

        Iterator<Creature> creatureIterator = creatures.iterator();
        while (creatureIterator.hasNext()) {
            Creature creature = creatureIterator.next();
            creature.update(deltaTime);
            if (creature.x < -creature.width) {
                creature.dispose();
                creatureIterator.remove();
                if (killedEnemies>0){
                    killedEnemies--;
                }

            }
        }

        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            obstacle.update();
            if (obstacle.x < -obstacle.width) {
                obstacle.dispose();//
                obstacleIterator.remove();
            }
        }


        Iterator<Explosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            explosion.update(Gdx.graphics.getDeltaTime());
            if (explosion.isFinished()) {
                explosionIterator.remove();
            }
        }

        checkBulletCollisions(player, levelManager);
    }

    public void draw(SpriteBatch batch) {


        for (FlyingEnemy enemy : flyingEnemies) {
            enemy.draw(batch);
            for (Bullet bullet : enemy.getBullets()) {
                batch.draw(enemyBulletTexture, bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }


        for (Creature creature : creatures) {
            creature.draw(batch);
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(batch);
        }



        for (Explosion explosion : explosions) {
            explosion.draw(batch);
        }
    }

    public void spawnFlyingEnemy() {
        float enemyX = Gdx.graphics.getWidth();
        float enemyY = random.nextFloat() * (Gdx.graphics.getHeight() - flyingEnemySizes.get(5).y);
        float enemySpeed = 300 + random.nextFloat() * 100;
        Vector2 size;
        float enemyWidth;
        float enemyHeight;

        FlyingEnemy enemy;

        if (currentLevel == 6) { // Free Level
            int randomLevel = random.nextInt(5) + 1;
            size = flyingEnemySizes.get(randomLevel);
            enemyWidth = size.x;
            enemyHeight = size.y;

            switch (randomLevel){
                case 1:
                    enemy = new FlyingEnemy1(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 2:
                    enemy = new FlyingEnemy2(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 3:
                    enemy = new FlyingEnemy3(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 4:
                    enemy = new FlyingEnemy4(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 5:
                    enemy = new FlyingEnemy5(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                default:
                    enemy = new FlyingEnemy1(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
            }
        } else {
            size = flyingEnemySizes.get(currentLevel);
            enemyWidth = size.x;
            enemyHeight = size.y;switch (currentLevel) {
                case 1:
                    enemy = new FlyingEnemy1(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 2:
                    enemy = new FlyingEnemy2(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 3:
                    enemy = new FlyingEnemy3(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 4:
                    enemy = new FlyingEnemy4(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                case 5:
                    enemy = new FlyingEnemy5(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
                default:
                    enemy = new FlyingEnemy1(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight, player);
                    break;
            }
        }

        flyingEnemies.add(enemy);
    }

    public void spawnCreature() {
        float creatureX = Gdx.graphics.getWidth();
        float creatureY = 150;
        float creatureSpeed = 100 + random.nextFloat() * 50;

        Creature creature;

        if (currentLevel == 6) { // Free Level
            int randomLevel = random.nextInt(5) + 1;

            switch (randomLevel) {
                case 1:
                    creature = new Creature1(creatureX, creatureY, creatureSpeed);
                    creature.health = 2 + (int) (Math.random() * 2);
                    break;
                case 2:
                    creature = new Creature2(creatureX, creatureY, creatureSpeed);
                    creature.health = 2 + (int) (Math.random() * 3);
                    break;
                case 3:
                    creature = new Creature3(creatureX, creatureY, creatureSpeed);
                    creature.health = 3 + (int) (Math.random() * 4);
                    break;
                case 4:
                    creature = new Creature4(creatureX, creatureY, creatureSpeed);creature.health = 3 + (int) (Math.random() * 4);
                    break;
                case 5:
                    creature = new Creature5(creatureX, creatureY, creatureSpeed);
                    creature.health = 4 + (int) (Math.random() * 5);
                    break;
                default:
                    creature = new Creature1(creatureX, creatureY, creatureSpeed);
                    creature.health = 2 + (int) (Math.random() * 3);
                    break;
            }
        } else {
            switch (currentLevel) {
                case 1:
                    creature = new Creature1(creatureX, creatureY, creatureSpeed);
                    creature.health = 2 + (int) (Math.random() * 2);
                    break;
                case 2:
                    creature = new Creature2(creatureX, creatureY, creatureSpeed);
                    creature.health = 2 + (int) (Math.random() * 3); // 2 veya 3 can
                    break;
                case 3:
                    creature = new Creature3(creatureX, creatureY, creatureSpeed);
                    creature.health = 3 + (int) (Math.random() * 4);
                    break;
                case 4:
                    creature = new Creature4(creatureX, creatureY, creatureSpeed);
                    creature.health = 3 + (int) (Math.random() * 4);
                    break;
                case 5:
                    creature = new Creature5(creatureX, creatureY, creatureSpeed);
                    creature.health = 4 + (int) (Math.random() * 5);
                    break;
                default:
                    creature = new Creature1(creatureX, creatureY, creatureSpeed);
                    creature.health = 2 + (int) (Math.random() * 3);
                    break;
            }
        }

        creatures.add(creature);
    }

    public void spawnObstacle() {
        float obstacleX = Gdx.graphics.getWidth();
        float obstacleY = random.nextFloat() * (Gdx.graphics.getHeight() - 200) + 200; // Engellerin yüksekliği
        float obstacleSpeed = 100 + random.nextFloat() * 50;
        float obstacleWidth = new Texture("obstacle1.png").getWidth() / 1.18f;
        float obstacleHeight = new Texture("obstacle1.png").getHeight() / 1.18f;

        Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleSpeed, obstacleWidth, obstacleHeight);

        if (currentLevel == 6) { // Free Level
            int randomLevel = random.nextInt(5) + 1;
            obstacle.setLevel(randomLevel);
        } else {
            obstacle.setLevel(currentLevel);
        }

        obstacles.add(obstacle);
    }


    private void checkBulletCollisions(Player player, LevelManager levelManager) {
        soundPlayed = false;

        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : player.getBullets()) {
            // Düşman uçakları ile çarpışma kontrolü
            Iterator<FlyingEnemy> flyingEnemyIterator = flyingEnemies.iterator();
            while (flyingEnemyIterator.hasNext()) {
                FlyingEnemy enemy = flyingEnemyIterator.next();
                if (bullet.rectangle.overlaps(enemy.rectangle)) {
                    bulletsToRemove.add(bullet);
                    enemy.health -= bullet.damage;
                    if (enemy.health <= 0) {
                        flyingEnemyIterator.remove();
                        explosions.add(new Explosion(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, false));
                        killedEnemies++;
                    } else {
                        explosions.add(new Explosion(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, true));
                    }

                    if (!soundPlayed) {
                        explosionSound.play();
                        soundPlayed = true;
                    }
                    break;
                }
            }

            // Yaratıklar ile çarpışma kontrolü
            Iterator<Creature> creatureIterator = creatures.iterator();
            while (creatureIterator.hasNext()) {
                Creature creature = creatureIterator.next();
                if (bullet.rectangle.overlaps(creature.rectangle)) {
                    bulletsToRemove.add(bullet);
                    int damage = 1 + (int) (Math.random() * 2); // 1 veya 2 hasar
                    creature.health -= damage;
                    if (creature.health <= 0) {
                        creatureIterator.remove();
                        explosions.add(new Explosion(creature.x + creature.width / 2, creature.y + creature.height / 2, false));killedEnemies++;
                    } else {
                        explosions.add(new Explosion(creature.x + creature.width / 2, creature.y + creature.height / 2, true));
                    }

                    if (!soundPlayed) {
                        explosionSound.play();
                        soundPlayed = true;
                    }
                    break;
                }
            }

            // Engeller ile çarpışma kontrolü
            Iterator<Obstacle> obstacleIterator = obstacles.iterator();
            while (obstacleIterator.hasNext()) {
                Obstacle obstacle = obstacleIterator.next();
                if (bullet.rectangle.overlaps(obstacle.rectangle)) {
                    bulletsToRemove.add(bullet);

                    explosions.add(new Explosion(bullet.x, bullet.y, true));

                    if (!soundPlayed) {
                        explosionSound.play();
                        soundPlayed = true;
                    }
                    break;
                }
            }

            // Düşman mermileri ile çarpışma kontrolü
            Iterator<FlyingEnemy> enemyIterator = flyingEnemies.iterator();
            while (enemyIterator.hasNext()) {
                FlyingEnemy enemy = enemyIterator.next();
                Iterator<Bullet> bulletIterator = enemy.getBullets().iterator();
                while (bulletIterator.hasNext()) {
                    Bullet enemyBullet = bulletIterator.next();
                    if (enemyBullet.rectangle.overlaps(player.playerPlaneRectangle)) {
                        player.health--;
                        if (player.health <= 0) {
                            levelManager.gameOver();
                        }
                        bulletIterator.remove(); // Mermiyi yok et
                        explosions.add(new Explosion(player.planeX + player.planeWidth / 2, player.planeY + player.planeHeight / 2, false)); // Patlama efekti ekle

                        if (!soundPlayed) {
                            explosionSound.play();
                            soundPlayed = true;
                        }
                    }
                }
            }
        }
        player.getBullets().removeAll(bulletsToRemove);
    }

    public void checkCollisions(Player player, LevelManager levelManager) {

        Iterator<FlyingEnemy> flyingEnemyIterator = flyingEnemies.iterator();
        while (flyingEnemyIterator.hasNext()) {
            FlyingEnemy enemy = flyingEnemyIterator.next();
            if (enemy.rectangle.overlaps(player.playerPlaneRectangle)) {
                player.health--;
                crashSound.play(150f);
                if (player.health <= 0) {
                    levelManager.gameOver();
                }
                explosions.add(new Explosion(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, false));
                flyingEnemyIterator.remove();
            }
        }

        // Yaratıklar ile çarpışma kontrolü
        Iterator<Creature> creatureIterator = creatures.iterator();
        while (creatureIterator.hasNext()) {
            Creature creature = creatureIterator.next();
            if (creature.rectangle.overlaps(player.playerPlaneRectangle)) {
                player.health--;
                crashSound.play(150f);
                if (player.health <= 0) {
                    levelManager.gameOver();
                }
                explosions.add(new Explosion(creature.x + creature.width / 2, creature.y + creature.height / 2, false));
                creatureIterator.remove();
            }
        }

        // Engeller ile çarpışma kontrolü
        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            if (obstacle.rectangle.overlaps(player.playerPlaneRectangle)) {
                player.health--;
                crashSound.play(150f);
                if (player.health <= 0) {
                    levelManager.gameOver();
                }
                explosions.add(new Explosion(obstacle.x + obstacle.width / 2, obstacle.y + obstacle.height / 2, false));
                obstacleIterator.remove();
            }
        }
        // Düşman mermileri ile çarpışma kontrolü
        Iterator<FlyingEnemy> enemyIterator = flyingEnemies.iterator();
        while (enemyIterator.hasNext()) {
            FlyingEnemy enemy = enemyIterator.next();
            Iterator<Bullet> bulletIterator = enemy.getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet enemyBullet = bulletIterator.next();
                if (enemyBullet.rectangle.overlaps(player.playerPlaneRectangle)) {
                    player.health--;
                    if (player.health <= 0) {
                        levelManager.gameOver();
                    }
                    bulletIterator.remove();


                    explosions.add(new Explosion(player.planeX + player.planeWidth / 2, player.planeY + player.planeHeight / 2, false));
                    explosionSound.play();
                }
            }
        }


        // Oyuncu uçağının sınırlarını güncelle
        player.playerPlaneRectangle.set(player.planeX, player.planeY, player.planeWidth, player.planeHeight);

    }

    public void reset() {
        flyingEnemies.clear();
        creatures.clear();
        obstacles.clear();
        explosions.clear();
        killedEnemies = 0;

    }

    public void dispose() {
        explosionSound.dispose();
        crashSound.dispose();

        for (Explosion explosion : explosions) {
            explosion.dispose();
        }
    }
}