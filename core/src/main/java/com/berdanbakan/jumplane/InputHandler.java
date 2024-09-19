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

    private Rectangle upRightButtonBounds;
    private Rectangle downRightButtonBounds;
    private Rectangle upLeftButtonBounds;
    private Rectangle downLeftButtonBounds;

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


    public boolean isUpButtonPressed = false;
    public boolean isDownButtonPressed = false;
    public boolean isLeftButtonPressed = false;
    public boolean isRightButtonPressed = false;

    public boolean isUpRightButtonPressed = false;
    public boolean isDownRightButtonPressed = false;
    public boolean isUpLeftButtonPressed = false;
    public boolean isDownLeftButtonPressed = false;


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
        dpadSize = 270;

        upButtonBounds = new Rectangle(dpadX + dpadSize / 3f, dpadY + 2 * dpadSize / 3f, dpadSize / 3f, dpadSize / 3f);
        downButtonBounds = new Rectangle(dpadX + dpadSize / 3f, dpadY, dpadSize / 3f, dpadSize / 3f);
        leftButtonBounds = new Rectangle(dpadX, dpadY + dpadSize / 3f, dpadSize / 3f, dpadSize/ 3f);
        rightButtonBounds = new Rectangle(dpadX + 2 * dpadSize / 3f, dpadY + dpadSize / 3f, dpadSize / 3f, dpadSize / 3f);

        upRightButtonBounds= new Rectangle(dpadX + 2 * dpadSize / 3f, dpadY + 2 * dpadSize / 3f, dpadSize / 3f, dpadSize / 3f);
        downRightButtonBounds = new Rectangle(dpadX + 2 * dpadSize / 3f, dpadY, dpadSize / 3f, dpadSize / 3f);
        upLeftButtonBounds = new Rectangle(dpadX, dpadY + 2 * dpadSize / 3f, dpadSize / 3f, dpadSize / 3f);
        downLeftButtonBounds = new Rectangle(dpadX, dpadY, dpadSize / 3f, dpadSize / 3f);

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
            }, SHOOT_DELAY * 4f);
        } else if (upRightButtonBounds.contains(screenX, screenY)) {
            isUpRightButtonPressed = true;
        } else if (downRightButtonBounds.contains(screenX, screenY)) {
            isDownRightButtonPressed = true;
        } else if (upLeftButtonBounds.contains(screenX, screenY)) {
            isUpLeftButtonPressed = true;
        } else if (downLeftButtonBounds.contains(screenX, screenY)) {
            isDownLeftButtonPressed = true;
        } else if (upButtonBounds.contains(screenX, screenY)) {
            isUpButtonPressed = true;
        } else if (downButtonBounds.contains(screenX, screenY)) {
            isDownButtonPressed = true;
        } else if (leftButtonBounds.contains(screenX, screenY)) {
            isLeftButtonPressed = true;
        } else if (rightButtonBounds.contains(screenX, screenY)) {
            isRightButtonPressed = true;
        }

        return true;
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
        } else if (upRightButtonBounds.contains(screenX, screenY)) {
            isUpRightButtonPressed = false;
        } else if (downRightButtonBounds.contains(screenX, screenY)) {
            isDownRightButtonPressed = false;
        } else if (upLeftButtonBounds.contains(screenX, screenY)) {
            isUpLeftButtonPressed = false;
        } else if (downLeftButtonBounds.contains(screenX, screenY)) {
            isDownLeftButtonPressed = false;
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
        player.shootBullet(levelManager.currentLevel, gameScreen.getAmmoSound(), gameScreen.getSwitchAmmoSound());
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

    public boolean isUpRightButtonPressed() {
        return Gdx.input.isTouched() && upRightButtonBounds.contains(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    public boolean isDownRightButtonPressed() {
        return Gdx.input.isTouched() && downRightButtonBounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    public boolean isUpLeftButtonPressed() {
        return Gdx.input.isTouched() && upLeftButtonBounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    public boolean isDownLeftButtonPressed() {return Gdx.input.isTouched() && downLeftButtonBounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
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

    public void dispose() {
        if (dpadTexture != null) {
            dpadTexture.dispose();
        }
        if (dpad_upTexture != null) {
            dpad_upTexture.dispose();
        }
        if (dpad_downTexture != null) {
            dpad_downTexture.dispose();
        }
        if (dpad_leftTexture != null) {
            dpad_leftTexture.dispose();
        }
        if (dpad_rightTexture != null) {
            dpad_rightTexture.dispose();
        }
        if (shootButtonTexture != null) {
            shootButtonTexture.dispose();
        }
        if (shootButtonPressedTexture != null) {
            shootButtonPressedTexture.dispose();
        }
    }
}