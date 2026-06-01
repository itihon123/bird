package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.PointCounter;
import ru.samsung.gamestudio.components.TextButton;

public class ScreenRestart implements Screen {
    MyGdxGame myGdxGame;
    MovingBackground background;
    PointCounter pointCounter;
    TextButton buttonRestart;
    TextButton buttonMenu;

    public int gamePoints;
    int highScore;
    BitmapFont highScoreFont;
    Preferences prefs;

    public ScreenRestart(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        pointCounter = new PointCounter(750, 530);
        buttonRestart = new TextButton(100, 400, "Restart");
        buttonMenu = new TextButton(100, 150, "Menu");
        background = new MovingBackground("backgrounds/restart_bg.png");

        prefs = Gdx.app.getPreferences("FlappyBirdSettings");
        highScoreFont = new BitmapFont();
        highScoreFont.getData().setScale(4f);
        highScoreFont.setColor(Color.GOLD);
    }

    @Override
    public void show() {
        highScore = prefs.getInteger("highscore", 0);
        if (gamePoints > highScore) {
            highScore = gamePoints;
            prefs.putInteger("highscore", highScore);
            prefs.flush();
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (buttonRestart.isHit((int) touch.x, (int) touch.y)) myGdxGame.setScreen(myGdxGame.screenGame);
            if (buttonMenu.isHit((int) touch.x, (int) touch.y)) myGdxGame.setScreen(myGdxGame.screenMenu);
        }

        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        buttonMenu.draw(myGdxGame.batch);
        buttonRestart.draw(myGdxGame.batch);
        pointCounter.draw(myGdxGame.batch, gamePoints);
        highScoreFont.draw(myGdxGame.batch, "Best: " + highScore, 750, 430);

        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        buttonRestart.dispose();
        buttonMenu.dispose();
        pointCounter.dispose();
        highScoreFont.dispose();
    }
}
