package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;

public class TimerBoost {
    public Texture texture;
    public float x, y, width, height;
    public Rectangle rectangle;
    public Sound sound;
    public boolean collected;

    public TimerBoost(float x, float y) {
        this.x = x;
        this.y = y;
        texture = new Texture("timer_boost.png"); // Görseli yükle
        width = texture.getWidth() /1.508f ;
        height = texture.getHeight()/1.508f;
        rectangle = new Rectangle(x, y, width, height);
        //sound = Gdx.audio.newSound(Gdx.files.internal("winsound.mp3")); // Ses dosyasını yükle
        collected = false;
    }

    public void update(float deltaTime) {
        if (!collected) {
            x -= 200 * deltaTime; // Sola doğru hareket
            rectangle.set(x, y, width, height);

            // Ekran sınırını kontrol et
            if (x < -width) {
                collected = true; // Ekran dışına çıkarsa toplanmış olarak işaretle
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (!collected) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void dispose() {
        texture.dispose();
        //sound.dispose();
    }
}