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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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


    private List<Potion> potions;
    private float potionSpawnTimer;
    private float potionSpawnDelay;
    private Random random;

    private Sound winSound;


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
        params.color=Color.BLACK;
        params.size=40;

        font=fontGen.generateFont(params);

        potions=new ArrayList<>();
        potionSpawnTimer=0;
        potionSpawnDelay=10f;//İksir çıkma aralığımız
        random=new Random();

        Gdx.input.setInputProcessor(inputHandler);


        winSound=Gdx.audio.newSound(Gdx.files.internal("winsound.mp3"));


        resetGame();
    }

    private void checkLevelCompleted() {
        if (levelCompleted) {
            if (level == levelMenuScreen.unlockedLevel && level < 5) {
                levelMenuScreen.setUnlockedLevel(level+1);
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
        ScreenUtils.clear(0, 0, 0, 1);

        if (levelManager.gameStarted) {
            // Oyuncuyu güncelle
            player.update(Gdx.graphics.getDeltaTime(), inputHandler);
            updatePotions(delta);
            player.checkPotionCollision(potions);//İksir çarpışması kontrolü

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

        for (Potion potion:potions){
            batch.draw(potion.texture,potion.x,potion.y,potion.width,potion.height);
        }



        if (levelManager.isGameOver) {
            if (!tryAgainDrawn) {
                font.draw(batch, "TRY AGAIN!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2);
                tryAgainDrawn = true;
            }
        } else if (!levelManager.gameStarted && levelManager.firstStart) { // Sadece oyun ilk kez başlatılıyorsa
            font.draw(batch, "LEVEL: " + (levelManager.currentLevel - 1) + " COMPLETED!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Click to continue", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);



        } else if (levelManager.gameStarted && !levelManager.isGameOver) { // Sadece oyun başladıysa ve bitmediyse

            // Önce label'ları çiz
            batch.draw(new Texture("label1.png"), Gdx.graphics.getWidth() - 850, Gdx.graphics.getHeight() - 110, 650, 100); // Killed Enemies label'ı

            // killedEnemiesWidth değişkenini tanımla
            String killedEnemiesText = "Killed Enemies: " + enemyManager.killedEnemies + " / " + levelManager.levelTargets[levelManager.currentLevel - 1];
            float killedEnemiesWidth = font.draw(batch, killedEnemiesText, Gdx.graphics.getWidth() - 800, Gdx.graphics.getHeight() - 40).width;

            batch.draw(new Texture("label.png"), Gdx.graphics.getWidth() - 670 - killedEnemiesWidth + 30, Gdx.graphics.getHeight() - 110, 300, 100); // Level label'ı

            // Metinleri çiz
            font.draw(batch, killedEnemiesText, Gdx.graphics.getWidth() - 800, Gdx.graphics.getHeight() - 40);
            font.draw(batch, "Level: " + levelManager.currentLevel, Gdx.graphics.getWidth() - 670 - killedEnemiesWidth + 80, Gdx.graphics.getHeight() - 40);
        }

        if (levelManager.levelCompleted) {
            font.draw(batch, "LEVEL: " + (levelManager.currentLevel - 1) + " COMPLETED!", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Click to continue", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);
            player.updatePlaneTexture(levelManager.currentLevel);
            background.setLevel(levelManager.currentLevel);

            if (!levelManager.winSoundPlayed) { // Sesi sadece bir kez çal
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

        player.draw(batch);
        enemyManager.draw(batch);
        hud.draw(batch, player.health, player.ammo);
        inputHandler.drawDpad(batch);
        inputHandler.drawShootButton(batch);

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

        for (Potion potion:potions){
            potion.dispose();
        }
    }
}
