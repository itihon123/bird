package ru.samsung.gamestudio.characters;

import static ru.samsung.gamestudio.MyGdxGame.SCR_HEIGHT;
import static ru.samsung.gamestudio.MyGdxGame.SCR_WIDTH;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.Random;

public class Tube {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MOVING = 1;

    Texture textureUpperTube;
    Texture textureDownTube;
    Texture textureCoin;

    Random random;
    int x, gapY, distanceBetweenTubes;
    boolean isPointReceived;

    int speed = 10;
    final int width = 200;
    final int height = 700;
    int gapHeight = 400;
    int padding = 100;

    int type;
    int speedY = 3;
    int directionY = 1;
    public boolean hasCoin;
    int coinX, coinY;
    final int coinSize = 60;
    public boolean isDestroyed;

    public Tube(int tubeCount, int tubeIdx) {
        random = new Random();
        type = (tubeIdx == 0) ? TYPE_NORMAL : random.nextInt(2);

        generateGapY();
        distanceBetweenTubes = (SCR_WIDTH + width) / (tubeCount - 1);
        x = distanceBetweenTubes * tubeIdx + SCR_WIDTH;

        textureUpperTube = new Texture("tubes/tube_flipped.png");
        textureDownTube = new Texture("tubes/tube.png");
        textureCoin = new Texture("tubes/money.png"); // Твоя монетка

        isPointReceived = false;
        isDestroyed = false;
        hasCoin = tubeIdx != 0 && random.nextFloat() < 0.40f;
    }

    private void generateGapY() {
        gapY = gapHeight / 2 + padding + random.nextInt(SCR_HEIGHT - 2 * (padding + gapHeight / 2));
    }

    public void draw(Batch batch) {
        if (isDestroyed) return;
        batch.draw(textureUpperTube, x, gapY + gapHeight / 2, width, height);
        batch.draw(textureDownTube, x, gapY - gapHeight / 2 - height, width, height);

        if (hasCoin) {
            coinX = x + width / 2 - coinSize / 2;
            coinY = gapY - coinSize / 2;
            batch.draw(textureCoin, coinX, coinY, coinSize, coinSize);
        }
    }

    public void move() {
        x -= speed;
        if (type == TYPE_MOVING) {
            gapY += speedY * directionY;
            int minY = gapHeight / 2 + padding;
            int maxY = SCR_HEIGHT - (padding + gapHeight / 2);
            if (gapY >= maxY) directionY = -1;
            else if (gapY <= minY) directionY = 1;
        }

        if (x < -width) {
            isPointReceived = false;
            isDestroyed = false;
            x = SCR_WIDTH + distanceBetweenTubes;
            generateGapY();
            type = random.nextInt(2);
            directionY = random.nextBoolean() ? 1 : -1;
            hasCoin = random.nextFloat() < 0.40f;
        }
    }

    public boolean checkCoinCollision(Bird bird) {
        if (!hasCoin || isDestroyed) return false;
        if (bird.x + bird.width >= coinX && bird.x <= coinX + coinSize &&
                bird.y + bird.height >= coinY && bird.y <= coinY + coinSize) {
            hasCoin = false;
            return true;
        }
        return false;
    }

    public boolean isHit(Bird bird) {
        if (isDestroyed) return false;
        if (bird.y <= gapY - gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width) return true;
        if (bird.y + bird.height >= gapY + gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width) return true;
        return false;
    }

    public boolean needAddPoint(Bird bird) {
        return !isPointReceived && !isDestroyed && bird.x > x + width;
    }

    public void setPointReceived() {
        isPointReceived = true;
    }

    public void dispose() {
        textureDownTube.dispose();
        textureUpperTube.dispose();
        textureCoin.dispose();
    }
}