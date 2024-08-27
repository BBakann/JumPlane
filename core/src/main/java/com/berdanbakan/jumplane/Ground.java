package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ground {
    private static final float GROUND_SCALE = 0.4f; // Zemin ölçeği

    private static Texture groundTexture; // Statik olarak tanımlandı
    public final float groundHeight; // Zemin yüksekliği (final olarak tanımlandı)

    public Ground() {
        if (groundTexture == null) {
            groundTexture = new Texture("ground.png");
        }

        groundHeight = groundTexture.getHeight() * GROUND_SCALE;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(groundTexture, 0, 0, Gdx.graphics.getWidth(), groundHeight);
    }

    public void dispose() {
        if (groundTexture != null) {groundTexture.dispose();
            groundTexture = null;
        }
    }
}