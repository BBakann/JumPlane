package com.berdanbakan.jumplane;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    public float x, y, speed, width, height;
    public Rectangle rectangle;

    public Obstacle(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
    }
}
