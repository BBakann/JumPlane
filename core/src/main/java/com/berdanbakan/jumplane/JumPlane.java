package com.berdanbakan.jumplane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JumPlane extends Game {

    private Music currentMusic;
    private SpriteBatch batch;
    private Texture splashBackgroundTexture;
    @Override
    public void create() {
        batch = new SpriteBatch();
        splashBackgroundTexture = new Texture("splash_background.png");
        setScreen(new SplashScreen(this,batch,splashBackgroundTexture));
    }

    @Override
    public void dispose() {
        getScreen().dispose();
        stopMusic();
    }

    public void playMusic(String musicFile) {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(musicFile));
        currentMusic.setLooping(true);
        currentMusic.play();
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
    }
}