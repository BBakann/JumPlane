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

    public SplashScreen(JumPlane game,SpriteBatch batch,Texture splashBackgroundTexture) {
        this.game = game;
        this.batch=batch;
        this.splashBackgroundTexture= splashBackgroundTexture;

    }

    @Override
    public void show() {
        // Zamanlayıcı başlat
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new MainMenuScreen(game, batch)); // Ana menüye geç
            }
        }, 3); // 2 saniye sonra ana menüye geç
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(splashBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
