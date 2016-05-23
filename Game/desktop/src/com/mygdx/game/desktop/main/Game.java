package com.mygdx.game.desktop.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.desktop.handlers.GameStateManager;
import com.mygdx.game.desktop.handlers.MyInput;
import com.mygdx.game.desktop.handlers.MyInputProcessor;

/**
 * Created by alexandre on 09.05.2016.
 */
public class Game implements ApplicationListener{

    public static final String TITLE = "Game Test";
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;

    private static final float STEP = 1 / 60f;
    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;

    public void create() {

        Gdx.input.setInputProcessor(new MyInputProcessor());

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);
    }

    public void render() {
        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
            MyInput.update();
        }
    }

    public void dispose() {

    }

    public void resize(int w, int h) {}
    public void pause() {}
    public void resume() {}

    public SpriteBatch getSb() {
        return sb;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public OrthographicCamera getHudCam() {
        return hudCam;
    }

}
