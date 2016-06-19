package com.mygdx.game.desktop.handlers;

import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

/**
 * Created by alexandre on 17.05.2016.
 * This class keeps track of all player contacts in the scene.
 */
public class MyContactListener implements ContactListener {

    private boolean[] playerOnGround;
    private boolean[] playerCanDoubleJump;

    public MyContactListener() {
        playerOnGround = new boolean[Utils.MAX_NUMBER_PLAYERS];
        playerCanDoubleJump = new boolean[Utils.MAX_NUMBER_PLAYERS];

        for(int i = 0; i < Utils.MAX_NUMBER_PLAYERS; i++) {
            playerCanDoubleJump[i] = true;
            playerOnGround[i] = false;
        }
    }

    /**
     * called when to fixtures collide together.
     * @param c
     */
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        touchesGround(fa);
        touchesGround(fb);

        touchesPlayer(fa, fb);
        touchesPlayer(fb, fa);
    }

    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        leavesGround(fa);
        leavesGround(fb);
    }

    /**
     * checks if 2 fixtures colliding are actually one player hitting another, and applying force to the one getting hit.
     * @param f fixture of the player hitting
     * @param target fixture of the target of the assault.
     */
    private void touchesPlayer(Fixture f, Fixture target) {
        if (f.getUserData() != null) {
            String userData = f.getUserData().toString().split("-")[1];
            if(userData.equals("onhit_UP")){
                target.getBody().applyForceToCenter(0, Utils.PLAYER_KNOCK_BACK_FORCE, true);
            }
            else if (userData.equals("onhit_RIGHT")){
                target.getBody().applyForceToCenter(Utils.PLAYER_KNOCK_BACK_FORCE, 0, true);
            }
            else if (userData.equals("onhit_LEFT")){
                target.getBody().applyForceToCenter(-Utils.PLAYER_KNOCK_BACK_FORCE, 0, true);
            }
        }
    }

    /**
     * check if the player making the collision touches the ground.
     * @param f fixture creating the collision.
     */
    private void touchesGround(Fixture f) {
        if (f.getUserData() != null) {
            String[] data = f.getUserData().toString().split("-");
            int playerNum = Integer.parseInt(data[0]);
            String userData = data[1];
            if(userData.equals("foot")){
                playerCanDoubleJump[playerNum] = true;
                playerOnGround[playerNum] = true;
            }
        }
    }

    /**
     * checks if a fixture is a player leaving ground.
     * @param f the fixture creating the collision.
     */
    private void leavesGround(Fixture f) {
        if (f.getUserData() != null) {
            String[] data = f.getUserData().toString().split("-");
            int playerNum = Integer.parseInt(data[0]);
            String userData = data[1];
            if(userData.equals("foot")){
                playerOnGround[playerNum] = false;
            }
        }
    }

    public boolean isPlayerOnGround(int playerNumber){
        return playerOnGround[playerNumber];
    }
    public boolean canPlayerDoubleJump(int playerNumber) {
        return playerCanDoubleJump[playerNumber];
    }
    public void setDoubleJump(int playerNumber, boolean in) {
        playerCanDoubleJump[playerNumber] = in;
    }

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
