package com.berdanbakan.jumplane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private Music music;
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


    public GameScreen(JumPlane game, int level, LevelMenuScreen levelMenuScreen) {
        this.game = game;
        this.level = level;
        this.levelMenuScreen = levelMenuScreen;
        batch = new SpriteBatch();

        background = new Background();
        background.setLevel(level);

        ground = new Ground();

        enemyManager = new EnemyManager();
        levelManager =new LevelManager(enemyManager);
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
    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);

        updateGameLogic(deltaTime);
        drawGame(deltaTime);
    }

    private void updateGameLogic(float deltaTime) {
        if (levelManager.gameStarted) {
            player.update(deltaTime, inputHandler);
            updatePotions(deltaTime);
            player.checkPotionCollision(potions);
            enemyManager.update(player, levelManager);
            enemyManager.checkCollisions(player, levelManager);
            levelManager.checkLevelUp(enemyManager.killedEnemies);
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
            font.draw(batch, "TRY AGAIN!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2);
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

        batch.end();
    }

    private void drawLevelCompletedMessage(int level) {
        font.draw(batch, "LEVEL: " + level + " COMPLETED!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
        font.draw(batch, "Click to continue", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);
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
            levelManager.gameStarted = false;
            levelManager.firstStart = false;
            player.reset();
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
        labelTexture.dispose(); // Önbelleğe alınan dokuyu serbest bırakın

        for (Potion potion : potions) {
            potion.dispose();
        }
    }
}
