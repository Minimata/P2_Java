package com.mygdx.game.desktop.handlers;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by alexandre on 17.05.2016.
 */
public class MyContactListener implements ContactListener {

    private boolean isOnGround;
    private boolean canDoubleJump;

    //called when 2 fixtures startt to collide together
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        touchesGround(fa);
        touchesGround(fb);
    }

    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        leavesGround(fa);
        leavesGround(fb);
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
