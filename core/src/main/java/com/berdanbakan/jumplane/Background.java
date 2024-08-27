package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private Texture[] backgrounds;
    private int currentLevel;


    public Background() {
        backgrounds =new Texture[5];
        for (int i=0;i<5;i++){
            backgrounds[i]=new Texture("background"+(i+1)+".png");
        }
        currentLevel=1;

    }
    public void setLevel(int level) {
        if (level >= 1 && level <= 5) { // Geçerli aralığı kontrol et
            currentLevel = level;
        }
    }




    public void draw(SpriteBatch batch) {
        batch.draw(backgrounds[currentLevel-1], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    public void dispose() {
        for (Texture background: backgrounds){
            background.dispose();
        }
    }
}
