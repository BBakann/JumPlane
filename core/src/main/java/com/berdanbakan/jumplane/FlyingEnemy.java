package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class FlyingEnemy {
    float x, y, speed, width, height;
    int health;
    Rectangle rectangle;

    public FlyingEnemy(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
        this.health = 2;
    }
    public void update() {
        x-= speed * Gdx.graphics.getDeltaTime();
        rectangle.set(x + width * 0.50f, y + height * 0.50f, width * 0.1f, height * 0.1f);
    }
}
