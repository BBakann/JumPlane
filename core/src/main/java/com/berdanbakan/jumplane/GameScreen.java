package com.berdanbakan.jumplane;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameScreen implements Screen {

    private final JumPlane game;
    private SpriteBatch batch;
    private Player player;
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

    private List<Potion> potions;
    private float potionSpawnTimer;
    private float potionSpawnDelay;
    private Random random;

    private Sound winSound;
    private Texture labelTexture;

    private boolean levelCompleted = false;

    private Texture stopButtonTexture;
    private Rectangle stopButtonRectangle;
    private boolean isPaused = false;
    private ShapeRenderer shapeRenderer; // Menü arka planı için


    public GameScreen(JumPlane game, int level, LevelMenuScreen levelMenuScreen) {
        this.game = game;
        this.level = level;
        this.levelMenuScreen = levelMenuScreen;
        batch = new SpriteBatch();

        background = new Background();
        background.setLevel(level);

        ground = new Ground();

        enemyManager = new EnemyManager();
        levelManager =new LevelManager(enemyManager,this);
        levelManager.currentLevel = this.level;
        player = new Player(ground, levelManager);

        inputHandler = new InputHandler(this, player, levelManager, enemyManager);

        hud = new HUD();

        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("negrita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.color = Color.BLACK;
        params.size = 40;

        font = fontGen.generateFont(params);

        potions = new ArrayList<>();
        potionSpawnTimer = 0;
        potionSpawnDelay = 10f;
        random = new Random();

        Gdx.input.setInputProcessor(inputHandler);


        winSound = Gdx.audio.newSound(Gdx.files.internal("winsound.mp3"));
        labelTexture = new Texture("label.png");

        stopButtonTexture = new Texture("stopbutton.png");
        stopButtonRectangle = new Rectangle(Gdx.graphics.getWidth() - stopButtonTexture.getWidth() * 1.5f - 80, // Sola kaydırmak için çarpanı ve sabiti ayarlayın
                Gdx.graphics.getHeight() - stopButtonTexture.getHeight() * 1.5f-40, // Sola kaydırmak için çarpanı ve sabiti ayarlayın
                stopButtonTexture.getWidth() * 2f, // Büyütmek için çarpanı ayarlayın
                stopButtonTexture.getHeight() * 2f // Büyütmek için çarpanı ayarlayın
        );
        shapeRenderer = new ShapeRenderer();


        resetGame();
    }

    private void checkLevelCompleted() {
        if (levelCompleted) {
            if (level == levelMenuScreen.unlockedLevel && level < 5) {
                levelMenuScreen.setUnlockedLevel(level + 1);
            }
        }
    }

    private void spawnPotion(){
        float potionX = Gdx.graphics.getWidth();
        float potionY = random.nextFloat() * (Gdx.graphics.getHeight() - ground.groundHeight - 100) + ground.groundHeight; // Yere değmeyecek şekilde rastgele Y pozisyonu
        Potion.PotionType type = random.nextBoolean() ? Potion.PotionType.HEALTH : Potion.PotionType.POISON; // Rastgele iksir tipi

        Potion potion = new Potion(potionX, potionY, type);
        potions.add(potion);
    }

    private void updatePotions(float deltaTime){
        potionSpawnTimer+=deltaTime;
        if (potionSpawnTimer>=potionSpawnDelay){
            potionSpawnTimer=0;
            spawnPotion();
        }

        Iterator<Potion> iter = potions.iterator();
        while (iter.hasNext()) {
            Potion potion = iter.next();
            potion.x -= 200 * deltaTime; // İksirleri sola doğru hareket ettir
            potion.rectangle.set(potion.x, potion.y, potion.width, potion.height);

            if (potion.x < -potion.width) {
                iter.remove();
            }
        }

    }






    @Override
    public void show() {
        game.stopMusic(); // Önceki müziği durdur
        game.playMusic("gamemusic.mp3");
    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);

        if (!isPaused) {
            updateGameLogic(deltaTime);
        }
        drawGame(deltaTime);

        if (isPaused) {
            handlePauseMenuInput();
        }
    }

    private void updateGameLogic(float deltaTime) {
        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (stopButtonRectangle.contains(touchX, touchY)) {
                isPaused = true;
                return; // Dokunma işlemi durdurma düğmesi üzerindeyse, diğer güncellemeleri yapma
            }
        }

        if (!isPaused) {
            if (levelManager.gameStarted) {
                player.update(deltaTime, inputHandler);
                updatePotions(deltaTime);
                player.checkPotionCollision(potions);
                enemyManager.update(player, levelManager);
                enemyManager.checkCollisions(player, levelManager);
                levelManager.checkLevelUp(enemyManager.killedEnemies);
            }
        }
    }
    private void drawGame(float deltaTime) {
        batch.begin();

        background.draw(batch);
        ground.draw(batch);

        for (Potion potion : potions) {
            batch.draw(potion.texture, potion.x, potion.y, potion.width, potion.height);
        }

        if (levelManager.isGameOver) {

            font.getData().setScale(2f);
            font.draw(batch, "TRY AGAIN!", Gdx.graphics.getWidth() / 2 - 320, Gdx.graphics.getHeight() / 2+20);
            font.getData().setScale(1f);

        } else if (!levelManager.gameStarted && levelManager.firstStart) {
            drawLevelCompletedMessage(levelManager.currentLevel - 1);
        } else if (levelManager.gameStarted && !levelManager.isGameOver) {
            drawGameLabels();
        }

        if (levelManager.levelCompleted) {
            drawLevelCompletedMessage(levelManager.currentLevel - 1);
            handleLevelCompleted();
        }

        player.draw(batch);
        enemyManager.draw(batch);
        hud.draw(batch, player.health, player.ammo);
        inputHandler.drawDpad(batch);
        inputHandler.drawShootButton(batch);
        batch.draw(stopButtonTexture, stopButtonRectangle.x, stopButtonRectangle.y, stopButtonTexture.getWidth() * 2f, stopButtonTexture.getHeight() * 2f);

        batch.end();
        if (isPaused) {
            drawPauseMenu();
        }
    }
    private void drawPauseMenu() {
        // Yarı Saydam dikdörtgen çizimi;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.5f);// siyah,%50 saydam
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


        batch.begin();
        font.getData().setScale(2f);
        // "Devam Et" label'ı
        batch.draw(labelTexture, Gdx.graphics.getWidth() / 2f - 280f, Gdx.graphics.getHeight() / 2f + 10f, 580, 80);

        font.draw(batch, "DEVAM ET !", Gdx.graphics.getWidth() / 2f - 245f, Gdx.graphics.getHeight() / 2f + 80f); // Sola kaydırmak için x koordinatını azaltın

        //"Ana Menü" label'ı
        batch.draw(labelTexture, Gdx.graphics.getWidth() / 2f - 280f, Gdx.graphics.getHeight() / 2f - 150f, 580, 80);
        font.draw(batch, "ANA MENU !", Gdx.graphics.getWidth() / 2f - 245f, Gdx.graphics.getHeight() / 2f - 80f); // Sola kaydırmak için x koordinatını azaltın
        font.getData().setScale(1f);
        batch.end();
    }
    private void handlePauseMenuInput() {
        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Devam Et düğmesi
            if (touchX > Gdx.graphics.getWidth() / 2f - 300f && touchX < Gdx.graphics.getWidth() / 2f + 300f &&
                    touchY >Gdx.graphics.getHeight() / 2f - 50f && touchY < Gdx.graphics.getHeight() / 2f + 150f) {
                isPaused = false;
            }

            // Ana Menü düğmesi - dokunma alanını genişlettik
            if (touchX > Gdx.graphics.getWidth() / 2f - 300f && touchX < Gdx.graphics.getWidth() / 2f + 300f &&
                    touchY > Gdx.graphics.getHeight() / 2f - 200f && touchY < Gdx.graphics.getHeight() / 2f - 0f) {
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    private void drawLevelCompletedMessage(int level) {
        font.getData().setScale(2f);
        font.draw(batch, "LEVEL: " + level + " COMPLETED!", Gdx.graphics.getWidth() / 2 - 500, Gdx.graphics.getHeight() / 2);
        font.getData().setScale(1f);
    }

    private void handleLevelCompleted() {
        player.updatePlaneTexture(levelManager.currentLevel);
        background.setLevel(levelManager.currentLevel);

        if (!levelManager.winSoundPlayed) {
            winSound.play();
            levelManager.winSoundPlayed = true;
        }

        if (Gdx.input.justTouched()) {
            levelManager.levelCompleted = false;
            levelManager.gameStarted = true;
            levelManager.firstStart = false;
        }
    }

    private void drawGameLabels() {
        String killedEnemiesText = "Killed Enemies: " + enemyManager.killedEnemies + " / " + levelManager.levelTargets[levelManager.currentLevel - 1];
        float killedEnemiesWidth = font.draw(batch, killedEnemiesText, Gdx.graphics.getWidth() - 800, Gdx.graphics.getHeight() - 40).width;

        batch.draw(labelTexture, Gdx.graphics.getWidth() - 850, Gdx.graphics.getHeight() - 110, 650, 100);
        batch.draw(labelTexture, Gdx.graphics.getWidth() - 670 - killedEnemiesWidth + 30, Gdx.graphics.getHeight() - 110, 300, 100);

        font.draw(batch, killedEnemiesText, Gdx.graphics.getWidth() - 800, Gdx.graphics.getHeight() - 40);
        font.draw(batch, "Level: " + levelManager.currentLevel, Gdx.graphics.getWidth() - 670 - killedEnemiesWidth + 80, Gdx.graphics.getHeight() - 40);
    }

    public void resetGame() {
        levelManager.reset();
        enemyManager.reset();
        player.health = 6;
        potions.clear();
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
        font.dispose();
        fontGen.dispose();
        winSound.dispose();
        labelTexture.dispose();
        stopButtonTexture.dispose();
        shapeRenderer.dispose();
        game.stopMusic();

        for (Potion potion : potions) {
            potion.dispose();
        }
    }
}
