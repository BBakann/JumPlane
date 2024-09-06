package com.berdanbakan.jumplane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class JumPlane extends Game {

    private Music currentMusic; //Şu anda çalan müziği tutan değişken

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        getScreen().dispose();
        stopMusic(); // Uygulama kapatıldığında müziği durdur
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