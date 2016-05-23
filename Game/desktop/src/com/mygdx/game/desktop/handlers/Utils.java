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
    public static final int PLAYER_SIZE = 5;
    public static final int PLAYER_DENSITY = 100;
    public static final float PLAYER_RESTITUTION = 0.0f;
    public static final float PLAYER_FRICTION = 0.1f;
    public static final int PLAYER_JUMP_FORCE = 200;
    public static final float PLAYER_ACCELERATION = 0.15f;
    public static final float PLAYER_MAX_SPEED = 3.0f;
}
