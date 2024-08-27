package com.berdanbakan.jumplane;

import com.badlogic.gdx.math.Rectangle;

public class GroundEnemy {
    public float x;
    private final float speed;
    public final float width;
    public final float height;
    public final int health;
    public Rectangle rectangle;

    private static final int INITIAL_HEALTH = 2; // Başlangıç canı
    private static final float GROUND_LEVEL = 0f; // Zemin seviyesi

    public GroundEnemy(float x, float speed, float width, float height) {
        this.x = x;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.health = INITIAL_HEALTH;
        this.rectangle = new Rectangle(x, GROUND_LEVEL, width, height);
    }

    public void update(float deltaTime){
        x += speed * deltaTime;
        rectangle.set(x, GROUND_LEVEL, width, height);
    }
}
