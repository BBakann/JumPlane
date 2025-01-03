package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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


    private boolean isMusicPlaying = true;

    private Texture infoButtonTexture;
    private ImageButton infoButton;
    private boolean infoButtonClicked = false;

    private Texture gameInfoButtonTexture;
    private ImageButton gameInfoButton;
    private boolean gameInfoButtonClicked = false;

    private BitmapFont font;

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

    private Texture voiceUpTexture;
    private Texture voiceDownTexture;

    private Texture labelTexture;


    public MainMenuScreen(JumPlane game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;


            stage = new Stage(new ScreenViewport());

            labelTexture = new Texture("label1.png");

            backgroundTexture = new Texture("menubackground.png");

            startButtonTexture = new Texture("openbutton.png");

            settingsButtonTexture = new Texture("settingsbutton.png");

            infoButtonTexture = new Texture("infobutton.png");

            exitButtonTexture = new Texture("exitbutton.png");

            resultButtonTexture = new Texture("resultbutton.png");

            settingsBackgroundTexture = new Texture("settings_background.png");

            closeButtonTexture = new Texture("exitbutton.png");

            languageButtonTexture_en = new Texture("language_button.png");

            languageButtonTexture_tr = new Texture("language_button1.png");

            voiceUpTexture = new Texture("voiceup.png");

            voiceDownTexture = new Texture("voice_down.png");


            createStartButton();
            createSettingsButton();
            createResultButton();
            createInfoButton();
            createExitButton();
            createSettingsScreen();
            createGameInfoButton();

            font = new BitmapFont(Gdx.files.internal("negrita.fnt"), Gdx.files.internal("negrita.png"), false);
            font.getData().setScale(0.620f);

            game.playMusic("backgroundmusic.mp3");
            shapeRenderer = new ShapeRenderer();

            clickSound = Gdx.audio.newSound(Gdx.files.internal("clicksound.mp3"));

    }

    private void createStartButton() {
        startButtonTexture= new Texture("openbutton.png");
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(startButtonTexture).getDrawable();

        startButton = new ImageButton(buttonStyle);

        startButton.setSize(200,200);

        startButton.setPosition(Gdx.graphics.getWidth()/3+230,Gdx.graphics.getHeight()/7);

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
        settingsButton.setPosition(Gdx.graphics.getWidth() / 3 + 480, Gdx.graphics.getHeight() / 7);

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
    private void createGameInfoButton() {
        gameInfoButtonTexture = new Texture("gameinfobutton.png");
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.ui.Image(gameInfoButtonTexture).getDrawable();

        gameInfoButton = new ImageButton(buttonStyle);
        gameInfoButton.setSize(200,200);


        gameInfoButton.setPosition(20, Gdx.graphics.getHeight() - gameInfoButton.getHeight() - 20);

        gameInfoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                gameInfoButtonClicked =! gameInfoButtonClicked;
            }
        });
        stage.addActor(gameInfoButton);
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

        resultButton.setPosition(Gdx.graphics.getWidth()/3-20,Gdx.graphics.getHeight()/7);

        resultButton.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event,float x,float y){
                clickSound.play();

            }

        });
        stage.addActor(resultButton);
    }

    private void createVoiceButton() {
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new Image(voiceUpTexture).getDrawable();

        voiceButton = new ImageButton(buttonStyle);
        voiceButton.setSize(150, 150);

        voiceButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                isMusicPlaying = !isMusicPlaying;

                if (isMusicPlaying) {
                    game.playMusic("backgroundmusic.mp3");
                    voiceButton.getStyle().imageUp = new Image(voiceUpTexture).getDrawable();
                } else {
                    game.stopMusic();
                    voiceButton.getStyle().imageUp = new Image(voiceDownTexture).getDrawable();
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

        createLanguageButton();

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
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(labelTexture, Gdx.graphics.getWidth() / 2f - 400f, Gdx.graphics.getHeight() / 7f - 200f, 800, 600);
        batch.end();

        boolean isAnyButtonClicked = infoButtonClicked || gameInfoButtonClicked;

        if (isAnyButtonClicked) {
            // Ekranı karart
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.5f); // Siyah, %50 saydam
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Düğmeleri gizle
            startButton.setVisible(false);
            settingsButton.setVisible(false);
            resultButton.setVisible(false);
            exitButton.setVisible(false);
        } else {
            // Düğmeleri göster
            startButton.setVisible(true);
            settingsButton.setVisible(true);
            resultButton.setVisible(true);
            exitButton.setVisible(true);
        }

        stage.act(delta);
        stage.draw(); // Düğmeleri çiz

            if (infoButtonClicked) {
                batch.begin();
                if (GameScreen.currentLanguage.equals("tr")){
                    font.draw(batch, "JumpLane\n\n" +
                            "Tehlikeli gokyuzunde ucaginizi yonlendirirken adrenalin dolu bir maceraya hazir olun!\n" +
                            "Ucaginizin yukselmesi icin ekrana dokunun, engellerden kacinin.\n" +
                            "Harika yeni ucaklarin ve zorlu seviyelerin kilidini acmak icin altinlari toplayin.\n\n" +
                            "Ozellikler:\n" +
                            "*Basit ve bagimlilik yapan tek dokunusla oynanis\n" +
                            "* Carpici grafiklerve surukleyici ses efektleri\n" +
                            "* Toplanacak cesitli benzersiz ucaklar\n" +
                            "* Artan zorluk seviyeleriyle sonsuz seviyeler\n" +
                            "* Liderlik tablolarinda arkadaslarinizla ve dunya capindaki oyuncularla yarisin\n\n" +
                            "Gelistirici: Berdan Bakan\n\n" +
                            "Tasarim ilhamlari bevoullin.com'dan alinmistir\n\n" +
                            "Tum haklari saklidir @ 2024.", 20, 900);
                }else {
                    font.draw(batch, "JumpLane\n\n" +
                            "Get ready for an adrenaline-fueled adventure as youpilot your plane through treacherous skies!\n" +
                            "Tap the screen to make your plane soar, dodge obstacles.\n" +
                            "Collect coins to unlock awesome new planes and challenging levels.\n\n" +
                            "Features:\n" +
                            "* Simple andaddictive one-touch gameplay\n" +"* Stunning graphics and immersive sound effects\n" +
                            "* A variety of unique planes to collect\n" +
                            "* Endless levels with increasing difficulty\n" +
                            "* Compete with friends and players worldwide on the leaderboards\n\n" +
                            "Developed by Berdan Bakan\n\n" +
                            "Design inspirations taken from bevoullin.com\n\n" +
                            "All rights reserved @ 2024.", 20, 900);
                }
                batch.end();
            }
            else if (gameInfoButtonClicked) {
                batch.begin();

                font.getData().setScale(0.50f); // Yazı boyutunu ayarla
                float textX = 50; // Yazının sol kenar boşluğu
                float imageSize = 200; // Görsel boyutufloat imageSpacing = 20; // Görseller arası boşluk

                String titleText;
                String gameInfoText;

                if (GameScreen.currentLanguage.equals("tr")) {
                    titleText = "NASIL OYNANIR?";
                    gameInfoText = "JumpLane, bir ucagi kontrol ettiginiz 5 seviyeli bir oyundur.\n" +
                            "Onceki seviyeleri tamamlayarak seviyelerin kilidini acin.\n" +
                            "Kazanmak icin dusmanlari oldurun, altin toplayinve sure sinirinda hayatta kalin.\n" +
                            "Zorluk ve sure her seviyede artar.\n" +
                            "Son 'Serbest Seviye'de, 3 dakika icinde en cok altini toplayarak ilk 10'a girmeyi hedefleyin.\n\n" +
                            "**Seviyeler:** Oyunda zorluk derecesi artan 5 seviye vardir.\n" +
                            "**Dusmanlar:** Seviyelerde ilerlemek icin dusmanlari oldurmeniz gerekir.\n" +
                            "**Altin:** Yeni ucaklarin ve seviyelerin kilidini acmak icin altin toplayin.\n" +
                            "**Sure Siniri:** Her seviyeyi tamamlamak icin sinirli bir sureniz vardir.\n" +
                            "**Engeller:** Hayatta kalmak icin engellerden kacinin.\n" +
                            "**Saglik:** Ucaginizin sagligi vardir ve engellere veya dusmanlara carptiginizda azalir.\n" +
                            "**Iksirler:** Sagliginizi geri kazanmak iciniksirleri toplayin.\n" +
                            "**Ucaginiz:** Ekrana dokunarak ucaginizi kontrol edin.\n" +
                            "**Serbest Seviye:** Son seviyede, 3 dakika icinde en cok altini toplamaya calisin.";
                } else {
                    titleText = "HOW TO PLAY?";
                    gameInfoText = "JumpLane is a 5-level game where you control a plane.\n" +
                            "Unlock levels by completing previous ones.\n" +
                            "To win, kill enemies, collect gold, and survive within the time limit.\n" +
                            "Difficulty and time increase with each level.\n" +
                            "In the final 'Free Level', aim for the top 10 by collecting the most gold in 3 minutes.\n\n" +
                            "**Levels:** There are 5 levels in the game, each with increasing difficulty.\n" +
                            "**Enemies:** You need to kill enemies to progress through the levels.\n" +
                            "**Gold:** Collect gold to unlock new planes and levels.\n" +
                            "**Time Limit:** You have a limited time to complete each level.\n" +
                            "**Obstacles:** Avoid obstacles to stay alive.\n" +
                            "**Health:** Your plane has health, which decreases when you hit obstacles or enemies.\n" +
                            "**Potions:** Collect potions to restore your health.\n" +
                            "**Your Plane:** Control your plane by tapping the screen.\n" +
                            "**Free Level:** In the final level, try to collect the most gold in 3 minutes.";
                }

                GlyphLayout titleLayout = new GlyphLayout(font, titleText);

                float titleX = (Gdx.graphics.getWidth() - titleLayout.width) / 2.2f; // Başlığın x koordinatı
                float titleY = Gdx.graphics.getHeight() - 50; // Başlığın y koordinatı

                font.getData().setScale(1.5f); // Başlık yazı boyutu
                font.draw(batch, titleText, titleX, titleY); // Başlık
                font.getData().setScale(0.70f); // Yazı boyutu

                GlyphLayout layout = new GlyphLayout(font, gameInfoText);

                float width = layout.width; // Metnin genişliği
                float height = layout.height; // Metnin yüksekliği

                float x = textX; // Yazının x koordinatı
                float y = titleY - titleLayout.height - 90; // Yazının y koordinatı

                font.draw(batch, gameInfoText, x, y, width, Align.left, true); // Metin

                batch.end();

            }

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
        infoButtonTexture.dispose();
        font.dispose();
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

        voiceUpTexture.dispose();
        voiceDownTexture.dispose();

        labelTexture.dispose();
    }
}