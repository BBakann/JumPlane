package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlyingEnemy1 extends FlyingEnemy {
    public float width = 273f/1.1f;
    public float height = 282f/1.1f;

    private Animation<TextureRegion> animation;
    private float animationTime;
    private Texture[] animationTextures = new Texture[13];

    public FlyingEnemy1(float x, float y, float speed, float width, float height) {
        super(x, y, speed*1.1f, width, height);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i = 0; i < 13; i++) {
            animationTextures[i] = new Texture("enemyplane1_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames= new TextureRegion[13];
        for (int i = 0; i < 13; i++) {
            frames[i] = new TextureRegion(animationTextures[i]);
        }
        animation = new Animation<>(0.1f, frames);
        animationTime = 0;
    }

    @Override
    public void draw(SpriteBatch batch) {
        animationTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true);
        batch.draw(currentFrame, x, y, width, height);


        for (Bullet bullet : getBullets()) {
            batch.draw(enemyBulletTexture, bullet.x, bullet.y, bullet.width, bullet.height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Texture texture : animationTextures) {
            texture.dispose();
        }
    }
}