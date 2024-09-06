package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private final JumPlane game;
    private Stage stage;
    private Texture backgroundTexture;
    private SpriteBatch batch;

    private ImageButton startButton;
    private Texture startButtonTexture;

    private ImageButton settingsButton;
    private Texture settingsButtonTexture;

    private Music music;
    private Sound clickSound;

    private ImageButton voiceButton;
    private Texture voiceButtonTexture;

    private boolean isMusicPlaying = true;

    private Texture infoButtonTexture;
    private ImageButton infoButton;
    private boolean infoButtonClicked = false;

    private BitmapFont font;
    private FreeTypeFontGenerator fontGen;

    private Texture exitButtonTexture;
    private ImageButton exitButton;

    private Texture resultButtonTexture;
    private ImageButton resultButton;

    private Group settingsScreen;
    private ImageButton languageButton;

    private Texture settingsBackgroundTexture;
    private Texture languageButtonTexture;
    private Texture closeButtonTexture;


    public MainMenuScreen(JumPlane game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        backgroundTexture = new Texture("menubackground.png");
        batch = new SpriteBatch();

        createStartButton();
        createSettingsButton();
        createResultButton();
        createInfoButton();
        createExitButton();
        createSettingsScreen();


        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("negrita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.color = Color.BLACK;
        params.size = 30;
        font = fontGen.generateFont(params);

        game.playMusic("backgroundmusic.mp3");

        clickSound = Gdx.audio.newSound(Gdx.files.internal("clicksound.mp3"));
    }

    private void createStartButton() {
        startButtonTexture= new Texture("openbutton.png");
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(startButtonTexture).getDrawable();

        startButton = new ImageButton(buttonStyle);

        startButton.setSize(200,200);

        startButton.setPosition(Gdx.graphics.getWidth()/3+300,Gdx.graphics.getHeight()/7);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                LevelMenuScreen levelMenuScreen=new LevelMenuScreen(game);
                game.setScreen(levelMenuScreen);

            }
        });
        stage.addActor(startButton);
    }

    private void createSettingsButton() {
        settingsButtonTexture = new Texture("settingsbutton.png");
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(settingsButtonTexture).getDrawable();

        settingsButton = new ImageButton(buttonStyle);
        settingsButton.setSize(200, 200);
        settingsButton.setPosition(Gdx.graphics.getWidth() / 3 + 550, Gdx.graphics.getHeight() / 7);

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                settingsScreen.setVisible(true); // Ayarlar ekranını görünür yap
            }
        });
        stage.addActor(settingsButton);
    }


    private void createInfoButton() {
        infoButtonTexture = new Texture("infobutton.png");
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(infoButtonTexture).getDrawable();

        infoButton = new ImageButton(buttonStyle);
        infoButton.setSize(200,200);


        infoButton.setPosition(Gdx.graphics.getWidth() - infoButton.getWidth() - 20, 20);

        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                infoButtonClicked = true;
            }
        });
        stage.addActor(infoButton);
    }

    private void createExitButton() {
        exitButtonTexture = new Texture("exitbutton.png");
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new Image(exitButtonTexture).getDrawable();

        exitButton = new ImageButton(buttonStyle);
        exitButton.setSize(200, 200);


        exitButton.setPosition(Gdx.graphics.getWidth() - exitButton.getWidth() - 20, Gdx.graphics.getHeight() - exitButton.getHeight() - 20);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);
    }

    private void createResultButton(){
        resultButtonTexture=new Texture("resultbutton.png");
        ImageButton.ImageButtonStyle buttonStyle= new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp= new Image(resultButtonTexture).getDrawable();

        resultButton=new ImageButton(buttonStyle);
        resultButton.setSize(200,200);

        resultButton.setPosition(Gdx.graphics.getWidth()/3+50,Gdx.graphics.getHeight()/7);

        resultButton.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event,float x,float y){
                clickSound.play();

            }

        });
            stage.addActor(resultButton);
        }
    private void createVoiceButton() {
        voiceButtonTexture = new Texture("voiceup.png");
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(voiceButtonTexture).getDrawable();

        voiceButton = new ImageButton(buttonStyle);
        voiceButton.setSize(150, 150);

        voiceButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                isMusicPlaying = !isMusicPlaying; // Müziğin durumunu değiştirmek için

                if (isMusicPlaying) {
                    game.playMusic("backgroundmusic.mp3"); // JumPlane üzerinden müziği başlat
                    voiceButton.getStyle().imageUp = new Image(new Texture("voiceup.png")).getDrawable();
                } else {
                    game.stopMusic(); // JumPlane üzerinden müziği durdur
                    voiceButton.getStyle().imageUp = new Image(new Texture("voice_down.png")).getDrawable();
                }
            }
        });

    }

    private void createSettingsScreen() {
        settingsScreen = new Group();
        settingsScreen.setVisible(false); // Başlangıçta gizle
        stage.addActor(settingsScreen);


        settingsBackgroundTexture = new Texture("settings_background.png");
        Image settingsBackground = new Image(settingsBackgroundTexture);
        settingsBackground.setSize(400,300);
        settingsScreen.addActor(settingsBackground);


        languageButtonTexture = new Texture("language_button.png");
        ImageButton.ImageButtonStyle languageButtonStyle = new ImageButton.ImageButtonStyle();
        languageButtonStyle.imageUp = new Image(languageButtonTexture).getDrawable();
        languageButton = new ImageButton(languageButtonStyle);
        languageButton.setSize(150,150);
        languageButton.setPosition(40, 80);
        languageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Dil ayarlarını değiştir
            }
        });
        settingsScreen.addActor(languageButton);


        createVoiceButton();
        voiceButton.setPosition(210, 80);
        settingsScreen.addActor(voiceButton);


        closeButtonTexture = new Texture("exitbutton.png");
        ImageButton.ImageButtonStyle closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.imageUp = new Image(closeButtonTexture).getDrawable();
        ImageButton closeButton = new ImageButton(closeButtonStyle);
        closeButton.setSize(50,50);
        closeButton.setPosition(settingsBackground.getWidth() - closeButton.getWidth() - 10,
                settingsBackground.getHeight() - closeButton.getHeight() - 10);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsScreen.setVisible(false); // Ayarlar ekranını gizle
            }
        });
        settingsScreen.addActor(closeButton);
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
        if (infoButtonClicked) {
            font.draw(batch, "Oyun Hakkında Bilgi...", 500, 500);
            infoButtonClicked = true;
        }
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width,int height) {
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
        startButtonTexture.dispose();
        startButton.clearListeners();
        settingsButtonTexture.dispose();
        settingsButton.clearListeners();
        music.stop();
        music.dispose();
        game.stopMusic();
        clickSound.dispose();
        voiceButtonTexture.dispose();
        voiceButton.clearListeners();
        infoButtonTexture.dispose();
        infoButton.clearListeners();
        font.dispose();
        fontGen.dispose();
        exitButtonTexture.dispose();
        exitButton.clearListeners();
        resultButtonTexture.dispose();
        resultButton.clearListeners();

        // Ayarlar ekranı için
        settingsBackgroundTexture.dispose();
        languageButtonTexture.dispose();
        closeButtonTexture.dispose();
        settingsScreen.clear();
    }
}