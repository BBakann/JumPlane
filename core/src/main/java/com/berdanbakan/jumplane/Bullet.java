package com.berdanbakan.jumplane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    public float x, y, speed, width, height;
    public Texture texture;
    public Rectangle rectangle;//Çarpışma Alanı
    int damage;

    public Bullet(float x, float y, float speed, Texture texture, float width, float height,int damage) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.damage=damage;
        this.rectangle = new Rectangle(x, y, width, height);
    }
}
