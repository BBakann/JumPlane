package com.berdanbakan.jumplane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlyingEnemy5 extends FlyingEnemy {
    public float width = 641 / 3f;
    public float height = 546 / 3f;

    private Texture texture;

    public FlyingEnemy5(float x, float y, float speed, float width, float height) {
        super(x, y, speed, width, height);
        texture = new Texture("enemyplane5_1.png");
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);

        // Düşman mermilerini çiz
        for (Bullet bullet : getBullets()) {
            batch.draw(enemyBulletTexture, bullet.x, bullet.y, bullet.width, bullet.height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
    }
}
