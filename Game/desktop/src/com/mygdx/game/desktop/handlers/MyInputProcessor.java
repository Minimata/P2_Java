package com.mygdx.game.desktop.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by alexandre on 23.05.2016.
 */
public class MyInputProcessor extends InputAdapter{

    public boolean keyDown(int k) {
        if(k == Input.Keys.W) {
            MyInput.setKey(MyInput.BUTTON_JUMP, true);
        }
        if(k == Input.Keys.D) {
            MyInput.setKey(MyInput.BUTTON_RIGHT, true);
        }
        if(k == Input.Keys.A) {
            MyInput.setKey(MyInput.BUTTON_LEFT, true);
        }
        if(k == Input.Keys.S) {
            MyInput.setKey(MyInput.BUTTON_SHOOT_DOWN, true);
        }
        if(k == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.BUTTON_HIT, true);
        }
        return true;
    }

    public boolean keyUp(int k) {
        if(k == Input.Keys.W) {
            MyInput.setKey(MyInput.BUTTON_JUMP, false);
        }
        if(k == Input.Keys.D) {
            MyInput.setKey(MyInput.BUTTON_RIGHT, false);
        }
        if(k == Input.Keys.A) {
            MyInput.setKey(MyInput.BUTTON_LEFT, false);
        }
        if(k == Input.Keys.S) {
            MyInput.setKey(MyInput.BUTTON_SHOOT_DOWN, false);
        }
        if(k == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.BUTTON_HIT, false);
        }
        return true;
    }

}
