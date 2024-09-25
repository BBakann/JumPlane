package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlyingEnemy2 extends FlyingEnemy {
    public float width = 958 / 3f;
    public float height = 586 / 3f;

    private Animation<TextureRegion> animation;
    private float animationTime;
    private Texture[] animationTextures = new Texture[6];

    public FlyingEnemy2(float x, float y, float speed, float width, float height,Player player) {
        super(x, y, speed*1.15f, width, height,player);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i = 0; i < 6; i++) {
            animationTextures[i] = new Texture("enemyplane2_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
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