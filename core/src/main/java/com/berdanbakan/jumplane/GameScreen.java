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

        background=new Background();
        background.setLevel(level);


        ground = new Ground();


        enemyManager = new EnemyManager(); // Önce EnemyManager'ı oluşturun
        levelManager = new LevelManager(enemyManager); // Sonra LevelManager'ı oluşturun
        levelManager.currentLevel=this.level;
        player = new Player(ground,levelManager);


        inputHandler = new InputHandler(this, player, levelManager, enemyManager); // GameScreen instance'ını InputHandler'a geçiyoruz

        hud = new HUD();

        fontGen=new FreeTypeFontGenerator(Gdx.files.internal("negrita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params= new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.color=new Color(0x4681F4FF);
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

        if (levelManager.isGameOver) {
            if (!tryAgainDrawn) {
                font.draw(batch, "TRY AGAIN!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2);
                tryAgainDrawn = true;
            }
        } else if (!levelManager.gameStarted && levelManager.firstStart) { // Sadece oyun ilk kez başlatılıyorsa
            font.draw(batch, "LEVEL: " + (levelManager.currentLevel - 1) + " COMPLETED!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Click to continue", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);



        } else if (levelManager.gameStarted && !levelManager.isGameOver) { // Sadece oyun başladıysa ve bitmediyse
            String killedEnemiesText = "Killed Enemies: " + enemyManager.killedEnemies + " / " + levelManager.levelTargets[levelManager.currentLevel - 1];
            float killedEnemiesWidth = font.draw(batch, killedEnemiesText, Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() - 20).width;

            font.draw(batch, "Level: " + levelManager.currentLevel, Gdx.graphics.getWidth() - 370 - killedEnemiesWidth + 80, Gdx.graphics.getHeight() - 20);
        }

        if (levelManager.levelCompleted) {
            font.draw(batch, "LEVEL: " + (levelManager.currentLevel - 1) + " COMPLETED!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Click to continue", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);
            player.updatePlaneTexture(levelManager.currentLevel);
            background.setLevel(levelManager.currentLevel);

            if (Gdx.input.justTouched()) {
                levelManager.levelCompleted = false;
                levelManager.gameStarted = false;
                levelManager.firstStart = false;
                player.reset();


            }
        }

        batch.end();
    }

    public void resetGame() {
        // Oyunu sıfırla
        levelManager.reset();
        enemyManager.reset();
        // Düşmanları oluşturma zamanlayıcısı
        tryAgainDrawn = false;
        player.health=6;

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
