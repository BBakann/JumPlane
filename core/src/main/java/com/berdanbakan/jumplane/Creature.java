package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Creature {
    public float x, y, speed, width, height;
    public int health;
    public Rectangle rectangle;//Çarpışma Alanı

    private float jumpTimer = 0; // Zıplama zamanlayıcısı
    private boolean isJumping = false; // Zıplıyor mu?
    private final float jumpHeight = 70; // Zıplama yüksekliği
    private final float initialY; // Başlangıç Y pozisyonu

    private static final float JUMP_INTERVAL = 3f; // Zıplama aralığı
    private static final float COLLISION_OFFSET = 0.40f; // Çarpışma alanı ofseti
    private static final float COLLISION_SCALE = 0.15f; // Çarpışma alanı ölçeği

    public Creature(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height= height;
        this.rectangle = new Rectangle(x, y, width, height);
        this.health = 2;
        this.initialY = y; // Başlangıç Y pozisyonunu kaydet
    }

    public void update() {
        x -= speed * Gdx.graphics.getDeltaTime();//Hareket sol yönde -=

        // Zıplama zamanlayıcısını güncelle
        jumpTimer += Gdx.graphics.getDeltaTime();

        // JUMP_INTERVAL saniyede bir zıpla
        if (jumpTimer >= JUMP_INTERVAL) {
            isJumping = true;
            jumpTimer = 0;
        }

        // Zıplıyorsa yukarı hareket ettir
        if (isJumping) {
            y += jumpHeight * Gdx.graphics.getDeltaTime();
            if (y >= initialY +jumpHeight) { // Zıplama yüksekliğine ulaşıldığında
                isJumping = false;
            }
        } else if (y > initialY) { // Yere düşme
            y -= jumpHeight * Gdx.graphics.getDeltaTime();
            if (y <= initialY) {
                y = initialY;
            }
        }
        // Y değerini kontrol et
        y = Math.max(y, initialY);


        // Rectangle'ı güncelle
        rectangle.set(x + width * COLLISION_OFFSET, y + height * COLLISION_OFFSET, width * COLLISION_SCALE, height * COLLISION_SCALE);
    }
}