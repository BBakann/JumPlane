package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;



public class GameScreen implements Screen {

    private final JumPlane game;
    private SpriteBatch batch;
    private Music music;
    private Player player;
    private boolean tryAgainDrawn = false; // TRY AGAIN! yazısı çizildi mi?
    private InputHandler inputHandler;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private Background background;
    private Ground ground;
    private HUD hud;
    private BitmapFont font;
    private FreeTypeFontGenerator fontGen;


    private int level;
    private LevelMenuScreen levelMenuScreen;
    private boolean levelCompleted=false;

    public GameScreen(JumPlane game,int level,LevelMenuScreen levelMenuScreen) {
        this.game = game;
        this.level=level;
        this.levelMenuScreen=levelMenuScreen;
        batch = new SpriteBatch();


        ground = new Ground();
        player = new Player(ground);

        enemyManager = new EnemyManager(); // Önce EnemyManager'ı oluşturun
        levelManager = new LevelManager(enemyManager); // Sonra LevelManager'ı oluşturun

        inputHandler = new InputHandler(this, player, levelManager, enemyManager); // GameScreen instance'ını InputHandler'a geçiyoruz
        background = new Background();
        hud = new HUD();

        fontGen=new FreeTypeFontGenerator(Gdx.files.internal("negrita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params= new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.color=Color.BLACK;
        params.size=40;

        font=fontGen.generateFont(params);



        Gdx.input.setInputProcessor(inputHandler);

        resetGame();
    }

    private void checkLevelCompleted() {
        if (levelCompleted) {
            if (level == levelMenuScreen.unlockedLevel && level < 5) {
                levelMenuScreen.setUnlockedLevel(level+1);
            }
            // ... diğer işlemler
        }
    }






    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (levelManager.gameStarted) {
            // Oyuncuyu güncelle
            player.update(Gdx.graphics.getDeltaTime(), inputHandler);

            // Düşmanları güncelle
            enemyManager.update(player, levelManager);

            // Çarpışmaları kontrol et
            enemyManager.checkCollisions(player, levelManager);

            // Seviyeyi kontrol et
            levelManager.checkLevelUp(enemyManager.killedEnemies);
        }

        // Çizim işlemleri
        batch.begin();

        background.draw(batch);
        ground.draw(batch);
        player.draw(batch);
        enemyManager.draw(batch);
        hud.draw(batch, player.health, player.ammo);
        inputHandler.drawDpad(batch);
        inputHandler.drawShootButton(batch);

        if (levelManager.isGameOver) { // Önce oyunu bitti mi kontrol et
            if (!tryAgainDrawn) {
                font.draw(batch, "TRY AGAIN!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2);
                tryAgainDrawn = true;
            }
        } else if (!levelManager.gameStarted) {
            font.draw(batch, "WELCOME TO THE GAME!", Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "CLICK TO START!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 50);
        } else {
            tryAgainDrawn = false;
        }

        if (levelManager.gameStarted && !levelManager.isGameOver) {
            String killedEnemiesText = "Killed Enemies: " + enemyManager.killedEnemies + " / " + levelManager.levelTargets[levelManager.currentLevel - 1];
            float killedEnemiesWidth= font.draw(batch, killedEnemiesText, Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() - 20).width; // Önce Killed Enemies'i çiz ve genişliğini al

            font.draw(batch, "Level: " + levelManager.currentLevel, Gdx.graphics.getWidth() - 370 - killedEnemiesWidth + 80, Gdx.graphics.getHeight() - 20); // Level'ı sola hizala
        }

        if (levelManager.levelCompleted) {
            font.draw(batch, "LEVEL: " + (levelManager.currentLevel - 1) + " COMPLETED!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Click to continue", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);
        }

        batch.end();
    }

    public void resetGame() {
        // Oyunu sıfırla
        levelManager.reset();
        enemyManager.reset();
        player.reset();// Düşmanları oluşturma zamanlayıcısı
        tryAgainDrawn = false;

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
        batch.dispose();
        music.dispose();
        font.dispose();
        fontGen.dispose();
    }
}
