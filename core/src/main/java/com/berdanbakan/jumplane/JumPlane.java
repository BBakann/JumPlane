package com.berdanbakan.jumplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class JumPlane extends ApplicationAdapter implements InputProcessor {

    // SpriteBatch ve dokular
    private SpriteBatch batch;
    private Texture background;private Texture[] planeTextures;
    private Texture currentPlaneTexture;
    private Texture enemyPlaneTexture;
    private Texture groundTexture;
    private Texture creatureTexture;
    private Texture obstacleTexture;
    private Texture[] healthTextures;

    // Yazı tipi ve mermi dokuları
    private BitmapFont font;
    private List<Bullet> bullets;
    private Texture[] bulletTextures;
    private Texture[] ammoTextures;

    // Dokunma kontrolü
    private float touchStartX, touchStartY;


    // Ateş etme düğmesi
    private Texture shootButtonTexture;
    private Texture shootButtonPressedTexture;
    private boolean isButtonPressed;
    private Rectangle buttonRectangle;
    private boolean dugmeGeciciOlarakBasili;


    // Oyun seviyesi ve uçak boyutları
    private int currentLevel;
    private float planeWidth;
    private float planeHeight;
    private float enemyPlaneWidth;
    private float enemyPlaneHeight;
    private float groundHeight;


    // Oyun nesneleri listeleri
    private List<FlyingEnemy> flyingEnemies;
    private List<GroundEnemy> groundEnemies;
    private List<Creature> creatures;
    private List<Obstacle> obstacles;

    //patlama efektleri listesi
    private List<Explosion>explosions = new ArrayList<>();


    // Rastgele sayı üreteci
    private Random random;


    // Oyuncu uçağı çarpışma alanı
    private Rectangle playerPlaneRectangle;

    // Uçak pozisyonu
    private float planeX; // Uçağın yatay pozisyonu (sabit)
    private float planeY; // Uçağın dikey pozisyonu

    // Oyun nesnelerinin oluşturulma aralıkları
    private float flyingEnemySpawnInterval = 10f;
    private float groundEnemySpawnInterval = 12f;
    private float creatureSpawnInterval = 15f;
    private float obstacleSpawnInterval = 18f;


    // Oyun durumu ve seviye
    private int health = 6;
    private boolean isGameOver;
    private int level = 1;

    // Mermi sayısı ve yeniden doldurma süresi
    private static final int MAX_AMMO = 6;
    private int ammo = MAX_AMMO;
    private float reloadTime = 0f;

    // Sabitler
    private static final float SHOOT_DELAY = 0.2f;



    private float touchDownTime;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png"); // Arka plan dokusunu yükle


        //Uçak dokularını yükle
        planeTextures = new Texture[5];
        for (int i = 0; i < 5; i++) {
            planeTextures[i] = new Texture("plane" + (i + 1) + ".png");
        }
        currentLevel = 1;
        updatePlaneTexture();
        Gdx.input.setInputProcessor(this);


        //Düşman uçağı dokusunu yükle ve boyut ayarı
        enemyPlaneTexture = new Texture("enemyplane.png");
        enemyPlaneWidth = enemyPlaneTexture.getWidth() / 4;
        enemyPlaneHeight = enemyPlaneTexture.getHeight() / 4;


        //Zemin dokusunu yükle ve yüksekliğini ayarla
        groundTexture = new Texture("ground.png");
        groundHeight = groundTexture.getHeight()*1.5f;


        //Yaratık ve engel dokularını yükle
        creatureTexture = new Texture("creature.png");
        obstacleTexture = new Texture("obstacle.png");

        //Can dokularını yükle
        healthTextures = new Texture[7];
        for (int i = 0; i < 7; i++) {
            healthTextures[i] = new Texture("health" + i + ".png");
        }



        //Ateş etme düğmesi dokularını yükle ve düğme alanı oluşturma
        shootButtonTexture=new Texture("shootbutton.png");
        shootButtonPressedTexture=new Texture("shootbuttonpressed.png");
        buttonRectangle = new Rectangle(Gdx.graphics.getWidth() - 248,50, 148, 132);
        isButtonPressed=false;


        //Yazı tipini oluşturma ve ölçeklendirme ve renk ayarı
        font = new BitmapFont();
        font.getData().setScale(3.5f);
        font.setColor(Color.BLACK);


        //Mermi listesini ve mermi dokularını oluşturma
        bullets = new ArrayList<>();
        bulletTextures = new Texture[3];
        for (int i = 0; i < 3; i++) {
            bulletTextures[i] = new Texture("bullet" + (i + 1) + ".png");
        }

        //Mermi sayısının dokularını yükle
        ammoTextures = new Texture[7];
        for (int i = 0; i < 7; i++) {
            ammoTextures[i] = new Texture("ammo" + i + ".png");
        }


        //OYUN NESNELERİ LİSTESİNİ OLUŞTURMA
        flyingEnemies = new ArrayList<>();
        groundEnemies = new ArrayList<>();
        creatures = new ArrayList<>();
        obstacles = new ArrayList<>();

        //Rastgele sayı üreteci oluşturma
        random = new Random();

        //Uçağın çarpışma alanını oluşturma
        playerPlaneRectangle = new Rectangle();

        //UÇAĞIN BAŞLANGIÇ POZİSYONUNU AYARLA
        planeX = 50;
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2;

        resetGame(); // Oyunu başlat
    }
    @Override
    public void render() {
        //Ekranı temizle
        ScreenUtils.clear(0, 0, 0, 1);

        //Uçak dokusunu güncelle
        updatePlaneTexture();

        if (!isGameOver) {
            // Uçan Düşmanlar
            Iterator<FlyingEnemy> flyingEnemyIterator = flyingEnemies.iterator();
            List<FlyingEnemy> flyingEnemiesToRemove = new ArrayList<>();
            while (flyingEnemyIterator.hasNext()) {
                FlyingEnemy enemy = flyingEnemyIterator.next();
                enemy.x -= enemy.speed * Gdx.graphics.getDeltaTime();
                if (enemy.x < -enemy.width) {
                    flyingEnemiesToRemove.add(enemy);
                }
                enemy.rectangle.set(enemy.x, enemy.y, enemy.width, enemy.height);
                if (enemy.rectangle.overlaps(playerPlaneRectangle)) {
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                    flyingEnemiesToRemove.add(enemy);
                }
            }
            flyingEnemies.removeAll(flyingEnemiesToRemove);

            // Zemin Düşmanları
            Iterator<GroundEnemy> groundEnemyIterator= groundEnemies.iterator();
            List<GroundEnemy> groundEnemiesToRemove = new ArrayList<>();
            while (groundEnemyIterator.hasNext()) {
                GroundEnemy enemy = groundEnemyIterator.next();
                enemy.x -= enemy.speed * Gdx.graphics.getDeltaTime();
                if (enemy.x < -enemy.width) {
                    groundEnemiesToRemove.add(enemy);
                }
                enemy.rectangle.set(enemy.x, groundHeight, enemy.width, enemy.height);
                if (enemy.rectangle.overlaps(playerPlaneRectangle)) {
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                    groundEnemiesToRemove.add(enemy);
                }
            }
            groundEnemies.removeAll(groundEnemiesToRemove);

            // Yaratıklar
            Iterator<Creature> creatureIterator = creatures.iterator();
            List<Creature> creaturesToRemove = new ArrayList<>();
            while (creatureIterator.hasNext()) {
                Creature creature = creatureIterator.next();
                creature.x -= creature.speed * Gdx.graphics.getDeltaTime();
                if (creature.x < -creature.width) {
                    creaturesToRemove.add(creature);
                }
                creature.rectangle.set(creature.x, creature.y, creature.width, creature.height);
                if (creature.rectangle.overlaps(playerPlaneRectangle)) {
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                    creaturesToRemove.add(creature);
                }
            }
            creatures.removeAll(creaturesToRemove);

            // Patlama efektlerini güncelle ve çiz
            Iterator<Explosion> explosionIterator = explosions.iterator();
            while (explosionIterator.hasNext()) {
                Explosion explosion = explosionIterator.next();
                explosion.update(Gdx.graphics.getDeltaTime());
                if (explosion.isFinished()) {
                    explosionIterator.remove();
                }
            }

            // Engeller ile çarpışma kontrolü
            List<Obstacle> obstaclesToRemove = new ArrayList<>(); // Değişiklik burada
            for (Obstacle obstacle : obstacles) {
                obstacle.x -= obstacle.speed * Gdx.graphics.getDeltaTime();
                if (obstacle.x < -obstacle.width) {
                    obstaclesToRemove.add(obstacle);
                }
                obstacle.rectangle.set(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
                if (obstacle.rectangle.overlaps(playerPlaneRectangle)) {
                    health--;
                    if (health <= 0) {
                        gameOver();
                    }
                }
            }
            obstacles.removeAll(obstaclesToRemove); // Değişiklik burada

            // Mermileri hareket ettir ve çarpışmaları kontrolü
            List<Bullet> bulletsToRemove = new ArrayList<>(); // Değişiklik burada
            for (Bullet bullet : bullets) {
                bullet.x += bullet.speed * Gdx.graphics.getDeltaTime();
                if (bullet.x > Gdx.graphics.getWidth()) {
                    bulletsToRemove.add(bullet);
                }
                bullet.rectangle.set(bullet.x, bullet.y, bullet.width, bullet.height);

                // Düşman uçakları ile çarpışma kontrolü
                flyingEnemyIterator = flyingEnemies.iterator();
                List<FlyingEnemy> flyingEnemiesToRemove2 = new ArrayList<>();
                while (flyingEnemyIterator.hasNext()) {
                    FlyingEnemy enemy = flyingEnemyIterator.next();
                    if (bullet.rectangle.overlaps(enemy.rectangle)) {
                        bulletsToRemove.add(bullet);
                        enemy.health -= bullet.damage;
                        if (enemy.health <= 0) {
                            flyingEnemiesToRemove2.add(enemy);
                            explosions.add(new Explosion(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, false)); // Büyük patlama
                        } else {
                            explosions.add(new Explosion(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, true)); // Küçük patlama
                        }
                        break;
                    }
                }
                flyingEnemies.removeAll(flyingEnemiesToRemove2);

                // Zemin düşmanları ile çarpışma kontrolü
                groundEnemyIterator = groundEnemies.iterator();
                List<GroundEnemy> groundEnemiesToRemove2 = new ArrayList<>();
                while (groundEnemyIterator.hasNext()) {
                    GroundEnemy enemy = groundEnemyIterator.next();
                    if (bullet.rectangle.overlaps(enemy.rectangle)) {
                        bulletsToRemove.add(bullet);
                        enemy.health -= bullet.damage;
                        if (enemy.health <= 0) {
                            groundEnemiesToRemove2.add(enemy);
                        } else {
                        }
                        break;
                    }
                }
                groundEnemies.removeAll(groundEnemiesToRemove2);

                // Yaratıklar ile çarpışma kontrolü
                creatureIterator = creatures.iterator();
                List<Creature> creaturesToRemove2 = new ArrayList<>();
                while (creatureIterator.hasNext()) {
                    Creature creature = creatureIterator.next();
                    if (bullet.rectangle.overlaps(creature.rectangle)) {
                        bulletsToRemove.add(bullet);
                        creature.health -= bullet.damage;
                        if (creature.health <= 0) {
                            creaturesToRemove2.add(creature);
                            explosions.add(new Explosion(creature.x + creature.width, creature.y + creature.height, false)); // Büyük patlama
                        } else {
                            explosions.add(new Explosion(creature.x + creature.width / 2, creature.y + creature.height / 2, true)); // Küçük patlama
                        }
                        break;
                    }
                }
                creatures.removeAll(creaturesToRemove2);
            }
            bullets.removeAll(bulletsToRemove);

            // Yeniden doldurma süresini güncelle
            if (reloadTime > 0) {
                reloadTime -= Gdx.graphics.getDeltaTime();
            }
        }



        //ÇİZİM KISMI
        batch.begin();
        //arkaplan çizimi
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //zemin çizimi
        batch.draw(groundTexture, 0, 0, Gdx.graphics.getWidth(), groundHeight);

        //uçak çizimi
        batch.draw(currentPlaneTexture, planeX, planeY, planeWidth, planeHeight);


        //Ateş etme düğmesi çizimi
        if (isButtonPressed || dugmeGeciciOlarakBasili) { // Her iki bayrağı da kontrol et
            batch.draw(shootButtonPressedTexture, buttonRectangle.x, buttonRectangle.y, buttonRectangle.width, buttonRectangle.height);
        } else {
            batch.draw(shootButtonTexture, buttonRectangle.x, buttonRectangle.y, buttonRectangle.width, buttonRectangle.height);
        }

        //Mermi sayısı revolver çizimi
        batch.draw(ammoTextures[ammo], 250, Gdx.graphics.getHeight() - 165, 168, 168);

        // Oyuncu uçağı çarpışma dikdörtgenini güncelle
        playerPlaneRectangle.set(planeX, planeY, planeWidth, planeHeight);


        //Düşman uçaklarının çizimi
        for (FlyingEnemy enemy : flyingEnemies) {
            batch.draw(enemyPlaneTexture, enemy.x, enemy.y, enemy.width, enemy.height);
        }

        //Zemin düşmanının çizimi
        for (GroundEnemy enemy : groundEnemies) {
            batch.draw(enemyPlaneTexture, enemy.x, groundHeight, enemy.width, enemy.height);
        }

        //yaratıkların çizimi
        for (Creature creature : creatures) {
            batch.draw(creatureTexture, creature.x, creature.y, creature.width, creature.height);
        }

        //engellerin çizimi
        for (Obstacle obstacle : obstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        //HEALTHBAR ÇİZİMİ
        batch.draw(healthTextures[health], 10, Gdx.graphics.getHeight() - 50, 200, 50);

        //MERMİLERİ ÇİZ
        for (Bullet bullet : bullets) {
            batch.draw(bullet.texture, bullet.x, bullet.y, bullet.width, bullet.height);
        }
        // Patlama efektlerini çiz
        for (Explosion explosion : explosions) {
            explosion.draw(batch);
        }


        //OYUN BİTTİYSE TRY AGAİN! YAZISINI ÇİZ
        if (isGameOver) {
            font.draw(batch, "TRY AGAİN!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2);
        }

        font.draw(batch, "Level:" + level, 20, Gdx.graphics.getHeight() - 90);

        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (buttonRectangle.contains(screenX, Gdx.graphics.getHeight() - screenY)) {
            isButtonPressed = true;
            // Gecikme eklemek için Timer kullanın
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    shootBullet();}
            }, SHOOT_DELAY*1.55f); // SHOOT_DELAY saniye sonra ateş et

        }
        touchStartX = screenX;
        touchStartY = screenY;
        return true;
    }



    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!isGameOver) {
            float deltaX = screenX - touchStartX;
            float deltaY = touchStartY - screenY; // Y eksenini ters çevir

            planeX += deltaX * 0.5f; // X eksenindeki hareketi ayarla (0.5f hız faktörü)
            planeY += deltaY * 0.5f; // Y eksenindeki hareketi ayarla (0.5f hız faktörü)

            // Uçağın ekran sınırları içinde kalmasını sağla
            if (planeX < 0) planeX = 0;
            if (planeX > Gdx.graphics.getWidth() - planeWidth) planeX = Gdx.graphics.getWidth() - planeWidth;
            if (planeY < 0) planeY = 0;
            if (planeY > Gdx.graphics.getHeight() - planeHeight) planeY = Gdx.graphics.getHeight() - planeHeight;

            touchStartX = screenX; // Dokunmanın başladığı noktayı güncelle
            touchStartY = screenY;
        }
        return true;
    }

    // Kullanılmayan metodları boş olarak override edin
    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyUp(int keycode) { return false; }
    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isButtonPressed) {
            isButtonPressed = false;
            return true;
        }
        if (isGameOver) {
            resetGame();
        }
        return true;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override
    public boolean scrolled(float amountX, float amountY) { return false; }

    private void updatePlaneTexture() {
        if (currentLevel >= 1 && currentLevel <= 5) {
            currentPlaneTexture = planeTextures[currentLevel - 1];
            planeWidth = currentPlaneTexture.getWidth() / 3;
            planeHeight = currentPlaneTexture.getHeight() / 3;
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
        float obstacleWidth = obstacleTexture.getWidth();
        float obstacleHeight = obstacleTexture.getHeight();

        Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleSpeed, obstacleWidth, obstacleHeight);
        obstacles.add(obstacle);
    }

    private void shootBullet() {
        if (!isGameOver) {
            if (ammo > 0) {
                ammo--;

                Texture bulletTexture = bulletTextures[Math.min(level - 1, bulletTextures.length - 1)];
                float bulletWidth= bulletTexture.getWidth() / 5;
                float bulletHeight = bulletTexture.getHeight() / 5;
                float bulletSpeed = 500;

                int bulletDamage = (level == 1) ? 1 : 2;
                Bullet bullet = new Bullet(planeX + planeWidth, planeY + planeHeight / 2 - bulletHeight / 2, bulletSpeed, bulletTexture, bulletWidth, bulletHeight, bulletDamage);
                bullets.add(bullet);

                // Bayrağı true olarak ayarla
                dugmeGeciciOlarakBasili= true;

                // Gecikme ile butonu eski haline getir
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        dugmeGeciciOlarakBasili = false; // Gecikmeden sonra bayrağı sıfırla
                    }
                }, 0.1f); // 0.1 saniye gecikme

                if (ammo == 0) {
                    reloadTime = 3f + random.nextFloat() * 2f;
                }
            } else if (reloadTime <= 0) {
                ammo = MAX_AMMO;
                reloadTime = 3f + random.nextFloat() * 2f;
            }
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
        for (Texture bulletTexture : bulletTextures) { // bulletTextures'ı dispose et
            bulletTexture.dispose();
        }
        for (Texture ammoTexture : ammoTextures) { // ammoTextures'ı dispose et
            ammoTexture.dispose();
        }
        for (Explosion explosion : explosions) {
            for (Texture frame : explosion.frames) {
                frame.dispose();
            }
        }
        font.dispose();
    }
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

}
