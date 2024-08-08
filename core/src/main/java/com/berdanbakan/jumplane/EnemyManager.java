package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemyManager {
    private Texture enemyPlaneTexture;
    private float enemyPlaneWidth;
    private float enemyPlaneHeight;
    private Texture creatureTexture;
    private Texture obstacleTexture;
    private List<FlyingEnemy> flyingEnemies;
    private List<Creature> creatures;
    private List<Obstacle> obstacles;
    public int killedEnemies = 0;
    private Random random;
    private List<Explosion> explosions;

    public EnemyManager() {
        enemyPlaneTexture = new Texture("enemyplane.png");
        enemyPlaneWidth= enemyPlaneTexture.getWidth() / 4;
        enemyPlaneHeight = enemyPlaneTexture.getHeight() / 4;
        creatureTexture = new Texture("creature.png");
        obstacleTexture = new Texture("obstacle.png");
        flyingEnemies = new ArrayList<>();
        creatures = new ArrayList<>();
        obstacles = new ArrayList<>();
        random = new Random();
        explosions = new ArrayList<>();
    }

    public void update(Player player, LevelManager levelManager) {

        Iterator<FlyingEnemy> flyingEnemyIterator = flyingEnemies.iterator();
        while (flyingEnemyIterator.hasNext()) {
            FlyingEnemy enemy = flyingEnemyIterator.next();
            enemy.update();
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
            batch.draw(enemyPlaneTexture, enemy.x, enemy.y, enemy.width, enemy.height);
        }

        for (Creature creature : creatures) {
            batch.draw(creatureTexture, creature.x, creature.y, creature.width, creature.height);
        }

        for (Obstacle obstacle : obstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Patlama efektlerini çiz
        for (Explosion explosion : explosions) {
            explosion.draw(batch);
        }
    }

    public void spawnFlyingEnemy() {
        float enemyX = Gdx.graphics.getWidth();
        float enemyY = random.nextFloat() * (Gdx.graphics.getHeight() - enemyPlaneHeight);
        float enemySpeed = 300 + random.nextFloat() * 100;

        FlyingEnemy enemy = new FlyingEnemy(enemyX, enemyY, enemySpeed, enemyPlaneWidth, enemyPlaneHeight);
        flyingEnemies.add(enemy);
    }

    public void spawnCreature() {
        float creatureX = Gdx.graphics.getWidth();
        float creatureY = 150; // Yaratıklar zeminde
        float creatureSpeed = 100 + random.nextFloat() * 50; // Hız artırıldı
        float creatureWidth = creatureTexture.getWidth() / 2;
        float creatureHeight = creatureTexture.getHeight() / 2;

        Creature creature = new Creature(creatureX, creatureY, creatureSpeed, creatureWidth, creatureHeight);
        creatures.add(creature);
    }

    public void spawnObstacle() {
        float obstacleX = Gdx.graphics.getWidth();
        float obstacleY = random.nextFloat() * (Gdx.graphics.getHeight() - 200) + 200; // Engellerin yüksekliği
        float obstacleSpeed = 100 + random.nextFloat() * 50; // Hız artırıldı
        float obstacleWidth = obstacleTexture.getWidth();
        float obstacleHeight = obstacleTexture.getHeight();

        Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleSpeed, obstacleWidth, obstacleHeight);
        obstacles.add(obstacle);
    }


    //MERMİLERİN DÜŞMANLA ÇARPIŞMASI KONTROLÜ
    private void checkBulletCollisions(Player player, LevelManager levelManager) {

        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : player.getBullets()) {
            // Düşman uçakları ile çarpışma kontrolü
            Iterator<FlyingEnemy> flyingEnemyIterator = flyingEnemies.iterator();
            while (flyingEnemyIterator.hasNext()) {
                FlyingEnemy enemy = flyingEnemyIterator.next();
                if (bullet.rectangle.overlaps(enemy.rectangle)) {
                    bulletsToRemove.add(bullet);
                    enemy.health -= bullet.damage;
                    if (enemy.health <= 0){
                        flyingEnemyIterator.remove();
                        explosions.add(new Explosion(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, false));
                        killedEnemies++; // Düşman öldürüldüğünde sayacı artır
                    } else {
                        explosions.add(new Explosion(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, true));
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
                        killedEnemies++; // Yaratık öldürüldüğünde sayacı artır
                    } else {
                        explosions.add(new Explosion(creature.x + creature.width / 2, creature.y + creature.height / 2, true));
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
                    break;}
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
                if (player.health<= 0) {
                    levelManager.gameOver();
                }
                explosions.add(new Explosion(obstacle.x + obstacle.width/ 2, obstacle.y + obstacle.height /2, false));
                obstacleIterator.remove();
            }
        }   // Oyuncu uçağının sınırlarını güncelle
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
        enemyPlaneTexture.dispose();
        creatureTexture.dispose();
        obstacleTexture.dispose();
        // Patlama efektlerini dispose et
        for (Explosion explosion : explosions) {
            for (TextureRegion frame : explosion.animation.getKeyFrames()) {
                frame.getTexture().dispose();
            }
        }
    }
}
