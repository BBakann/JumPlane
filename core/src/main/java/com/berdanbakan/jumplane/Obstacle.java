package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    public float x;
    public final float y;
    private final float speed;
    public final float width;
    public final float height;
    public Rectangle rectangle;
    private Texture[] textures;
    private int currentLevel;

    private static final float RECT_OFFSET_X_FACTOR = 0.3f;
    private static final float RECT_OFFSET_Y_FACTOR = 0.2f;
    private static final float RECT_WIDTH_FACTOR = 0.4f;
    private static final float RECT_HEIGHT_FACTOR = 0.6f;

    public Obstacle(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);

        textures=new Texture[5];
        for (int i=0;i<5;i++){
            textures[i]=new Texture("obstacle"+(i+1)+".png");
        }
        currentLevel=1;
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        x -= speed * deltaTime;
        rectangle.set(x + width * RECT_OFFSET_X_FACTOR, y + height * RECT_OFFSET_Y_FACTOR, width * RECT_WIDTH_FACTOR, height * RECT_HEIGHT_FACTOR);
    }

    public void setLevel(int level){
        currentLevel=level;
    }

    public void draw(SpriteBatch batch){
        int index=(currentLevel-1)%5;
        batch.draw(textures[index],x,y,width,height);
    }

    public void dispose(){
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}