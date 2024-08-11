package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    public float x, y, speedX, speedY, width, height;
    public Texture texture;
    public Rectangle rectangle;//Çarpışma Alanı
    int damage;

    public Bullet(float x, float y, float speedX,float speedY, Texture texture, float width, float height,int damage) {
        this.x = x;
        this.y = y;
        this.speedY = speedY;
        this.speedX=speedX;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.damage=damage;
        this.rectangle = new Rectangle(x, y, width, height);
    }
    public void update() {
        x += speedX * Gdx.graphics.getDeltaTime(); // speedX kullanıldı
        y += speedY * Gdx.graphics.getDeltaTime(); // speedY kullanıldı
        rectangle.set(x, y, width, height);
    }
}
