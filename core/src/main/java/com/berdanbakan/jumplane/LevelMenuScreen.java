package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelMenuScreen implements Screen {
    private final JumPlane game;
    private Stage stage;
    private Texture levelbackgroundTexture;
    private SpriteBatch batch;
    public int unlockedLevel=1;
    private Sound clickSound;

    private Texture exitButtonTexture;
    private ImageButton exitButton;

    private Texture backButtonTexture;
    private ImageButton  backButton;

    private Preferences prefs;

    private Texture[] levelOpenedButtonTextures = new Texture[6];
    private Texture[] levelClosedButtonTextures = new Texture[6];


    public LevelMenuScreen(JumPlane game){
        this.game=game;
        stage=new Stage(new ScreenViewport());
        levelbackgroundTexture=new Texture("levelmenubackground.png");
        batch=new SpriteBatch();

        for (int i = 1; i <= 6; i++) {
            levelOpenedButtonTextures[i-1] = new Texture("level" + i + "openedbutton.png");
            if (i > 1) {
                levelClosedButtonTextures[i-1] = new Texture("level" + i + "button.png");}
        }

        createLevelButtons();
        createExitButton();
        createBackButton();

        prefs = Gdx.app.getPreferences("My Preferences");
        unlockedLevel = prefs.getInteger("unlockedLevel", 1);

        clickSound=Gdx.audio.newSound(Gdx.files.internal("clicksound.mp3"));
    }


    public int getUnlockedLevel() {
        return unlockedLevel;
    }

    public void setUnlockedLevel(int unlockedLevel) {
        this.unlockedLevel = unlockedLevel;
        stage.clear();
        createLevelButtons();
    }


    private void createLevelButtons(){
        float buttonWidth = 450;
        float buttonHeight = 300;

        ImageButton[] levelButtons = new ImageButton[6]; // 6 level için

        for (int i = 1; i <= 6; i++){ // 6 level için döngü
            Texture levelbuttonTexture;

            if (i <= unlockedLevel) {
                levelbuttonTexture = levelOpenedButtonTextures[i-1];
            } else {
                levelbuttonTexture = levelClosedButtonTextures[i-1];
            }

            ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
            buttonStyle.imageUp = new Image(levelbuttonTexture).getDrawable();

            ImageButton levelButton = new ImageButton(buttonStyle);
            levelButtons[i-1] = levelButton;
            levelButton.setSize(buttonWidth, buttonHeight);

            if (i > unlockedLevel) {
                levelButton.setDisabled(true);
            }

            // Butonları konumlandır
            float offsetX = 270f; // Kaydırma miktarı
            float offsetY = 200f;

            if (i == 1) {
                levelButton.setPosition(offsetX, offsetY);
            } else if (i == 2) {
                levelButton.setPosition(Gdx.graphics.getWidth() / 2 - buttonWidth / 2, offsetY);
            } else if (i == 3) {
                levelButton.setPosition(Gdx.graphics.getWidth() - buttonWidth - offsetX, offsetY);
            } else if (i == 4) {
                levelButton.setPosition(offsetX, offsetY - buttonHeight + 50); // 4.level
            } else if (i == 5) {
                levelButton.setPosition(Gdx.graphics.getWidth() / 2 - buttonWidth / 2, offsetY-buttonHeight+50); // 5. level
            } else if (i == 6) {
                levelButton.setPosition(Gdx.graphics.getWidth() - buttonWidth - offsetX, offsetY-buttonHeight+50); // Free Level (6. level)
            }

            final int level = i;
            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    clickSound.play();
                    if (level <= unlockedLevel) {
                        if (level == 6) { // Free Level ise
                            showNameInputDialog();
                        } else {
                            game.setScreen(new GameScreen(game, level, LevelMenuScreen.this));
                        }
                    }
                }
            });

            stage.addActor(levelButton);
        }
    }
    private void showNameInputDialog() {
        // TODO: İsim girme ekranını gösteren kodları buraya ekleyeceğiz.
        // Şimdilik, Free Level'a geçiş yapacak basit bir kod ekleyelim.
        game.setScreen(new GameScreen(game, 6, LevelMenuScreen.this)); // 6. level Free Level
    }



    private void createExitButton(){
        exitButtonTexture=new Texture("exitbutton.png");
        ImageButton.ImageButtonStyle buttonStyle= new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp=new Image(exitButtonTexture).getDrawable();

        exitButton= new ImageButton(buttonStyle);
        exitButton.setSize(200,200);

        exitButton.setPosition(Gdx.graphics.getWidth() - exitButton.getWidth() - 20, 20);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.app.exit(); // Oyundan çık
            }
        });
        stage.addActor(exitButton);
    }

    private  void createBackButton(){
        backButtonTexture=new Texture("backbutton.png");
        ImageButton.ImageButtonStyle buttonStyle=new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp=new Image(backButtonTexture).getDrawable();

        backButton=new ImageButton(buttonStyle);
        backButton.setSize(200,200);

        backButton.setPosition(Gdx.graphics.getWidth() - exitButton.getWidth() - 20, Gdx.graphics.getHeight() - exitButton.getHeight() - 20);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                clickSound.play();
                game.setScreen(new MainMenuScreen(game,batch));
            }
        });
        stage.addActor(backButton);
    }


    @Override
    public void show(){
        if (stage == null) {
            stage = new Stage(new ScreenViewport());
        } else {
            stage.clear();
        }

        createLevelButtons();
        createExitButton();
        createBackButton();

        Gdx.input.setInputProcessor(stage);
        game.stopMusic();
    }


    @Override
    public void render(float delta){

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelbackgroundTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        batch.end();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width,int height){

    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void hide(){
        stage.clear();
    }

    @Override
    public void dispose(){
        batch.dispose();
        levelbackgroundTexture.dispose();
        stage.dispose();

        exitButtonTexture.dispose();
        backButtonTexture.dispose();

        clickSound.dispose();

        for (int i = 0; i < 5; i++) {
            levelOpenedButtonTextures[i].dispose();
            levelClosedButtonTextures[i].dispose();
        }
    }
}