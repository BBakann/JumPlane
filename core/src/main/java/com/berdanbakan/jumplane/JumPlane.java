package com.berdanbakan.jumplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

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
    private Texture[] healthTextures;
    private BitmapFont font;
    private List<Bullet> bullets;
    private Texture[] bulletTextures;


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

    private Rectangle playerPlaneRectangle;

    private float planeX; // Uçağın yatay pozisyonu (sabit)
    private float planeY; // Uçağın dikey pozisyonu

    private float flyingEnemySpawnInterval = 10f; // Düşman uçaklarının oluşturulma aralığı
    private float groundEnemySpawnInterval = 12f; // Zemin düşmanlarının oluşturulma aralığı
    private float creatureSpawnInterval = 15f; // Yaratıkların oluşturulma aralığı
    private float obstacleSpawnInterval = 18f; // Engellerin oluşturulma aralığı

    private int health = 6;
    private boolean isGameOver;
    private int level=1;

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

        healthTextures = new Texture[7];
        for (int i = 0; i < healthTextures.length; i++) {
            healthTextures[i] = new Texture("health" + i + ".png");
        }

        bulletTextures=new Texture[4];
        for (int i=0;i<bulletTextures.length;i++){
            bulletTextures[i]=new Texture("bullet"+(i+1)+".png");
        }




        font = new BitmapFont();
        font.getData().setScale(4); // Yazı boyutunu ayarla

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
        bullets=new ArrayList<>();
        random = new Random();

        playerPlaneRectangle = new Rectangle();

        // Uçağın başlangıç pozisyonunu ayarla
        planeX = 0; // Ekranın en sol noktası
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2; // Y ekseninde ortala

        isGameOver = false;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (isGameOver) {
                    resetGame();
                } else {
                    planeY = Gdx.graphics.getHeight() - screenY - planeHeight / 2;
                    shootBullet();
                }
                return true;
            }
        });

        // Düşman uçakları oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isGameOver) spawnFlyingEnemy();
            }
        }, flyingEnemySpawnInterval, flyingEnemySpawnInterval);

        // Zemin düşmanları oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isGameOver) spawnGroundEnemy();
            }
        }, groundEnemySpawnInterval, groundEnemySpawnInterval);

        // Yaratıklar oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isGameOver) spawnCreature();
            }
        }, creatureSpawnInterval, creatureSpawnInterval);

        // Engeller oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isGameOver) spawnObstacle();
            }
        }, obstacleSpawnInterval, obstacleSpawnInterval);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        updatePlaneTexture();

        if (!isGameOver) {
            // Uçak kontrolü
            if (Gdx.input.isTouched()) {
                planeY = Gdx.graphics.getHeight() - Gdx.input.getY() - planeHeight / 2;
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
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                    toRemoveFlyingEnemies.add(enemy);
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
                enemy.rectangle.set(enemy.x, groundHeight, enemy.width, enemy.height);
                if (enemy.rectangle.overlaps(playerPlaneRectangle)) {
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                    toRemoveGroundEnemies.add(enemy);
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
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                    toRemoveCreatures.add(creature);
                }
            }
            creatures.removeAll(toRemoveCreatures);

            // Engeller ile çarpışma kontrolü
            List<Obstacle> toRemoveObstacles = new ArrayList<>();
            for (Obstacle obstacle : obstacles) {
                obstacle.x -= obstacle.speed * Gdx.graphics.getDeltaTime();
                if (obstacle.x < -obstacle.width) {
                    toRemoveObstacles.add(obstacle);
                }
                obstacle.rectangle.set(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
                if (obstacle.rectangle.overlaps(playerPlaneRectangle)) {
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                }
            }
            obstacles.removeAll(toRemoveObstacles);

            // Mermileri hareket ettir ve temizle
            List<Bullet> toRemoveBullets = new ArrayList<>();
            for (Bullet bullet : bullets) {
                bullet.x += bullet.speed * Gdx.graphics.getDeltaTime();
                if (bullet.x > Gdx.graphics.getWidth()) {
                    toRemoveBullets.add(bullet);
                }
                bullet.rectangle.set(bullet.x, bullet.y, bullet.width, bullet.height);

                // Mermilerin düşmanlarla çarpışmasını kontrol et
                for (FlyingEnemy enemy : flyingEnemies) {
                    if (bullet.rectangle.overlaps(enemy.rectangle)) {
                        toRemoveBullets.add(bullet);
                        flyingEnemies.remove(enemy);
                        // Patlama efekti veya diğer çarpışma etkilerini burada başlatabilirsiniz
                        break;
                    }
                }

                for (GroundEnemy enemy : groundEnemies) {
                    if (bullet.rectangle.overlaps(enemy.rectangle)) {
                        toRemoveBullets.add(bullet);
                        groundEnemies.remove(enemy);
                        // Patlama efekti veya diğer çarpışma etkilerini burada başlatabiliriz.
                        break;
                    }
                }


            }
            bullets.removeAll(toRemoveBullets);
        }

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

        batch.draw(healthTextures[health], 10, Gdx.graphics.getHeight() - 50, 200, 50);

        for (Bullet bullet : bullets) {
            batch.draw(bullet.texture, bullet.x, bullet.y, bullet.width, bullet.height);
        }

        if (isGameOver) {
            font.draw(batch, "GAME OVER!", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);
        }

        font.draw(batch, "Level:" + level, 20, Gdx.graphics.getHeight() - 80);

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
        float enemySpeed = 300 + random.nextFloat() * 100; // Hız artırıldı

        FlyingEnemy enemy = new FlyingEnemy(enemyX, enemyY, enemySpeed, enemyPlaneWidth, enemyPlaneHeight);
        flyingEnemies.add(enemy);
    }

    private void spawnGroundEnemy() {
        float enemyX = Gdx.graphics.getWidth();
        float enemySpeed = 150 + random.nextFloat() * 50; // Hız artırıldı

        GroundEnemy enemy = new GroundEnemy(enemyX, enemySpeed, enemyPlaneWidth, enemyPlaneHeight);
        groundEnemies.add(enemy);
    }

    private void spawnCreature() {
        float creatureX = Gdx.graphics.getWidth();
        float creatureY = groundHeight; // Yaratıklar zeminde
        float creatureSpeed = 100 + random.nextFloat() * 50; // Hız artırıldı
        float creatureWidth = creatureTexture.getWidth() / 2;
        float creatureHeight = creatureTexture.getHeight() / 2;

        Creature creature = new Creature(creatureX, creatureY, creatureSpeed, creatureWidth, creatureHeight);
        creatures.add(creature);
    }

    private void spawnObstacle() {
        float obstacleX = Gdx.graphics.getWidth();
        float obstacleY = random.nextFloat() * (Gdx.graphics.getHeight() - groundHeight);
        float obstacleSpeed = 100 + random.nextFloat() * 50; // Hız artırıldı
        float obstacleWidth = obstacleTexture.getWidth() / 2;
        float obstacleHeight = obstacleTexture.getHeight() / 2;

        Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleSpeed, obstacleWidth, obstacleHeight);
        obstacles.add(obstacle);
    }

    private void shootBullet() {
        if (!isGameOver) {
            Texture bulletTexture = bulletTextures[Math.min(level - 1, bulletTextures.length - 1)];
            float bulletWidth = bulletTexture.getWidth() / 5;
            float bulletHeight = bulletTexture.getHeight() / 5;
            float bulletSpeed = 500; // Mermi hızını ayarla

            Bullet bullet = new Bullet(planeX + planeWidth, planeY + planeHeight / 2 - bulletHeight / 2, bulletSpeed, bulletTexture, bulletWidth, bulletHeight);
            bullets.add(bullet);
        }
    }


    private void gameOver() {
        isGameOver = true;
        // Oyun bittiğinde yapılacak işlemler
        health = 6;
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2; // Uçağın pozisyonunu sıfırla
        flyingEnemies.clear();
        groundEnemies.clear();
        creatures.clear();
        obstacles.clear();
    }
    public void levelUp(){
        level++;
    }

    private void resetGame() {
        isGameOver = false;
        health = 6;
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2; // Uçağın pozisyonunu sıfırla
        flyingEnemies.clear();
        groundEnemies.clear();
        creatures.clear();
        obstacles.clear();
        Timer.instance().clear();


        // Düşman uçakları oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnFlyingEnemy();
            }
        }, flyingEnemySpawnInterval, flyingEnemySpawnInterval);

        // Zemin düşmanları oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnGroundEnemy();
            }
        }, groundEnemySpawnInterval, groundEnemySpawnInterval);

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

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        for (Texture planeTexture : planeTextures) {
            planeTexture.dispose();
        }
        enemyPlaneTexture.dispose();
        groundTexture.dispose();
        creatureTexture.dispose();
        obstacleTexture.dispose();
        for (Texture healthTexture : healthTextures) {
            healthTexture.dispose();
        }
        font.dispose();
    }

}
