package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Creature3 extends Creature {
    private Texture[] animationTextures = new Texture[43];
    private static final float WIDTH = new Texture("creature3_1.png").getWidth()*1.1f;
    private static final float HEIGHT = new Texture("creature3_1.png").getHeight()*1.1f;

    public Creature3(float x, float y, float speed) {
        super(x, y, speed*3.2f, WIDTH, HEIGHT);
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        for (int i =0; i < 43; i++) {
            animationTextures[i] = new Texture("creature3_" + (i + 1) + ".png");
        }
    }

    private void loadAnimation() {
        TextureRegion[] frames = new TextureRegion[43];
        for (int i = 0; i < 43; i++) {
            frames[i] = new TextureRegion(animationTextures[i]);
        }
        animation = new Animation<>(0.1f, frames);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Texture texture : animationTextures) {
            texture.dispose();
        }
    }
}