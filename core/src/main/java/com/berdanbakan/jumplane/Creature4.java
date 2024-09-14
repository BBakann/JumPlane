package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Creature4 extends Creature {
    private Texture[] animationTextures = new Texture[12];
    private static final float WIDTH = new Texture("creature4_1.png").getWidth()*1.5f;
    private static final float HEIGHT = new Texture("creature4_1.png").getHeight()*1.5f;

    public Creature4(float x, float y, float speed) {
        super(x, y, speed*3.5f, WIDTH, HEIGHT);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i =0; i < 12; i++) {
            animationTextures[i] = new Texture("creature4_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[12];
        for (int i = 0; i < 12; i++) {
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