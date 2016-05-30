package com.mygdx.game.desktop.handlers;

/**
 * Created by alexandre on 17.05.2016.
 */
public class Utils {
    //Pixels per meter ration
    public static final float PPM = 100f;

    //Category bits
    public static final short BIT_GROUND = 2;
    public static final short BIT_PLAYER = 4;

    //Window properties
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;

    //Player properties
    public static final int PLAYER_SIZE = 4;
    public static final float PLAYER_FRICTION = 0.2f;

    public static final int PLAYER_JUMP_FORCE = 200;
    public static final int PLAYER_JUMP_FORCE_DOUBLE_JUMP = 100;
    public static final int PLAYER_SHOOT_DOWN_FORCE = 200;
    public static final int PLAYER_KNOCK_BACK_FORCE = 100;
    public static final int PLAYER_DAMAGE = 20;
    public static final int PLAYER_TIMER_HIT = 20;

    public static final float PLAYER_ACCELERATION = 0.11f;
    public static final float PLAYER_MAX_SPEED = 2.5f;
}
