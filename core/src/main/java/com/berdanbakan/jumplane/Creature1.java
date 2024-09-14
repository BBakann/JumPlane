package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Creature1 extends Creature {
    private Texture[] animationTextures = new Texture[14];
    private static final float WIDTH = new Texture("creature1_1.png").getWidth() / 2;
    private static final float HEIGHT = new Texture("creature1_1.png").getHeight() / 2;

    public Creature1(float x, float y, float speed) {
        super(x, y, speed*2f, WIDTH, HEIGHT);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i =0; i < 14; i++) {
            animationTextures[i] = new Texture("creature1_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[14];
        for (int i = 0; i < 14; i++) {
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