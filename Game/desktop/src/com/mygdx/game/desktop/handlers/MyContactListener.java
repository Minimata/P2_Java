package com.mygdx.game.desktop.handlers;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by alexandre on 17.05.2016.
 */
public class MyContactListener implements ContactListener {

    private boolean isOnGround;
    private boolean canDoubleJump;

    //called when 2 fixtures start to collide together
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

    private void touchesPlayer(Fixture f, Fixture target) {
        if (f.getUserData() != null) {
            if(f.getUserData().equals("onhit_UP")){
                target.getBody().applyForceToCenter(0, Utils.PLAYER_KNOCK_BACK_FORCE, true);
                System.out.println("Force applied to UP");
            }
            else if (f.getUserData().equals("onhit_RIGHT")){
                target.getBody().applyForceToCenter(Utils.PLAYER_KNOCK_BACK_FORCE, 0, true);
                System.out.println("Force applied to RIGHT");
            }
            else if (f.getUserData().equals("onhit_LEFT")){
                target.getBody().applyForceToCenter(-Utils.PLAYER_KNOCK_BACK_FORCE, 0, true);
                System.out.println("Force applied to LEFT");
            }
        }
    }

    private void touchesGround(Fixture f) {
        if (f.getUserData() != null && f.getUserData().equals("foot")){
            isOnGround = true;
            canDoubleJump = true;
        }
    }

    private void leavesGround(Fixture f) {
        if (f.getUserData() != null && f.getUserData().equals("foot")){
            isOnGround = false;
        }
    }

    public boolean isPlayerOnGround(){
        return isOnGround;
    }
    public boolean canPlayerDoubleJump() {
        return canDoubleJump;
    }
    public void setDoubleJump(boolean in) {
        canDoubleJump = in;
    }

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
