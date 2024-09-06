package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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


    public LevelMenuScreen(JumPlane game){
        this.game=game;
        stage=new Stage(new ScreenViewport());
        levelbackgroundTexture=new Texture("levelmenubackground.png");
        batch=new SpriteBatch();

        createLevelButtons();
        createExitButton();
        createBackButton();

        clickSound=Gdx.audio.newSound(Gdx.files.internal("clicksound.mp3"));
    }


    public int getUnlockedLevel() {
        return unlockedLevel;
    }

    public void setUnlockedLevel(int unlockedLevel) {
        this.unlockedLevel = unlockedLevel;
    }




    private void createLevelButtons(){
        float buttonWidth = 450;
        float buttonHeight = 300;

        ImageButton[] levelButtons = new ImageButton[5];

        for (int i=1;i<=5;i++){
            Texture levelbuttonTexture=new Texture("level"+i+"button.png");
            ImageButton.ImageButtonStyle buttonStyle=new ImageButton.ImageButtonStyle();buttonStyle.imageUp=new com.badlogic.gdx.scenes.scene2d.ui.Image(levelbuttonTexture).getDrawable();

            ImageButton levelButton = new ImageButton(buttonStyle);
            levelButtons[i-1] = levelButton;
            levelButton.setSize(buttonWidth,buttonHeight);
        }

        // Butonları konumlandır
        float offsetX = 250f; // Kaydırma miktarı
        float offsetY = 150f;


        levelButtons[0].setPosition(offsetX, offsetY);
        levelButtons[1].setPosition(Gdx.graphics.getWidth() / 2 - buttonWidth / 2, offsetY);
        levelButtons[2].setPosition(Gdx.graphics.getWidth() - buttonWidth - offsetX, offsetY);


        levelButtons[3].setPosition(Gdx.graphics.getWidth() / 4 - buttonWidth / 2 + 180,  offsetY - buttonHeight + 100);
        levelButtons[4].setPosition(Gdx.graphics.getWidth() * 3 /4 - buttonWidth / 2 - 180, offsetY - buttonHeight + 100);

        for (int i=1;i<=5;i++){
            final int level = i;
            levelButtons[i-1].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x,float y){
                    clickSound.play();
                    if (level<=unlockedLevel){
                        game.setScreen(new GameScreen(game, level,LevelMenuScreen.this));
                        dispose();
                    }
                }
            });
            stage.addActor(levelButtons[i-1]);
        }
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
                MainMenuScreen mainMenuScreen=new MainMenuScreen(game);
                game.setScreen(mainMenuScreen);
            }
        });
        stage.addActor(backButton);
    }


    @Override
    public void show(){
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

    }
   @Override
    public void dispose(){
       stage.dispose();
       batch.dispose();
       levelbackgroundTexture.dispose();
       exitButtonTexture.dispose();
       clickSound.dispose();
       exitButton.clearListeners();
       backButtonTexture.dispose();
       backButton.clearListeners();

   }
}
