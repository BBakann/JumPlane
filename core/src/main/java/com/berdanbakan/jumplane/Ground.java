package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ground {
    private Texture groundTexture;

    public float groundHeight; // Zemin yüksekliği

    public Ground() {
        groundTexture = new Texture("ground.png");

        groundHeight = groundTexture.getHeight()*0.2f;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(groundTexture, 0, 0, Gdx.graphics.getWidth(), groundHeight);


    }

    public void dispose() {
        groundTexture.dispose();
    }
}
