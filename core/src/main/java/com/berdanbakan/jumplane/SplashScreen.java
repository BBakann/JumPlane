package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class SplashScreen implements Screen {

    private final JumPlane game;
    private SpriteBatch batch;
    private Texture splashBackgroundTexture;

    public SplashScreen(JumPlane game) {
        this.game = game;
        batch = new SpriteBatch();
        splashBackgroundTexture = new Texture("splash_background.png"); // Arka plan resmini yükleyin
    }

    @Override
    public void show() {
        // Zamanlayıcı başlat
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new MainMenuScreen(game)); // Ana menüye geç
            }
        }, 2); // 2 saniye sonra ana menüye geç
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Arka plan rengini(siyah) ayarlayın
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(splashBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Arka planı çiz
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (splashBackgroundTexture != null) {
            splashBackgroundTexture.dispose();
        }
    }
}
