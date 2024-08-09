package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    private Music music;


    public LevelMenuScreen(JumPlane game){
        this.game=game;
        stage=new Stage(new ScreenViewport());
        levelbackgroundTexture=new Texture("levelmenubackground.png");
        batch=new SpriteBatch();

        createLevelButtons();


        music=Gdx.audio.newMusic(Gdx.files.internal("backgroundmusic.mp3"));
        music.setLooping(true);
        music.play();
        music.setVolume(0.5f);
    }



    public int getUnlockedLevel() {
        return unlockedLevel;
    }

    public void setUnlockedLevel(int unlockedLevel) { // Setter metodu ekleyin
        this.unlockedLevel = unlockedLevel;
    }




    private void createLevelButtons(){
        float buttonWidth = 0; // Buton genişliğini hesaplamak için değişken

        ImageButton[] levelButtons = new ImageButton[5]; // Butonları saklamak için dizi

        for (int i=1;i<=5;i++){
            Texture levelbuttonTexture=new Texture("level"+i+"button.png");
            ImageButton.ImageButtonStyle buttonStyle=new ImageButton.ImageButtonStyle();buttonStyle.imageUp=new com.badlogic.gdx.scenes.scene2d.ui.Image(levelbuttonTexture).getDrawable();

            ImageButton levelButton = new ImageButton(buttonStyle);
            levelButtons[i-1] = levelButton; // Butonu diziye ekle

            if (buttonWidth == 0) {
                buttonWidth = levelButton.getWidth(); // İlk butonun genişliğini al
            }


        }

        // Butonları konumlandır
        float offset = 50f; // Kaydırma miktarı

        levelButtons[0].setPosition(Gdx.graphics.getWidth() / 4 - buttonWidth / 2 - offset, Gdx.graphics.getHeight() / 5); // level1button sola kaydır
        levelButtons[1].setPosition(Gdx.graphics.getWidth() / 2 - buttonWidth / 2, Gdx.graphics.getHeight() / 5); // level2button (değişiklik yok)
        levelButtons[2].setPosition(Gdx.graphics.getWidth() * 3 / 4 - buttonWidth / 2 + offset, Gdx.graphics.getHeight() / 5); // level3button sağa kaydır
        levelButtons[3].setPosition(Gdx.graphics.getWidth() / 2- buttonWidth / 2, Gdx.graphics.getHeight() / 5 - 160); // level4button (değişiklik yok)
        levelButtons[4].setPosition(Gdx.graphics.getWidth() / 2 - buttonWidth / 2, Gdx.graphics.getHeight() / 5 - 320); // level5button (değişiklik yok)

        for (int i=1;i<=5;i++){
            final int level = i;
            levelButtons[i-1].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x,float y){
                    if (level<=unlockedLevel){
                        game.setScreen(new GameScreen(game, level,LevelMenuScreen.this));
                        dispose();
                    }
                }
            });
            stage.addActor(levelButtons[i-1]);
        }
    }





    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
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


   }
}
