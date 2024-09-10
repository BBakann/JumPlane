package com.berdanbakan.jumplane;

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
    private int currentLevel=1;

    private Map<Integer, Vector2> flyingEnemySizes;

    public EnemyManager() {;
        flyingEnemies = new ArrayList<>();
        creatures = new ArrayList<>();
        obstacles = new ArrayList<>();
        random = new Random();
        explosions = new ArrayList<>();
        explosionSound=Gdx.audio.newSound(Gdx.files.internal("explosion01.wav"));
        crashSound=Gdx.audio.newSound(Gdx.files.internal("crash.mp3"));


        flyingEnemySizes = new HashMap<>();
        flyingEnemySizes.put(1, new Vector2(273/1.1f,282/1.1f)); // enemyplane1_1
        flyingEnemySizes.put(2, new Vector2(958 / 3f, 586 / 3f)); // enemyplane2_1
        flyingEnemySizes.put(3, new Vector2(473/2f,468/2f)); // enemyplane3_1
        flyingEnemySizes.put(4, new Vector2(401/1.1f,249/1.1f)); // enemyplane4_1
        flyingEnemySizes.put(5, new Vector2(641 / 3f, 546 / 3f)); // enemyplane5_1

    }

    public void update(Player player, LevelManager levelManager) {

        float deltaTime = Gdx.graphics.getDeltaTime();

        Iterator<FlyingEnemy> flyingEnemyIterator = flyingEnemies.iterator();
        while (flyingEnemyIterator.hasNext()) {
            FlyingEnemy enemy = flyingEnemyIterator.next();
            enemy.update(deltaTime);
            if (enemy.x < -enemy.width) {
                flyingEnemyIterator.remove();
            }
        }

        Iterator<Creature> creatureIterator = creatures.iterator();
        while (creatureIterator.hasNext()) {
            Creature creature = creatureIterator.next();
            creature.update();
            if (creature.x < -creature.width) {
                creatureIterator.remove();
            }
        }

        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            obstacle.update();
            if (obstacle.x < -obstacle.width) {
                obstacleIterator.remove();
            }
        }

        // Patlama efektlerini güncelle
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
            enemy.draw(batch); // FlyingEnemy'nin kendi draw metodunu kullan

            // Düşman mermilerini çiz
            for (Bullet bullet : enemy.getBullets()) {
                batch.draw(enemy.enemyBulletTexture, bullet.x, bullet.y, bullet.width, bullet.height);
            }


        }

        for (Creature creature : creatures) {creature.draw(batch);
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(batch); // Obstacle'ın kendi draw metodunu kullan
        }


        // Patlama efektlerini çiz
        for (Explosion explosion : explosions) {
            explosion.draw(batch);
        }
    }
    public void setLevel(int level) {
         this.currentLevel = level;
    }

    public void spawnFlyingEnemy() {
        float enemyX = Gdx.graphics.getWidth();
        float enemyY = random.nextFloat() * (Gdx.graphics.getHeight() - new Texture("enemyplane5_1.png").getHeight());
        float enemySpeed = 300 + random.nextFloat() * 100;
        Vector2 size = flyingEnemySizes.get(currentLevel);
        float enemyWidth = size.x;
        float enemyHeight = size.y;

        FlyingEnemy enemy;

        switch (currentLevel) {
            case 1:
                enemy = new FlyingEnemy1(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight);
                break;
            case 2:
                enemy = new FlyingEnemy2(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight);
                break;
            case 3:
                enemy = new FlyingEnemy3(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight);
                break;
            case 4:
                enemy = new FlyingEnemy4(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight);
                break;
            case 5:
                enemy = new FlyingEnemy5(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight);
                break;
            default:
                enemy = new FlyingEnemy1(enemyX, enemyY, enemySpeed, enemyWidth, enemyHeight);
                break;
        }

        flyingEnemies.add(enemy);
    }

    public void spawnCreature() {
        float creatureX = Gdx.graphics.getWidth();
        float creatureY = 150; // Yaratıklar zeminde
        float creatureSpeed = 100 + random.nextFloat() * 50;

        Creature creature;
        switch (currentLevel) {
            case 1:
                Texture texture1 = new Texture("creature1_1.png");
                float creatureWidth1 = texture1.getWidth() / 2;
                float creatureHeight1 = texture1.getHeight() / 2;
                creature = new Creature1(creatureX, creatureY, creatureSpeed, creatureWidth1, creatureHeight1);
                texture1.dispose();
                break;
            case 2:
                Texture texture2 = new Texture("creature2_1.png");
                float creatureWidth2 = texture2.getWidth()/1.5f;
                float creatureHeight2 = texture2.getHeight()/1.5f;
                creature = new Creature2(creatureX, creatureY, creatureSpeed, creatureWidth2, creatureHeight2);
                texture2.dispose();
                break;
            case 3:
                Texture texture3 = new Texture("creature3_1.png");
                float creatureWidth3 = texture3.getWidth();
                float creatureHeight3 = texture3.getHeight();
                creature = new Creature3(creatureX, creatureY, creatureSpeed, creatureWidth3, creatureHeight3);
                texture3.dispose();
                break;
            case 4:
                Texture texture4 = new Texture("creature4_1.png");
                float creatureWidth4 = texture4.getWidth() /1.2f;
                float creatureHeight4 = texture4.getHeight() /1.2f;
                creature= new Creature4(creatureX, creatureY, creatureSpeed, creatureWidth4, creatureHeight4);
                texture4.dispose();
                break;
            case 5:
                Texture texture5 = new Texture("creature5_1.png");
                float creatureWidth5 = texture5.getWidth() /1.5f;
                float creatureHeight5 = texture5.getHeight() /1.5f;
                creature = new Creature5(creatureX, creatureY, creatureSpeed, creatureWidth5, creatureHeight5);
                texture5.dispose();
                break;
            default:
                Texture textureDefault = new Texture("creature1_1.png");
                float creatureWidthDefault = textureDefault.getWidth()/2;
                float creatureHeightDefault = textureDefault.getHeight()/2;
                creature = new Creature1(creatureX, creatureY, creatureSpeed, creatureWidthDefault, creatureHeightDefault);
                textureDefault.dispose();
                break;
        }

        creatures.add(creature);
    }

    public void spawnObstacle() {
        float obstacleX = Gdx.graphics.getWidth();
        float obstacleY = random.nextFloat() * (Gdx.graphics.getHeight() - 200) + 200; // Engellerin yüksekliği
        float obstacleSpeed = 100 + random.nextFloat() * 50;
        float obstacleWidth = new Texture("obstacle1.png").getWidth()/1.18f;
        float obstacleHeight = new Texture("obstacle1.png").getHeight()/1.18f;

        Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleSpeed, obstacleWidth, obstacleHeight);
        obstacle.setLevel(currentLevel); // Mevcut seviyeyi obstacle'a ilet
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
                    creature.health -= bullet.damage;
                    if (creature.health <= 0) {
                        creatureIterator.remove();
                        explosions.add(new Explosion(creature.x + creature.width / 2, creature.y + creature.height / 2, false));
                        killedEnemies++;
                    } else {explosions.add(new Explosion(creature.x + creature.width / 2, creature.y + creature.height / 2, true));
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
                    // Sadece patlama efekti oluştur
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
                flyingEnemyIterator.remove(); // Düşmanı yok et
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
                if (player.health<= 0) {
                    levelManager.gameOver();
                }
                explosions.add(new Explosion(obstacle.x + obstacle.width/ 2, obstacle.y + obstacle.height /2, false));
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
                    bulletIterator.remove(); // Mermiyi yok et


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
            for (TextureRegion frame : explosion.animation.getKeyFrames()) {
                frame.getTexture().dispose();
            }
        }
    }
}