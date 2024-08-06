package com.berdanbakan.jumplane;

import com.badlogic.gdx.math.Rectangle;

public class GroundEnemy {
    public float x, speed, width, height;
    public int health; // Add health property
    public Rectangle rectangle;

    public GroundEnemy(float x, float speed, float width, float height) {
        this.x = x;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, 0, width, height);
        this.health = 2;
    }
}
