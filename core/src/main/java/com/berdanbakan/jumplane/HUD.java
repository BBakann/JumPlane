package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {
    private static final int MAX_HEALTH = 6;// Maksimum can değeri
    private static final int MAX_AMMO = 6; // Maksimum mermi değeri

    private static final float HEALTH_X = -50f; // Can göstergesinin x koordinatı
    private static final float HEALTH_Y = Gdx.graphics.getHeight() - 145f; // Can göstergesinin y koordinatı
    private static final float HEALTH_WIDTH = 432f; // Can göstergesinin genişliği
    private static final float HEALTH_HEIGHT = 136f; // Can göstergesinin yüksekliği
    private static final float AMMO_X = 20f; // Mermi göstergesinin x koordinatı
    private static final float AMMO_Y = Gdx.graphics.getHeight() - 305f; // Mermi göstergesinin y koordinatı
    private static final float AMMO_SIZE = 168f; // Mermi göstergesinin boyutu

    private static Texture[] healthTextures;
    private static Texture[] ammoTextures;

    public HUD() {
        if (healthTextures == null || ammoTextures == null) {
            healthTextures = new Texture[MAX_HEALTH + 1];
            for (int i = 0; i <= MAX_HEALTH; i++) {
                healthTextures[i] = new Texture("health" + i + ".png");
            }

            ammoTextures = new Texture[MAX_AMMO + 1];
            for (int i = 0; i <= MAX_AMMO; i++) {
                ammoTextures[i] = new Texture("ammo" + i + ".png");
            }
        }
    }

    public void draw(SpriteBatch batch, int health, int ammo) {
        if (health > 0 && health <= MAX_HEALTH) {
            batch.draw(healthTextures[health], HEALTH_X, HEALTH_Y, HEALTH_WIDTH, HEALTH_HEIGHT);
        }

        if (ammo >= 0 && ammo <= MAX_AMMO) {
            batch.draw(ammoTextures[ammo], AMMO_X, AMMO_Y, AMMO_SIZE, AMMO_SIZE);
        }
    }

    public void dispose() {
        if (healthTextures != null) {
            for (Texture texture : healthTextures) {
                texture.dispose();
            }
            healthTextures = null;
        }

        if (ammoTextures != null) {
            for (Texture texture : ammoTextures) {
                texture.dispose();
            }
            ammoTextures = null;
        }
    }
}
