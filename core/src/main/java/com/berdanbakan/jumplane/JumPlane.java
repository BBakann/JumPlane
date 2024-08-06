package com.berdanbakan.jumplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

public class JumPlane extends ApplicationAdapter {

    private SpriteBatch batch;
    private Music music;
    private Player player;
    private boolean tryAgainDrawn = false; // TRY AGAIN! yazısı çizildi mi?
    private InputHandler inputHandler;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private Background background;
    private Ground ground;private HUD hud;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.play();

        ground = new Ground();
        player = new Player(ground);
        levelManager = new LevelManager(enemyManager);
        inputHandler = new InputHandler(this, player, levelManager, enemyManager);
        enemyManager = new EnemyManager();
        background = new Background();
        hud = new HUD();

        font = new BitmapFont();
        font.getData().setScale(3.5f);
        font.setColor(Color.BLACK);




        Gdx.input.setInputProcessor(inputHandler);

        resetGame();
    }

    @Override
    public void render() {
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
            font.draw(batch, "Level: " + levelManager.currentLevel, 20, Gdx.graphics.getHeight() - 90);
            font.draw(batch, "Killed Enemies: " + enemyManager.killedEnemies + " / " + levelManager.levelTargets[levelManager.currentLevel - 1], Gdx.graphics.getWidth() - 570, Gdx.graphics.getHeight() - 20);
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
        tryAgainDrawn=false;



        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!levelManager.isGameOver && levelManager.gameStarted) {
                    enemyManager.spawnFlyingEnemy();
                }
            }
        }, levelManager.flyingEnemySpawnInterval, levelManager.flyingEnemySpawnInterval);

        // Yaratıkları oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!levelManager.isGameOver && levelManager.gameStarted) {
                    enemyManager.spawnCreature();
                }
            }
        }, levelManager.creatureSpawnInterval, levelManager.creatureSpawnInterval);

        // Engelleri oluşturma zamanlayıcısı
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!levelManager.isGameOver && levelManager.gameStarted) {
                    enemyManager.spawnObstacle();
                }
            }
        }, levelManager.obstacleSpawnInterval, levelManager.obstacleSpawnInterval);
    }

    @Override
    public void dispose() {
        batch.dispose();
        music.dispose();
        player.dispose();
        enemyManager.dispose();
        background.dispose();
        ground.dispose();
        hud.dispose();
        font.dispose();
    }
}
