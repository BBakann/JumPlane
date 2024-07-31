package com.berdanbakan.jumplane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Explosion {
    public Texture[] frames;
    private float x, y;
    private int currentFrame;
    private float animationTime;
    private float frameDuration;
    private boolean isFinished;
    private float scale =1f; // Ölçeklendirme faktörü

    public Explosion(float x, float y, boolean isSmall) {
        this.x = x;
        this.y = y;
        frames = new Texture[10];
        for (int i = 1; i <= 10; i++) {
            frames[i - 1] = new Texture("patlama" + i + ".png");
        }
        currentFrame = 0;
        animationTime = 0;
        frameDuration = 0.05f;
        isFinished = false;

        if (isSmall) {
            scale = 0.5f; // Küçük patlama için ölçeklendirme
        }
    }

    public void update(float deltaTime) {
        if (!isFinished) {
            animationTime += deltaTime;
            if (animationTime >= frameDuration) {
                animationTime -= frameDuration;
                currentFrame++;
                if (currentFrame >= frames.length) {
                    isFinished = true;
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (!isFinished) {
            float width = frames[currentFrame].getWidth() * scale*5f;
            float height = frames[currentFrame].getHeight() * scale*5;
            batch.draw(frames[currentFrame], x - width / 2, y - height / 2, width, height);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
