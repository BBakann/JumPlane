package com.berdanbakan.jumplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;



public class JumPlane extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture planeone;
    Texture[] planeTextures;
    Texture currentPlaneTexture;

    int currentLevel;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");//arkaplanımızı ekledik.


        //Uçak görsellerini ekliyoruz.
        planeTextures=new Texture[5];
        for (int i=0;i<planeTextures.length;i++){
            planeTextures[i]=new Texture("plane"+(i+1)+".png");
        }

        currentPlaneTexture=planeTextures[0];// Plane1 ilk uçağımız olarak seçildi.
        currentLevel=1;


    }

    @Override
    public void render() {
        ScreenUtils.clear(0,0,0,1);
        updatePlaneTexture();//sonraki kısımda uçak seçimini güncelliyoruz.




        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(currentPlaneTexture, Gdx.graphics.getWidth() / 2 - currentPlaneTexture.getWidth() / 2, Gdx.graphics.getHeight() / 2 - currentPlaneTexture.getHeight() / 2); // Uçağı ekranda ortala

        batch.end();
    }
    public void updatePlaneTexture(){
        if (currentLevel>=1<&&currentLevel)
    }




    @Override
    public void dispose() {


    }
}



