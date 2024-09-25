package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlyingEnemy3 extends FlyingEnemy {
    public float width = 473 / 2f;
    public float height = 468 / 2f;

    private Animation<TextureRegion> animation;
    private float animationTime;
    private Texture[] animationTextures = new Texture[11];

    public FlyingEnemy3(float x, float y, float speed, float width, float height,Player player) {
        super(x, y, speed*1.25f, width, height,player);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i = 0; i < 11; i++) {
            animationTextures[i] = new Texture("enemyplane3_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[11];
        for (int i = 0; i < 11; i++) {
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

        for (Bullet bullet : getBullets()) {batch.draw(enemyBulletTexture, bullet.x, bullet.y, bullet.width, bullet.height);
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