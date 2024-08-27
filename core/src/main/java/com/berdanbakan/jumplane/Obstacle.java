package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    public float x;
    public final float y;
    private final float speed;
    public final float width;
    public final float height;
    public Rectangle rectangle;

    private static final float RECT_OFFSET_X_FACTOR = 0.3f;
    private static final float RECT_OFFSET_Y_FACTOR = 0.2f;
    private static final float RECT_WIDTH_FACTOR = 0.4f;
    private static final float RECT_HEIGHT_FACTOR = 0.6f;

    public Obstacle(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        x -= speed * deltaTime;
        rectangle.set(x + width * RECT_OFFSET_X_FACTOR, y + height * RECT_OFFSET_Y_FACTOR, width * RECT_WIDTH_FACTOR, height * RECT_HEIGHT_FACTOR);
    }
}
