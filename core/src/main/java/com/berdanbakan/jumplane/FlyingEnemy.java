package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FlyingEnemy {
    float x, y, speed, width, height;
    int health;
    Rectangle rectangle;

    private Player player; // Player nesnesini tutmak için
    private float targetX; // Oyuncunun x koordinatı
    private float targetY; // Oyuncunun y koordinatı

    private final List<Bullet> bullets;
    private float shootTimer;
    private static final int INITIAL_HEALTH= 2; // Başlangıç canı
    private static final float SHOOT_DELAY = 2f; // Ateş gecikmesi
    private static final float SHOOT_PROBABILITY = 0.60f; //Ateş etme olasılığı
    private static final float COLLISION_SCALE =0.90f; // Çarpışma alanı ölçeği
    private static final float BULLET_SPEED = 750f; // Mermi hızı
    private static final int BULLET_DAMAGE = 1; // Mermi hasarı
    private static final float BULLET_SCALE = 1/6f; // Mermi ölçeği

    public static Texture enemyBulletTexture;
    private static Random random;

    private float movementTimer = 0; // Hareket zamanlayıcısı

    public FlyingEnemy(float x, float y, float speed, float width, float height, Player player) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
        this.health = INITIAL_HEALTH;
        bullets = new ArrayList<>();
        shootTimer = 0;

        this.player = player;
        this.targetX = player.planeX; // Oyuncunun x koordinatını alma
        this.targetY = player.planeY; // Oyuncunun y koordinatını alma

        if (enemyBulletTexture == null) {
            enemyBulletTexture = new Texture("bullet1.png");
        }
        if(random == null) {
            random = new Random();
        }
    }

    public void draw(SpriteBatch batch) {

    }

    public void update(float deltaTime) {

        x -= speed * deltaTime;

        // Çarpışma alanını görselin ortasına ve boyutuna göre ayarla
        float collisionOffsetX = width * (1 - COLLISION_SCALE) / 2;
        float collisionOffsetY = height * (1 - COLLISION_SCALE) / 2;
        float collisionWidth = width * COLLISION_SCALE;
        float collisionHeight = height * COLLISION_SCALE;

        rectangle.set(x + collisionOffsetX, y + collisionOffsetY, collisionWidth, collisionHeight);

        shootTimer += Gdx.graphics.getDeltaTime();
        if (shootTimer >= SHOOT_DELAY) {
            shootTimer = 0;
            if (random.nextFloat() < SHOOT_PROBABILITY) {
                shoot();}
        }

        // Mermileri güncelle ve ekran dışına çıkanları kaldır
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update();
            if (bullet.x < 0) {
                iter.remove();
            }
        }
    }

    private void shoot() {
        float bulletX = x + width / 3;
        float bulletY = y + height / 3;

        Bullet bullet = new Bullet(bulletX, bulletY, -BULLET_SPEED, 0, enemyBulletTexture, enemyBulletTexture.getWidth() * BULLET_SCALE, enemyBulletTexture.getHeight() * BULLET_SCALE, BULLET_DAMAGE);
        bullets.add(bullet);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void dispose(){

    }
}