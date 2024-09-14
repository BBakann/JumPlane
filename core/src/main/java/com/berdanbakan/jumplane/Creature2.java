package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Creature2 extends Creature {
    private Texture[] animationTextures = new Texture[13];
    private static final float WIDTH = new Texture("creature2_1.png").getWidth() / 2;
    private static final float HEIGHT = new Texture("creature2_1.png").getHeight() / 2;

    public Creature2(float x, float y, float speed) {
        super(x, y, speed*2.5f, WIDTH, HEIGHT);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i =0; i < 13; i++) {
            animationTextures[i] = new Texture("creature2_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[13];
        for (int i = 0; i < 13; i++) {
            frames[i] = new TextureRegion(animationTextures[i]);
        }
        animation = new Animation<>(0.1f, frames);
    }

    @Override
    public void update() {
        x -= speed * Gdx.graphics.getDeltaTime();
        updateRectangle();
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Texture texture : animationTextures) {
            texture.dispose();
        }
    }
}