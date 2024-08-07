package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {
    private Texture[] healthTextures;
    private Texture[] ammoTextures;

    public HUD() {
        healthTextures = new Texture[7]; // 0-6 can için
        for (int i = 0; i < 7; i++) {
            healthTextures[i] = new Texture("health" + i + ".png");
        }

        ammoTextures = new Texture[7]; // 0-6 mermi için
        for (int i = 0; i < 7; i++) {
            ammoTextures[i] = new Texture("ammo" + i + ".png");
        }
    }

    public void draw(SpriteBatch batch, int health, int ammo) {
        if (health > 0) {
            batch.draw(healthTextures[health], -50, Gdx.graphics.getHeight() - 145, 432, 136);
        }

        batch.draw(ammoTextures[ammo], 400, Gdx.graphics.getHeight() - 185, 168, 168);
    }
    public void dispose() {
        for (Texture texture : healthTextures) {
            texture.dispose();
        }
        for (Texture texture : ammoTextures) {
            texture.dispose();
        }
    }
}
