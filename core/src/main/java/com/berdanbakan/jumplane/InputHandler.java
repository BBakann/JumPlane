package com.berdanbakan.jumplane;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

public class InputHandler extends InputAdapter {
    private float dpadX;
    private float dpadY;
    private float dpadSize;

    private Texture dpadTexture;
    private Texture dpad_upTexture;
    private Texture dpad_downTexture;
    private Texture dpad_leftTexture;
    private Texture dpad_rightTexture;

    private JumPlane game;

    private Rectangle upButtonBounds;
    private Rectangle downButtonBounds;
    private Rectangle leftButtonBounds;
    private Rectangle rightButtonBounds;

    private Texture shootButtonTexture;
    private Texture shootButtonPressedTexture;
    private boolean isButtonPressed;
    private Rectangle buttonRectangle;

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private final GameScreen gameScreen;

    private boolean gameStarted=false;
    private boolean dugmeGeciciOlarakBasili;
    private static final float SHOOT_DELAY = 0.125f;

    // Uçak pozisyonu
    private float planeX; // Uçağın yatay pozisyonu (sabit)
    private float planeY; // Uçağın dikey pozisyonu
    private float planeSpeed = 450f;

    private float planeWidth;
    private float planeHeight;

    private float groundHeight;

    public boolean isUpButtonPressed = false;
    public boolean isDownButtonPressed = false;
    public boolean isLeftButtonPressed = false;
    public boolean isRightButtonPressed = false;


