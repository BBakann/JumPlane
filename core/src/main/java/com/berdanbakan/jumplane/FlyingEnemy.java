package com.berdanbakan.jumplane;

import com.badlogic.gdx.math.Rectangle;

public class FlyingEnemy {
    float x, y, speed, width, height;
    int health; // Add health property
    Rectangle rectangle;

    public FlyingEnemy(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
        this.health = 2; // Initialize health (e.g., 2)
    }
}
