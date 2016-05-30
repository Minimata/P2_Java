package com.mygdx.game.desktop.main;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.desktop.handlers.Utils;

import static com.mygdx.game.desktop.handlers.Utils.PPM;

/**
 * Created by alexandre on 23.05.2016.
 */
public class Player {

    private BodyDef bdef;
    private PolygonShape shape;
    private FixtureDef fdef;
    private Body playerBody;
    private int timerHit;

    public Player(World world) {
        bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();

        bdef.position.set(Utils.V_WIDTH / (2 * PPM), 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(Utils.PLAYER_SIZE / PPM, Utils.PLAYER_SIZE / PPM);
        fdef.shape = shape;
        fdef.friction = Utils.PLAYER_FRICTION;
        fdef.filter.categoryBits = Utils.BIT_PLAYER;
        fdef.filter.maskBits = Utils.BIT_GROUND;
        playerBody.createFixture(fdef).setUserData("player");

        //create foot sensor
        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -Utils.PLAYER_SIZE / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = Utils.BIT_PLAYER;
        fdef.filter.maskBits = Utils.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");

        createOnHitBox(4 * Utils.PLAYER_SIZE, 0, "onhit_R");
        createOnHitBox(-4 * Utils.PLAYER_SIZE, 0, "onhit_L");
        createOnHitBox(0, 4 * Utils.PLAYER_SIZE, "onhit_U");
    }

    private void createOnHitBox(int x, int y, String userData) {
        //create on hit damage box sensors
        shape.setAsBox(2 * Utils.PLAYER_SIZE / PPM, 2 * Utils.PLAYER_SIZE / PPM,
                new Vector2(x / PPM, y / PPM), 0);
        fdef.shape = shape;
        fdef.filter.maskBits = Utils.BIT_PLAYER;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData(userData);
    }

    public void jumpUp(int force) {
        playerBody.applyForceToCenter(0, force, true);
    }

    public void moveOnSide(boolean moveRight, float acceleration) {
        if (moveRight)
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x + acceleration, playerBody.getLinearVelocity().y);
        else
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x - acceleration, playerBody.getLinearVelocity().y);

        //Limits the speed of the player, and keeps its direction.
        if (Math.abs(playerBody.getLinearVelocity().x) >= Utils.PLAYER_MAX_SPEED) {
            float v = (playerBody.getLinearVelocity().x / Math.abs(playerBody.getLinearVelocity().x)) * Utils.PLAYER_MAX_SPEED;
            playerBody.setLinearVelocity(v, playerBody.getLinearVelocity().y);
        }
    }

    public void shootDown(int force) {
        playerBody.applyForceToCenter(0, -force, true);
    }

    public void decreaseHitTimer() {
        timerHit--;
        if(timerHit <= 0) {
            for(Fixture f: playerBody.getFixtureList()) {
                if(f.getUserData() != null){
                    if(f.getUserData() == "onhit_UP") f.setUserData("onhit_U");
                    if(f.getUserData() == "onhit_RIGHT") f.setUserData("onhit_R");
                    if(f.getUserData() == "onhit_LEFT") f.setUserData("onhit_L");
                }
            }
        }
    }

    public void hit(int directionX) {
        if(timerHit <= 0) {
            timerHit = Utils.PLAYER_TIMER_HIT;

            if(directionX >= 1) {
                for(Fixture f: playerBody.getFixtureList()) {
                    if(f.getUserData() != null){
                        if(f.getUserData() == "onhit_R") f.setUserData("onhit_RIGHT");
                    }
                }
            }

            else if(directionX <= -1) {
                for(Fixture f: playerBody.getFixtureList()) {
                    if(f.getUserData() != null){
                        if(f.getUserData() == "onhit_L") f.setUserData("onhit_LEFT");
                    }
                }
            }

            else {
                for(Fixture f: playerBody.getFixtureList()) {
                    if(f.getUserData() != null){
                        if(f.getUserData() == "onhit_U") f.setUserData("onhit_UP");
                    }
                }
            }
        }
    }
}
