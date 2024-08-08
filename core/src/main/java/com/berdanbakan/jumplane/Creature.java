package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Creature {
    public float x, y, speed, width, height;
    public int health;
    public Rectangle rectangle;//Çarpışma Alanı

    private float jumpTimer = 0; // Zıplama zamanlayıcısı
    private boolean isJumping = false; // Zıplıyor mu?
    private float jumpHeight = 70; // Zıplama yüksekliği
    private float initialY; // Başlangıç Y pozisyonu

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
        x -= speed * Gdx.graphics.getDeltaTime();//Hareket

        //Çarpışma alanı boyutu
        rectangle.set(x + width * 0.45f, y + height * 0.45f, width * 0.1f, height * 0.1f);

        // Zıplama zamanlayıcısını güncelle
        jumpTimer += Gdx.graphics.getDeltaTime();

        // 3 saniyede bir zıpla
        if (jumpTimer >= 3) {
            isJumping = true;
            jumpTimer = 0;
        }

        // Zıplıyorsa yukarı hareket ettir
        if (isJumping) {
            y += jumpHeight * Gdx.graphics.getDeltaTime();
            if (y >= initialY + jumpHeight) { // Zıplama yüksekliğine ulaşıldığında
                isJumping = false;
            }
        } else if (y > initialY) { // Yere düşme
            y -= jumpHeight * Gdx.graphics.getDeltaTime();if (y <= initialY) {
                y = initialY;
            }
        }

        // Rectangle'ı güncelle
        rectangle.set(x, y, width, height);
    }
}
