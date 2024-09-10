package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Creature5 extends Creature {

    public Creature5(float x, float y, float speed, float width, float height) {
    super(x, y, speed, width/2.5f, height/2.5f);

    TextureRegion[] frames = new TextureRegion[4];
    for (int i = 0; i < 4; i++) {
        Texture texture = new Texture("creature5_" + (i + 1) + ".png");
        frames[i] = new TextureRegion(texture);
    }

    animation = new Animation<TextureRegion>(0.1f, frames);
}

    @Override
    public void update() {
        x -= speed * Gdx.graphics.getDeltaTime();
        updateRectangle();
    }
}
