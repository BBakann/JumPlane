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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private Texture closeButtonTexture;

    private ShapeRenderer shapeRenderer;

    private Texture languageButtonTexture_en;
    private Texture languageButtonTexture_tr;

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
        params.color = Color.WHITE;
        params.size = 40;
        font = fontGen.generateFont(params);

        game.playMusic("backgroundmusic.mp3");
        shapeRenderer=new ShapeRenderer();

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
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(settingsButtonTexture).getDrawable();settingsButton = new ImageButton(buttonStyle);
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

    private void createLanguageButton() {
        languageButtonTexture_en = new Texture("language_button.png");
        languageButtonTexture_tr = new Texture("language_button1.png");
        ImageButton.ImageButtonStyle languageButtonStyle = new ImageButton.ImageButtonStyle();

        // Başlangıç durumunu ayarla
        if (GameScreen.currentLanguage.equals("tr")) {
            languageButtonStyle.imageUp = new Image(languageButtonTexture_tr).getDrawable();
        } else {
            languageButtonStyle.imageUp = new Image(languageButtonTexture_en).getDrawable();
        }

        languageButton = new ImageButton(languageButtonStyle);
        languageButton.setSize(150, 150);
        languageButton.setPosition(40, 80);

        languageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameScreen.currentLanguage.equals("tr")) {
                    GameScreen.currentLanguage = "en";
                    languageButton.getStyle().imageUp = new Image(languageButtonTexture_en).getDrawable();
                } else {
                    GameScreen.currentLanguage = "tr";
                    languageButton.getStyle().imageUp = new Image(languageButtonTexture_tr).getDrawable();
                }

            }
        });

        settingsScreen.addActor(languageButton);
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
                infoButtonClicked = !infoButtonClicked;
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
        settingsBackground.setSize(400, 300);
        settingsScreen.addActor(settingsBackground);

        createLanguageButton(); // Dil düğmesini oluştur

        createVoiceButton();
        voiceButton.setPosition(210, 80);
        settingsScreen.addActor(voiceButton);

        closeButtonTexture = new Texture("exitbutton.png");
        ImageButton.ImageButtonStyle closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.imageUp = new Image(closeButtonTexture).getDrawable();
        ImageButton closeButton = new ImageButton(closeButtonStyle);closeButton.setSize(50, 50);
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
    public void render(float delta) {Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end(); // Batch'i burada kapatın

        if (infoButtonClicked) {
            // Ekranı karart
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.5f); // Siyah, %50 saydam
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();
            font.draw(batch, "JumpLane\n\n" +
                    "Get ready for an adrenaline-fueled adventure as youpilot your plane through treacherous skies!\n" +
                    "Tap the screen to make your plane soar, dodge obstacles, and collect coins to unlock awesome new planes and challenging levels.\n\n" +
                    "Features:\n" +
                    "* Simple and addictive one-touch gameplay\n" +"* Stunning graphics and immersive sound effects\n" +
                    "* A variety of unique planes to collect\n" +
                    "* Endless levels with increasing difficulty\n" +
                    "* Compete with friends and players worldwide on the leaderboards\n\n" +
                    "Developed by Berdan Bakan\n\n" +
                    "Design inspirations taken from bevoullin.com\n\n" +
                    "All rights reserved @ 2024.", 20, 900);
            batch.end();


            startButton.setVisible(false);
            settingsButton.setVisible(false);
            resultButton.setVisible(false);
            exitButton.setVisible(false);
        } else {

            startButton.setVisible(true);
            settingsButton.setVisible(true);resultButton.setVisible(true);
            exitButton.setVisible(true);
        }

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
        settingsButtonTexture.dispose();
        if (music != null) {
            music.stop();
            music.dispose();
        }
        clickSound.dispose();
        voiceButtonTexture.dispose();
        infoButtonTexture.dispose();
        font.dispose();
        fontGen.dispose();
        exitButtonTexture.dispose();
        resultButtonTexture.dispose();
        shapeRenderer.dispose();


        settingsBackgroundTexture.dispose();

        languageButtonTexture_en.dispose();
        languageButtonTexture_tr.dispose();

        settingsScreen.clear();


        startButton.clearListeners();
        settingsButton.clearListeners();
        voiceButton.clearListeners();
        infoButton.clearListeners();
        exitButton.clearListeners();
        resultButton.clearListeners();
    }
}