    public InputHandler(GameScreen gameScreen,Player player,LevelManager levelManager,EnemyManager enemyManager) {
        this.player = player;
        this.levelManager=levelManager;
        this.gameScreen=gameScreen;
        this.enemyManager=enemyManager;

        dpadTexture=new Texture("dpad.png");
        dpad_leftTexture=new Texture("dpad_left.png");
        dpad_rightTexture=new Texture("dpad_right.png");
        dpad_upTexture=new Texture("dpad_up.png");
        dpad_downTexture=new Texture("dpad_down.png");
        dpadX = 50;
        dpadY = 50;
        dpadSize = 250;

        upButtonBounds = new Rectangle(dpadX + dpadSize / 3f, dpadY + 2 * dpadSize / 3f, dpadSize / 3f, dpadSize / 3f);
        downButtonBounds = new Rectangle(dpadX + dpadSize / 3f, dpadY, dpadSize / 3f, dpadSize / 3f);
        leftButtonBounds = new Rectangle(dpadX, dpadY + dpadSize / 3f, dpadSize / 3f, dpadSize/ 3f);
        rightButtonBounds = new Rectangle(dpadX + 2 * dpadSize / 3f, dpadY + dpadSize / 3f, dpadSize / 3f, dpadSize / 3f);

        shootButtonTexture = new Texture("shootbutton.png");
        shootButtonPressedTexture = new Texture("shootbuttonpressed.png");
        buttonRectangle = new Rectangle(Gdx.graphics.getWidth() - 400, 50, 300, 240);
        isButtonPressed = false;

    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY; // Y koordinatını ters çevir

        if (levelManager.isGameOver) {
            gameScreen.resetGame(); // Oyunu sıfırla
            levelManager.isGameOver = false; // Oyun bitti durumunu sıfırla
            return true;
        }

        if (levelManager.levelCompleted) {
            levelManager.levelCompleted = false;
            levelManager.gameStarted = true;
            return true;
        }

        if (!levelManager.gameStarted) {
            levelManager.gameStarted = true;
            levelManager.reset(); // Oyunu sıfırla
            return true;
        }

        if (buttonRectangle.contains(screenX, screenY)) {
            isButtonPressed = true;
            dugmeGeciciOlarakBasili = true; // Düğmeye basıldığında bayrağı ayarla
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    shootBullet();
                    dugmeGeciciOlarakBasili = false; // Ateş ettikten sonra bayrağı sıfırla
                }
            }, SHOOT_DELAY * 1.55f);
        } else if (upButtonBounds.contains(screenX, screenY)) {
            isUpButtonPressed = true;
        } else if (downButtonBounds.contains(screenX, screenY)) {
            isDownButtonPressed = true;
        } else if (leftButtonBounds.contains(screenX, screenY)) {
            isLeftButtonPressed = true;
        } else if (rightButtonBounds.contains(screenX, screenY)) {
            isRightButtonPressed = true;
        }return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY; // Y koordinatını ters çevir

        if (buttonRectangle.contains(screenX, screenY)) {
            isButtonPressed = false;
        } else if (upButtonBounds.contains(screenX, screenY)) {
            isUpButtonPressed = false;
        } else if (downButtonBounds.contains(screenX, screenY)) {
            isDownButtonPressed = false;
        } else if (leftButtonBounds.contains(screenX, screenY)) {
            isLeftButtonPressed = false;
        } else if (rightButtonBounds.contains(screenX, screenY)) {
            isRightButtonPressed = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override
    public boolean scrolled(float amountX, float amountY) { return false; }



    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyUp(int keycode) { return false; }
    @Override
    public boolean keyTyped(char character) { return false; }
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }



    public void drawDpad(SpriteBatch batch) {
        int screenX = Gdx.input.getX();
        int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (Gdx.input.isTouched()) {
            if (upButtonBounds.contains(screenX, screenY)) {
                batch.draw(dpad_upTexture, dpadX, dpadY, dpadSize, dpadSize);
            } else if (downButtonBounds.contains(screenX, screenY)) {
                batch.draw(dpad_downTexture, dpadX, dpadY, dpadSize, dpadSize);
            } else if (leftButtonBounds.contains(screenX, screenY)) {
                batch.draw(dpad_leftTexture, dpadX, dpadY, dpadSize, dpadSize);
            } else if (rightButtonBounds.contains(screenX, screenY)) {
                batch.draw(dpad_rightTexture, dpadX, dpadY, dpadSize, dpadSize);
            } else {

                batch.draw(dpadTexture, dpadX, dpadY, dpadSize, dpadSize);
            }
        } else {

            batch.draw(dpadTexture, dpadX, dpadY, dpadSize, dpadSize);
        }

    }

    public void drawShootButton(SpriteBatch batch) {
        if (isButtonPressed) {
            batch.draw(shootButtonPressedTexture, buttonRectangle.x, buttonRectangle.y, buttonRectangle.width, buttonRectangle.height);
        } else {
            batch.draw(shootButtonTexture, buttonRectangle.x, buttonRectangle.y, buttonRectangle.width, buttonRectangle.height);
        }
    }


    private void shootBullet() {
        player.shootBullet(levelManager.currentLevel);
    }

    public boolean isUpButtonPressed() {
        return Gdx.input.isTouched() && upButtonBounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    public boolean isDownButtonPressed() {
        return Gdx.input.isTouched() && downButtonBounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    public boolean isLeftButtonPressed() {
        return Gdx.input.isTouched() && leftButtonBounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());}

    public boolean isRightButtonPressed() {
        return Gdx.input.isTouched() && rightButtonBounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    public boolean isButtonPressed() {
        return isButtonPressed;
    }

    public Rectangle getButtonRectangle() {
        return buttonRectangle;
    }

    public float getDpadX() {
        return dpadX;
    }

    public float getDpadY() {
        return dpadY;
    }

    public float getDpadSize() {
        return dpadSize;
    }

    public Rectangle getUpButtonBounds() {
        return upButtonBounds;
    }

    public Rectangle getDownButtonBounds() {
        return downButtonBounds;
    }

    public Rectangle getLeftButtonBounds() {
        return leftButtonBounds;
    }

    public Rectangle getRightButtonBounds() {
        return rightButtonBounds;
    }
}
