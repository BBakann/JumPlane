package com.berdanbakan.jumplane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
    private static final int FRAME_COUNT = 10; // Patlama animasyonu png sayısı
    private static final float FRAME_DURATION = 0.030f; // Kare süresi
    private static final float SMALL_EXPLOSION_SIZE = 100f; // Küçük patlama boyutu
    private static final float LARGE_EXPLOSION_SIZE = 200f; // Büyük patlama boyutu

    private static Texture[] explosionFrames; // Patlama animasyonu kareleri
    private static Animation<TextureRegion> smallExplosionAnimation; // Küçük patlama animasyonu
    private static Animation<TextureRegion> largeExplosionAnimation; // Büyük patlama animasyonu

    public final Animation<TextureRegion> animation; // Kullanılacak animasyon
    private float animationTime;
    private final float x, y;

    public Explosion(float x, float y, boolean isSmall) {
        this.x = x;
        this.y = y;

        if (explosionFrames == null) {
            // Patlama animasyonu karelerini yükle
            explosionFrames = new Texture[FRAME_COUNT];
            for (int i = 0; i < FRAME_COUNT; i++) {
                explosionFrames[i] = new Texture("patlama" + (i + 1) + ".png");
            }
        }

        if (smallExplosionAnimation == null || largeExplosionAnimation == null) {
            // Animasyonları oluştur
            TextureRegion[] regions = new TextureRegion[FRAME_COUNT];
            for (int i = 0; i < FRAME_COUNT; i++) {
                regions[i] = new TextureRegion(explosionFrames[i]);
            }
            smallExplosionAnimation = new Animation<>(FRAME_DURATION,regions);
            largeExplosionAnimation = new Animation<>(FRAME_DURATION, regions);
        }

        animation = isSmall ? smallExplosionAnimation : largeExplosionAnimation; // Animasyonu seç
        animationTime = 0;

    }

    public void update(float deltaTime) {
        animationTime += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(animationTime);
        final float explosionSize = animation == smallExplosionAnimation ? SMALL_EXPLOSION_SIZE : LARGE_EXPLOSION_SIZE;

        batch.draw(currentFrame, x - explosionSize / 2, y - explosionSize / 2, explosionSize, explosionSize);
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(animationTime);
    }

    public void dispose() {
        if (explosionFrames != null) {
            for (Texture frame : explosionFrames) {
                frame.dispose();
            }
            explosionFrames = null;
            smallExplosionAnimation = null;
            largeExplosionAnimation= null;
        }
    }
}