package com.berdanbakan.jumplane;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameScreen implements Screen {

    public final JumPlane game;
    private SpriteBatch batch;
    private Player player;
    private InputHandler inputHandler;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private Background background;
    private Ground ground;
    private HUD hud;
    private BitmapFont font;


    private int level;
    private LevelMenuScreen levelMenuScreen;

    private List<Potion> potions;
    private float potionSpawnTimer;
    private float potionSpawnDelay;
    private Random random;

    private float coinSpawnTimer;
    private float coinSpawnDelay = 5f;

    private Sound winSound;
    private Texture labelTexture;

    private boolean levelCompleted = false;

    private Texture stopButtonTexture;
    private Rectangle stopButtonRectangle;
    private boolean isPaused = false;
    private ShapeRenderer shapeRenderer;

    private Texture loadingBackgroundTexture;
    public boolean isLoading = false;

    private List<Coin> coins;
    private int collectedCoins;

    public static String currentLanguage = "en";
    private String killedEnemiesText_en = "Shooted Enemies: ";
    private String levelText_en = "Level: ";
    private String coinsText_en = "Coins: ";
    private String tryAgainText_en = "TRY AGAIN!";
    private String touchToStartText_en = "TOUCH AND START!";

    private String levelText_tr = "Seviye: ";
    private String coinsText_tr = "Altin: ";
    private String tryAgainText_tr = "TEKRAR DENE!";
    private String touchToStartText_tr = "DOKUN VE BASLA!";
    private String killedEnemiesText_tr = "Vurulan Canavar: ";

    private Preferences prefs;

    public GameScreen(JumPlane game, int level, LevelMenuScreen levelMenuScreen) {
        this.game = game;
        this.level = level;
        this.levelMenuScreen = levelMenuScreen;
        batch = new SpriteBatch();

        prefs = Gdx.app.getPreferences("My Preferences");

        background = new Background();
        background.setLevel(level);

        ground = new Ground();

        enemyManager = new EnemyManager();
        levelManager =new LevelManager(enemyManager,this,batch);
        levelManager.currentLevel = this.level;
        enemyManager.setLevel(levelManager.currentLevel);
        player = new Player(ground, levelManager);

        inputHandler = new InputHandler(this, player, levelManager, enemyManager);

        hud = new HUD();

        font = new BitmapFont(Gdx.files.internal("negrita.fnt"), Gdx.files.internal("negrita.png"), false);
        font.setColor(Color.BLACK);



        potions = new ArrayList<>();
        potionSpawnTimer = 0;
        potionSpawnDelay = 10f;
        random = new Random();

        Gdx.input.setInputProcessor(inputHandler);
        checkLevelCompleted();

        coins = new ArrayList<>();
        random = new Random();


        winSound = Gdx.audio.newSound(Gdx.files.internal("winsound.mp3"));
        labelTexture = new Texture("label.png");

        stopButtonTexture = new Texture("stopbutton.png");
        stopButtonRectangle = new Rectangle(Gdx.graphics.getWidth() - stopButtonTexture.getWidth() * 1.5f - 80,
                Gdx.graphics.getHeight() - stopButtonTexture.getHeight() * 1.5f-40,
                stopButtonTexture.getWidth() * 2f,
                stopButtonTexture.getHeight() * 2f
        );
        shapeRenderer = new ShapeRenderer();

        loadingBackgroundTexture = new Texture("levelmenubackground.png");

        resetGame();
    }

    public void checkLevelCompleted() {
        if (levelCompleted) {
            if (level == levelMenuScreen.unlockedLevel && level < 5) {
                levelMenuScreen.setUnlockedLevel(level + 1);
            }
        }
    }

    private void spawnPotion(){
        float potionX = Gdx.graphics.getWidth();
        float potionY = random.nextFloat() * (Gdx.graphics.getHeight() - ground.groundHeight - 100) + ground.groundHeight;
        Potion.PotionType type = random.nextBoolean() ? Potion.PotionType.HEALTH : Potion.PotionType.POISON;

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
            potion.x -= 200 * deltaTime;
            potion.rectangle.set(potion.x, potion.y, potion.width, potion.height);

            if (potion.x < -potion.width) {
                iter.remove();
            }
        }

    }

    private void updateCoins(float deltaTime) {
        coinSpawnTimer += deltaTime;
        if (coinSpawnTimer >= coinSpawnDelay) {
            coinSpawnTimer = 0;
            spawnCoin();
        }

        Iterator<Coin> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Coin coin = coinIterator.next();
            coin.update(deltaTime);
            if (!coin.collected && player.playerPlaneRectangle.overlaps(coin.rectangle)) {
                coin.collected = true;
                collectedCoins++;
                coin.playSound();
            }
            if (coin.x < -coin.width) {
                coin.dispose();
                coinIterator.remove();
            }
        }
    }

    private void spawnCoin() {
        float coinX = Gdx.graphics.getWidth();
        float coinY = random.nextFloat() * (Gdx.graphics.getHeight() - ground.groundHeight - 100) + ground.groundHeight;
        coins.add(new Coin(coinX, coinY));
    }

    @Override
    public void show() {
        game.stopMusic();
        game.playMusic("gamemusic.mp3");
    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0,1);

        if (!isPaused) {
            if (!isLoading) {
                updateGameLogic(deltaTime);
            }
        }

        drawGame(deltaTime);

        if (isPaused) {
            handlePauseMenuInput();
        }
    }

    private void updateGameLogic(float deltaTime) {
        if (!isLoading && Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (stopButtonRectangle.contains(touchX, touchY)) {
                isPaused = true;
                return;
            }
        }

        if (!isPaused && !isLoading) {
            if (levelManager.gameStarted) {
                player.update(deltaTime, inputHandler);
                updatePotions(deltaTime);
                player.checkPotionCollision(potions);
                enemyManager.update(player, levelManager);
                enemyManager.checkCollisions(player, levelManager);
                levelManager.checkLevelUp(enemyManager.killedEnemies, collectedCoins);
                updateCoins(deltaTime);
            }
        }
    }

    private void drawGame(float deltaTime) {
        batch.begin();

        background.draw(batch);
        ground.draw(batch);

        if (!isLoading) {
            for (Potion potion : potions) {
                batch.draw(potion.texture, potion.x, potion.y, potion.width, potion.height);
            }

            for (Coin coin : coins) {
                coin.draw(batch);
            }

            if (levelManager.isGameOver) {
                font.getData().setScale(2f);
                if (currentLanguage.equals("tr")) {
                    font.draw(batch, tryAgainText_tr, Gdx.graphics.getWidth() / 2 - 320, Gdx.graphics.getHeight() / 2 + 20);
                } else {
                    font.draw(batch, tryAgainText_en, Gdx.graphics.getWidth() / 2 - 320, Gdx.graphics.getHeight() / 2 + 20);
                }
                font.getData().setScale(1f);
            } else if (!levelManager.gameStarted && levelManager.firstStart) {
                font.getData().setScale(2f);
                if (currentLanguage.equals("tr")) {
                    font.draw(batch, touchToStartText_tr, Gdx.graphics.getWidth() / 2 - 400, Gdx.graphics.getHeight() / 2);
                } else {
                    font.draw(batch, touchToStartText_en, Gdx.graphics.getWidth() / 2 - 400, Gdx.graphics.getHeight() / 2);
                }
                font.getData().setScale(1f);
            } else if (levelManager.gameStarted && !levelManager.isGameOver) {
                drawGameLabels();
            }

            player.draw(batch);
            enemyManager.draw(batch);
            hud.draw(batch, player.health, player.ammo);
            inputHandler.drawDpad(batch);
            inputHandler.drawShootButton(batch);
            batch.draw(stopButtonTexture, stopButtonRectangle.x, stopButtonRectangle.y, stopButtonTexture.getWidth() * 2f, stopButtonTexture.getHeight() * 2f);

        } else {

            batch.draw(loadingBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            font.getData().setScale(2f);
            batch.draw(labelTexture, Gdx.graphics.getWidth() / 2f - 330f, Gdx.graphics.getHeight() / 2f - 450f, 700, 200);
            font.draw(batch, "LOADING...", Gdx.graphics.getWidth() / 2f - 240f, Gdx.graphics.getHeight() / 2f - 300f);
            font.getData().setScale(1f);

        }

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

        batch.draw(labelTexture, Gdx.graphics.getWidth() / 2f - 280f, Gdx.graphics.getHeight() / 2f + 10f, 580, 150);

        font.draw(batch, "CONTINUE !", Gdx.graphics.getWidth() / 2f - 234f, Gdx.graphics.getHeight() / 2f + 145f);


        batch.draw(labelTexture, Gdx.graphics.getWidth() / 2f - 280f, Gdx.graphics.getHeight() / 2f - 200f, 580, 150);
        font.draw(batch, "ANA MENU !", Gdx.graphics.getWidth() / 2f - 254f, Gdx.graphics.getHeight() / 2f - 65f);
        font.getData().setScale(1f);
        batch.end();
    }

    private void handlePauseMenuInput() {
        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Devam Et düğmesi
            if (touchX> Gdx.graphics.getWidth() / 2f - 300f && touchX < Gdx.graphics.getWidth() / 2f + 300f &&
                    touchY > Gdx.graphics.getHeight() / 2f - 50f && touchY < Gdx.graphics.getHeight() / 2f + 150f) {
                isPaused = false;
            }


            if (touchX > Gdx.graphics.getWidth() / 2f - 300f && touchX< Gdx.graphics.getWidth() / 2f + 300f &&
                    touchY > Gdx.graphics.getHeight() / 2f - 200f && touchY < Gdx.graphics.getHeight() / 2f - 0f) {
                levelMenuScreen.show();
                game.setScreen(levelMenuScreen);
            }
        }
    }

    public void handleLevelCompleted() {
        if (!levelManager.winSoundPlayed) {
            winSound.play();
            levelManager.winSoundPlayed = true;
        }

        isLoading = true;
        levelManager.gameStarted = false;

        if (levelManager.currentLevel > prefs.getInteger("unlockedLevel", 1)) {
            prefs.putInteger("unlockedLevel", levelManager.currentLevel);
            prefs.flush();


            levelMenuScreen.setUnlockedLevel(levelManager.currentLevel);
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                player.updatePlaneTexture(levelManager.currentLevel);
                background.setLevel(levelManager.currentLevel);
                levelManager.levelCompleted = false;
                levelManager.gameStarted = true;
                levelManager.firstStart = false;
                isLoading = false;
            }
        }, 4f);
    }

    private void drawGameLabels() {
        String killedEnemiesText, levelText, coinsText;

        if (currentLanguage.equals("tr")) {
            killedEnemiesText = killedEnemiesText_tr+ enemyManager.killedEnemies + " / " + levelManager.levelTargets[levelManager.currentLevel - 1];
            levelText = levelText_tr + levelManager.currentLevel;
            coinsText = coinsText_tr + collectedCoins + "/" + levelManager.levelCoinTargets[levelManager.currentLevel - 1];
        } else {
            killedEnemiesText = killedEnemiesText_en + enemyManager.killedEnemies + " / " + levelManager.levelTargets[levelManager.currentLevel - 1];
            levelText =levelText_en + levelManager.currentLevel;
            coinsText = coinsText_en + collectedCoins + "/" + levelManager.levelCoinTargets[levelManager.currentLevel - 1];
        }


        batch.draw(labelTexture, Gdx.graphics.getWidth() - 850, Gdx.graphics.getHeight() - 110, 650, 100);

        float killedEnemiesWidth = font.draw(batch, killedEnemiesText, Gdx.graphics.getWidth() - 830, Gdx.graphics.getHeight() - 40).width;
        batch.draw(labelTexture, Gdx.graphics.getWidth() - 670 - killedEnemiesWidth + 30, Gdx.graphics.getHeight() - 110, 300, 100);

        font.draw(batch, levelText, Gdx.graphics.getWidth() - 670 - killedEnemiesWidth + 80, Gdx.graphics.getHeight() - 40);

        float levelTextX = Gdx.graphics.getWidth() - 670 - killedEnemiesWidth +80;
        float spacing = 30 + killedEnemiesWidth - 500;
        float labelWidth = 300;

        batch.draw(labelTexture, levelTextX - labelWidth - spacing, Gdx.graphics.getHeight() - 110, labelWidth, 100);
        font.draw(batch, coinsText, levelTextX - labelWidth - spacing + 25, Gdx.graphics.getHeight() - 40);
    }

    public void resetGame() {
        levelManager.reset();
        enemyManager.reset();
        player.health = 6;
        potions.clear();
        player.resetPosition();

        collectedCoins = 0;
        coins.clear();

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
        winSound.dispose();
        labelTexture.dispose();
        stopButtonTexture.dispose();
        shapeRenderer.dispose();
        game.stopMusic();
        loadingBackgroundTexture.dispose();


        for (Potion potion : potions) {
            potion.dispose();
        }
        for (Coin coin : coins) {
            coin.dispose();
        }
    }
}
