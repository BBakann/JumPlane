package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;

public class Potion {
    public enum PotionType{
        HEALTH,POISON
    }

    public Texture texture;
    public PotionType type;
    public float x,y,width,height;
    public Rectangle rectangle;
    public Sound sound;

    public Potion(float x,float y,PotionType type){
        this.x=x;
        this.y=y;
        this.type=type;

        if (type==PotionType.HEALTH){
        texture=new Texture("health_potion.png");
        sound= Gdx.audio.newSound(Gdx.files.internal("heal.mp3"));
    }
        else {
        texture=new Texture("poison_potion.png");
        sound=Gdx.audio.newSound(Gdx.files.internal("poison.mp3"));
        }
        width=texture.getWidth()/4f;
        height=texture.getHeight()/4f;
        rectangle=new Rectangle(x,y,width,height);

    }

    public void dispose() {
        texture.dispose();
        sound.dispose();
    }

}
