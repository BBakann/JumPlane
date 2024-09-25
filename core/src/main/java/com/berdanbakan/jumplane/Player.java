package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private static final int MAX_AMMO = 6;
    private static Texture[] planeTextures;
    private static Texture[] bulletTextures;

    public Texture currentPlaneTexture;
    public float planeWidth;
    public float planeHeight;
    public float planeX;
    public float planeY;
    private float planeSpeed = 600f;
    public int health;
    public int ammo;private float reloadTime;
    public Rectangle playerPlaneRectangle;
    private List<Bullet> bullets;
    private boolean dugmeGeciciOlarakBasili;
    private Ground ground;
    private LevelManager levelManager;

    private JumPlane game;


    static { // Statik blok içinde
        planeTextures = new Texture[5];
        for (int i = 0; i < 5; i++) {
            planeTextures[i] = new Texture("plane" + (i + 1) + ".png");
        }

        bulletTextures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            bulletTextures[i] = new Texture("bullet" + (i + 1) + ".png");
        }
    }

    public Player(Ground ground, LevelManager levelManager,JumPlane game) {
        this.ground = ground;
        this.levelManager = levelManager;
        this.game = game;
        playerPlaneRectangle = new Rectangle();
        bullets = new ArrayList<>();
        reset();
    }

    public void update(float deltaTime, InputHandler inputHandler) {
        if (inputHandler.isUpRightButtonPressed()) {
            planeX += planeSpeed * deltaTime * 0.701f; // Sağ yukarı çapraz
            planeY += planeSpeed * deltaTime * 0.701f;
        } else if (inputHandler.isDownRightButtonPressed()) {
            planeX += planeSpeed * deltaTime * 0.701f; // Sağ aşağı çapraz
            planeY -= planeSpeed * deltaTime * 0.701f;
        } else if (inputHandler.isUpLeftButtonPressed()) {
            planeX -= planeSpeed * deltaTime * 0.701f; // Sol yukarı çapraz
            planeY += planeSpeed * deltaTime * 0.701f;
        } else if (inputHandler.isDownLeftButtonPressed()) {
            planeX -= planeSpeed * deltaTime * 0.701f; // Sol aşağı çapraz
            planeY -= planeSpeed * deltaTime * 0.701f;

        } else if (inputHandler.isUpButtonPressed()) {
            planeY += planeSpeed * deltaTime;
        } else if (inputHandler.isDownButtonPressed()) {
            planeY -= planeSpeed * deltaTime;
        } else if (inputHandler.isLeftButtonPressed()) {
            planeX -= planeSpeed * deltaTime;
        } else if (inputHandler.isRightButtonPressed()) {
            planeX += planeSpeed * deltaTime;
        }

        // Ekran sınırlarını kontrol et
        planeX = Math.max(0, Math.min(planeX, Gdx.graphics.getWidth() - planeWidth));
        planeY = Math.max(ground.groundHeight, Math.min(planeY, Gdx.graphics.getHeight() - planeHeight));

        // Mermileri güncelle
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.x += bullet.speedX * deltaTime;
            if (bullet.x > Gdx.graphics.getWidth()) {
                bulletsToRemove.add(bullet);
            }
            bullet.rectangle.set(bullet.x, bullet.y, bullet.width, bullet.height);
        }
        bullets.removeAll(bulletsToRemove);

        // Yeniden doldurma süresini güncelle
        if (reloadTime > 0) {
            reloadTime -= deltaTime;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(currentPlaneTexture, planeX, planeY, planeWidth, planeHeight);

        // Mermileri çiz
        for (Bullet bullet : bullets) {
            batch.draw(bullet.texture, bullet.x, bullet.y, bullet.width, bullet.height);
        }
    }

    public void updatePlaneTexture(int currentLevel) {
        if (currentLevel >= 1 && currentLevel <= 5) {
            currentPlaneTexture= planeTextures[currentLevel - 1];
            planeWidth = currentPlaneTexture.getWidth() / 2.6f;
            planeHeight = currentPlaneTexture.getHeight() / 2.6f;
            planeSpeed = 600 + (currentLevel - 1) * 50; // her levelde 50 speed artışı
        } else if (currentLevel == 6) { // Free Level
            currentPlaneTexture = planeTextures[4]; // Level 5 uçağını kullan
            planeWidth = currentPlaneTexture.getWidth() / 2.6f;
            planeHeight = currentPlaneTexture.getHeight() / 2.6f;
            planeSpeed = 850; // Level 5 ile aynı hızı kullan
        }
    }

    public void checkPotionCollision(List<Potion> potions) {
        Iterator<Potion> iter = potions.iterator();
        while (iter.hasNext()) {
            Potion potion= iter.next();
            if (playerPlaneRectangle.overlaps(potion.rectangle)) {
                if (potion.type == Potion.PotionType.HEALTH) {
                    health = Math.min(health + 1, 6); // Canı en fazla 6 yap
                } else {
                    health--;
                }
                potion.sound.play();
                iter.remove(); // İksiri listeden kaldır
            }
        }
    }

    public void reset() {
        planeX = 50;
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2;
        health = 6;
        ammo = MAX_AMMO;
        reloadTime = 0;
        bullets.clear();
        updatePlaneTexture(levelManager.currentLevel);
    }

    public void shootBullet(int level, Sound ammoSound, Sound switchAmmoSound) {
        if (ammo > 0 && !dugmeGeciciOlarakBasili) {
            ammo--;

            Texture bulletTexture = bulletTextures[Math.min(level - 1, bulletTextures.length - 1)];
            float bulletWidth = bulletTexture.getWidth() / 5;
            float bulletHeight = bulletTexture.getHeight() / 5;
            float bulletSpeed = 650;

            int bulletDamage = (level == 1) ? 1 : 2;
            Bullet bullet = new Bullet(planeX + planeWidth, planeY + planeHeight / 2 - bulletHeight / 2, bulletSpeed, 0, bulletTexture, bulletWidth, bulletHeight, bulletDamage);
            bullets.add(bullet);

            dugmeGeciciOlarakBasili = true;
            ammoSound.play();

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    dugmeGeciciOlarakBasili = false;
                }
            }, 0.1f);

            if (ammo == 0) {
                reloadTime = 1.25f;
            }
        } else if (reloadTime <= 0) {
            ammo = MAX_AMMO;
            reloadTime = 1.25f;

            switchAmmoSound.play();
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public static void dispose() {
        for (Texture planeTexture : planeTextures) {
            planeTexture.dispose();
        }
        for (Texture bulletTexture : bulletTextures) {
            bulletTexture.dispose();
        }
    }

    public void resetPosition() {
        planeX = 50; // veya istediğiniz başlangıç x değeri
        planeY = Gdx.graphics.getHeight() / 2 - planeHeight / 2; // veya istediğiniz başlangıç y değeri
    }
}