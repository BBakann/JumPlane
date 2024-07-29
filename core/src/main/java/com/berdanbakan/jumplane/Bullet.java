package com.berdanbakan.jumplane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    public float x, y, speed, width, height;
    public Texture texture;
    public Rectangle rectangle;

    public Bullet(float x, float y, float speed, Texture texture, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
    }
}
