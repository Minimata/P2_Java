package com.mygdx.game.desktop.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.desktop.handlers.Utils;
import com.mygdx.game.desktop.handlers.GameStateManager;
import com.mygdx.game.desktop.handlers.MyInput;
import com.mygdx.game.desktop.handlers.MyInputProcessor;

/**
 * Created by alexandre on 09.05.2016.
 */
public class Game implements ApplicationListener{

    public static final String TITLE = "Game Test";
    private static final float STEP = 1 / 60f;
    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;

    public void create() {

        //Texture.setEnforcePotImages(false);

        Gdx.input.setInputProcessor(new MyInputProcessor());

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Utils.V_WIDTH, Utils.V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, Utils.V_WIDTH, Utils.V_HEIGHT);

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
