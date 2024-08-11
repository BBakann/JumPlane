package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FlyingEnemy {
    float x, y, speed, width, height;
    int health;
    Rectangle rectangle;

    private List<Bullet> bullets;
    private float shootTimer;
    private float shootDelay;
    public Texture enemyBulletTexture; // Public yapıldı
    private Random random;

    public FlyingEnemy(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
        this.health = 2;

        bullets=new ArrayList<>();
        shootTimer=0;
        shootDelay=1f;
        enemyBulletTexture=new Texture("bullet1.png");
        random=new Random();



    }
    public void update() {
        x-= speed * Gdx.graphics.getDeltaTime();
        rectangle.set(x + width * 0.50f, y + height * 0.50f, width * 0.1f, height * 0.1f);

        shootTimer += Gdx.graphics.getDeltaTime();
        if (shootTimer >= shootDelay) {
            shootTimer = 0;
            if (random.nextFloat() < 0.40f) { // %40 ihtimalle ateş et
                shoot();
            }
        }

        // Mermileri güncelle ve ekran dışına çıkanları kaldır
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.x -= bullet.speedX * Gdx.graphics.getDeltaTime(); // Mermiyi sola doğru hareket ettir
            bullet.rectangle.set(bullet.x, bullet.y, bullet.width, bullet.height);
            if (bullet.x < -bullet.width) {
                iter.remove();
            }
        }
    }
    private void shoot() {
        float bulletX = x;
        float bulletY = y + height / 2;
        float bulletSpeed = 400; // Mermi hızı
        float angle=random.nextFloat()*360;
        float bulletSpeedX = bulletSpeed * (float) Math.cos(Math.toRadians(angle));
        float bulletSpeedY = bulletSpeed * (float) Math.sin(Math.toRadians(angle));
        float bulletWidth = enemyBulletTexture.getWidth();
        float bulletHeight = enemyBulletTexture.getHeight();
        int damage = 1; // Merminin hasarı

        Bullet bullet = new Bullet(bulletX, bulletY, bulletSpeedX, bulletSpeedY, enemyBulletTexture, bulletWidth/6, bulletHeight/6, damage);
        bullets.add(bullet);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

}
