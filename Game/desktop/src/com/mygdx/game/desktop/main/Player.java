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

    public Player(World world) {
        bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();

        bdef.position.set(Utils.V_WIDTH / (2*PPM), 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(Utils.PLAYER_SIZE / PPM, Utils.PLAYER_SIZE / PPM);
        fdef.shape = shape;
        //fdef.restitution = Utils.PLAYER_RESTITUTION;
        fdef.friction = Utils.PLAYER_FRICTION;
        //fdef.density = Utils.PLAYER_DENSITY;
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
    }

    public void jumpUp(int force) {
        playerBody.applyForceToCenter(0, force, true);
        System.out.println(force);
    }

    public void moveOnSide(boolean moveRight, float acceleration) {
        if(moveRight) playerBody.setLinearVelocity(playerBody.getLinearVelocity().x + acceleration, playerBody.getLinearVelocity().y);
        else playerBody.setLinearVelocity(playerBody.getLinearVelocity().x - acceleration, playerBody.getLinearVelocity().y);

        //Limits the speed of the player, and keeps its direction.
        if(Math.abs(playerBody.getLinearVelocity().x) >= Utils.PLAYER_MAX_SPEED) {
            float v = (playerBody.getLinearVelocity().x / Math.abs(playerBody.getLinearVelocity().x))*Utils.PLAYER_MAX_SPEED;
            playerBody.setLinearVelocity(v, playerBody.getLinearVelocity().y);
        }
    }
}
