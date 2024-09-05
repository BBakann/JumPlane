package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlyingEnemy3 extends FlyingEnemy {
    public float width = 401 / 1.5f;
    public float height = 249 / 1.5f;

    private Animation<TextureRegion> animation;
    private float animationTime;

    public FlyingEnemy3(float x, float y, float speed, float width, float height) {
        super(x, y, speed, width, height);
        loadAnimation();
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            Texture texture = new Texture("enemyplane3_" + (i + 1) + ".png");
            frames[i] = new TextureRegion(texture);
        }
        animation = new Animation<>(0.5f, frames);
        animationTime = 0;
    }

    @Override
    public void draw(SpriteBatch batch) {
        animationTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true);
        batch.draw(currentFrame, x, y, width, height);

        // Düşman mermilerini çiz
        for (Bullet bullet : getBullets()) {
            batch.draw(enemyBulletTexture, bullet.x, bullet.y, bullet.width, bullet.height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (TextureRegion frame : animation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
    }
}