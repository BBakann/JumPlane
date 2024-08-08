package com.berdanbakan.jumplane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
    public Animation<TextureRegion> animation;
    private float animationTime;
    private float x, y;private boolean isSmall;

    public Explosion(float x, float y, boolean isSmall) {
        this.x = x;
        this.y = y;
        this.isSmall = isSmall;
        animationTime = 0;

        // Patlama animasyonu için dokuları yükle
        Texture[] frames = new Texture[10];
        for (int i = 0; i < 10; i++) {
            frames[i] = new Texture("patlama" + (i + 1) + ".png");
        }

        //Animasyon oluştur
        TextureRegion[] regions = new TextureRegion[frames.length];
        for (int i = 0; i < frames.length; i++) {
            regions[i] = new TextureRegion(frames[i]);
        }
        animation = new Animation<>(0.05f, regions); // Animasyon hızını ayarlayın (0.05 saniye)
    }

    public void update(float deltaTime) {
        animationTime += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(animationTime);
        float explosionSize = isSmall ? 100: 200; // EXPLOSİON BOYUTU AYARI **
        batch.draw(currentFrame, x - explosionSize / 2, y - explosionSize / 2, explosionSize, explosionSize);
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(animationTime);
    }

    public void dispose() {
        for (TextureRegion frame : animation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
    }
}
