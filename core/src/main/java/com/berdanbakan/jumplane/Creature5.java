package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Creature5 extends Creature {
    private Texture[] animationTextures = new Texture[4];
    private static final float WIDTH = new Texture("creature5_1.png").getWidth() / 2f;
    private static final float HEIGHT = new Texture("creature5_1.png").getHeight() / 2f;

    public Creature5(float x, float y, float speed) {
        super(x, y, speed*5f, WIDTH, HEIGHT);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i = 0; i <4; i++) {
            animationTextures[i] = new Texture("creature5_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
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