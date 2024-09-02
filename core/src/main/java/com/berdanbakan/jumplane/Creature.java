package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;import com.badlogic.gdx.math.Rectangle;

public abstract class Creature {
    public float x, y, speed, width, height;
    public int health;
    public Rectangle rectangle; // Çarpışma Alanı

    protected Animation<TextureRegion> animation; // Animasyon değişkeni
    protected float animationTime; // Animasyon zamanı

    public Creature(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width= width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
        this.health = 2;
        animationTime = 0;
    }

    public abstract void update(); // Soyut update metodu

    public void draw(SpriteBatch batch) {
        animationTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true);
        batch.draw(currentFrame, x, y, width, height);
    }

    protected void updateRectangle() {
        float collisionWidth = width * 0.6f; // Çarpışma alanının genişliği
        float collisionHeight = height * 0.8f; // Çarpışma alanının yüksekliği
        float collisionXOffset = (width - collisionWidth) / 2; // X ekseni ofseti
        float collisionYOffset = (height - collisionHeight) * 0.2f; // Y ekseni ofseti

        rectangle.set(x + collisionXOffset, y + collisionYOffset, collisionWidth, collisionHeight);
    }public void dispose() {
        for (TextureRegion frame : animation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
    }
}