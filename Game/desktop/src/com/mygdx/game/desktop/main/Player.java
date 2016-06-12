package com.mygdx.game.desktop.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.desktop.handlers.Content;
import com.mygdx.game.desktop.handlers.MyContactListener;
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
    private String currentImage;
    private boolean isShootingDown;
    private Content res;

    public Player(World world) {
        bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();

        bdef.position.set(Utils.V_WIDTH / (2 * PPM), 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(Utils.PLAYER_SIZE / PPM, Utils.PLAYER_SIZE * 2/ PPM);
        fdef.shape = shape;
        fdef.friction = Utils.PLAYER_FRICTION;
        fdef.filter.categoryBits = Utils.BIT_PLAYER;
        fdef.filter.maskBits = Utils.BIT_GROUND;
        playerBody.createFixture(fdef).setUserData("player");

        //create foot sensor
        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -Utils.PLAYER_SIZE*2 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = Utils.BIT_PLAYER;
        fdef.filter.maskBits = Utils.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");

        createOnHitBox(4 * Utils.PLAYER_SIZE, 0, "onhit_R");
        createOnHitBox(-4 * Utils.PLAYER_SIZE, 0, "onhit_L");
        createOnHitBox(0, 4 * Utils.PLAYER_SIZE, "onhit_U");

        res = new Content();

        res.loadTexture("res/images/player1_IDLE.png", "IDLE");
        res.loadTexture("res/images/player1_DAMAGED1.png", "DAMAGED1");
        res.loadTexture("res/images/player1_DOWN.png", "DOWN");
        res.loadTexture("res/images/player1_HIT1.png", "HIT1");
        res.loadTexture("res/images/player1_JUMP.png", "JUMP");
        res.loadTexture("res/images/player1_MOVE.png", "MOVE");
        res.loadTexture("res/images/player1_HITUP1.png", "HITUP1");
        res.loadTexture("res/images/player1_HIT1_FLIP.png", "HIT1_FLIP");
        res.loadTexture("res/images/player1_JUMP_FLIP.png", "JUMP_FLIP");
        res.loadTexture("res/images/player1_MOVE_FLIP.png", "MOVE_FLIP");
        res.loadTexture("res/images/player1_HITUP1_FLIP.png", "HITUP1_FLIP");
        currentImage = "IDLE";
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
        if (moveRight) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x + acceleration, playerBody.getLinearVelocity().y);
        } else {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x - acceleration, playerBody.getLinearVelocity().y);
        }

        //Limits the speed of the player, and keeps its direction.
        if (Math.abs(playerBody.getLinearVelocity().x) >= Utils.PLAYER_MAX_SPEED) {
            float v = (playerBody.getLinearVelocity().x / Math.abs(playerBody.getLinearVelocity().x)) * Utils.PLAYER_MAX_SPEED;
            playerBody.setLinearVelocity(v, playerBody.getLinearVelocity().y);
        }
    }

    public void shootDown(int force) {
        isShootingDown = true;
        playerBody.applyForceToCenter(0, -force, true);
        currentImage = "DOWN";
    }

    public void decreaseHitTimer() {
        timerHit--;
        if (timerHit <= 0) {
            for (Fixture f : playerBody.getFixtureList()) {
                if (f.getUserData() != null) {
                    if (f.getUserData() == "onhit_UP") f.setUserData("onhit_U");
                    if (f.getUserData() == "onhit_RIGHT") f.setUserData("onhit_R");
                    if (f.getUserData() == "onhit_LEFT") f.setUserData("onhit_L");
                }
            }
        }
    }

    public void hit(int directionX) {
        if (timerHit <= 0) {
            timerHit = Utils.PLAYER_TIMER_HIT;

            if (directionX >= 1) {
                for (Fixture f : playerBody.getFixtureList()) {
                    if (f.getUserData() != null && f.getUserData() == "onhit_R") {
                        f.setUserData("onhit_RIGHT");
                        currentImage = "HIT1";

                    }
                }
            } else if (directionX <= -1) {
                for (Fixture f : playerBody.getFixtureList()) {
                    if (f.getUserData() != null && f.getUserData() == "onhit_L") {
                        f.setUserData("onhit_LEFT");
                        currentImage = "HIT1_FLIP";
                    }
                }
            } else {
                for (Fixture f : playerBody.getFixtureList()) {
                    if (f.getUserData() != null && f.getUserData() == "onhit_U") {
                        f.setUserData("onhit_UP");
                        if (playerBody.getLinearVelocity().x > 0) currentImage = "HITUP1";
                        else currentImage = "HITUP1_FLIP";

                    }
                }
            }
        }
    }

    public void update(MyContactListener cl) {
        decreaseHitTimer();

        if (timerHit <= 0) {
            if (cl.isPlayerOnGround()) {
                isShootingDown = false;
                if (playerBody.getLinearVelocity().x == 0) {
                    currentImage = "IDLE";
                } else if (playerBody.getLinearVelocity().x >= 0) {
                    currentImage = "MOVE";
                } else {
                    currentImage = "MOVE_FLIP";
                }
            } else if (isShootingDown) {
                currentImage = "DOWN";
            } else if (playerBody.getLinearVelocity().x >= 0) {
                currentImage = "JUMP";
            } else {
                currentImage = "JUMP_FLIP";
            }
        }

    }

    public Texture getCurrentImage() {
        return res.getTexture(currentImage);
    }

    public Vector2 pos() {
        return playerBody.getPosition();
    }

}
