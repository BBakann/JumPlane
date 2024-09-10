package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Coin {
    public float x, y, width, height;
    public boolean collected;
    public Rectangle rectangle;
    private Animation<TextureRegion> animation;
    private float animationTime;
    private Sound coinSound;
    private float speed;

    public Coin(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 200;
        this.height = 200;
        this.collected = false;
        this.rectangle = new Rectangle(x, y, width, height);
        speed=200;


        TextureRegion[] frames = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            Texture texture = new Texture("coin1_" + (i + 1) + ".png");
            frames[i] = new TextureRegion(texture);
        }


        animation = new Animation<>(0.1f, frames);
        animationTime = 0;

        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
    }

    public void update(float deltaTime) {
        x -= speed * deltaTime;
        rectangle.x = x;
    }

    public void draw(SpriteBatch batch) {
        if (!collected) {
            animationTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = animation.getKeyFrame(animationTime, true);
            batch.draw(currentFrame, x, y, width, height);
        }
    }

    public void playSound() {
        coinSound.play();
    }

    public void dispose() {
        coinSound.dispose();
    }
}