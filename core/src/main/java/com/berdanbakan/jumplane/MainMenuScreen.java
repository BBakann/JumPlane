package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private final JumPlane game;
    private Stage stage;
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private ImageButton startButton;
    private ImageButton settingsButton; // Ayarlar düğmesi
    private Music music;




    public MainMenuScreen(JumPlane game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        backgroundTexture = new Texture("menubackground.png");
        batch = new SpriteBatch();
        createStartButton();
        createSettingsButton(); // Ayarlar düğmesini oluştur

        music=Gdx.audio.newMusic(Gdx.files.internal("backgroundmusic.mp3"));
        music.setLooping(true);
        music.play();

    }

    private void createStartButton() {
        Texture buttonTexture = new Texture("openbutton.png");
        ImageButtonStyle buttonStyle = new ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(buttonTexture).getDrawable();

        startButton = new ImageButton(buttonStyle);
        startButton.setPosition(Gdx.graphics.getWidth() / 2.19f - startButton.getWidth() / 2.8f, Gdx.graphics.getHeight() / 8f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LevelMenuScreen levelMenuScreen=new LevelMenuScreen(game);
                game.setScreen(levelMenuScreen); // Oyun ekranına geçiş
                dispose();
            }
        });
        stage.addActor(startButton);
    }

    private void createSettingsButton() {
        Texture buttonTexture = new Texture("settingsbutton.png");
        ImageButtonStyle buttonStyle = new ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(buttonTexture).getDrawable();

        settingsButton = new ImageButton(buttonStyle);
        settingsButton.setPosition(10, Gdx.graphics.getHeight() - settingsButton.getHeight() - 10); // Sol üst köşe
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Ayarlar düğmesine tıklandığında yapılacak işlemler buraya eklenecek
            }
        });
        stage.addActor(settingsButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        startButton.clearListeners(); // ClickListener'ı temizle
        settingsButton.clearListeners(); // ClickListener'ı temizle
        music.dispose();
    }}
