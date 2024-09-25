package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private Texture background;
    private int currentLevel;

    public Background() {
        currentLevel = 1;
        loadBackground(currentLevel);
    }

    public void setLevel(int level) {
        if (level >= 1 && level <= 6) {
            if (level != currentLevel) {
                if (background != null) {
                    background.dispose();
                }
                currentLevel = level;
                loadBackground(currentLevel);
            }
        }
    }

    private void loadBackground(int level) {
        background = new Texture("background" + level + ".png");
    }

    public void draw(SpriteBatch batch) {
        if (background != null) {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public void dispose() {
        if (background != null) {
            background.dispose();
        }
    }
}
