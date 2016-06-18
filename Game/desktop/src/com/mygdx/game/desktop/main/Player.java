package com.mygdx.game.desktop.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.desktop.handlers.*;

import static com.mygdx.game.desktop.handlers.Utils.PPM;

/**
 * Created by alexandre on 23.05.2016.
 * this class implements everything related to any player of the game.
 */
public class Player {

    //PHYSICS
    private PolygonShape shape;
    private FixtureDef fdef;
    private Body playerBody;

    //LOGIC
    private int timerHit;
    private boolean isShootingDown;
    private int playerNumber;

    //RENDER
    private String currentImage;
    private Content res;

    //NETWORK
    private Network network;

    /**
     * Player Constructor
     * @param world the world in which the player is added, so it can add itself to it.
     * @param playerNum the ID of the player, going from 0 to Utils.MAX_NUMBER_PLAYER (as written in Play.java).
     */
    public Player(World world, int playerNum) {

        //NETWORK
        network = new Network();

        //LOGIC
        playerNumber = playerNum;

        //PHYSICS
        BodyDef bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();

        bdef.position.set(Utils.V_WIDTH / (2 * PPM), 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(Utils.PLAYER_SIZE / PPM, Utils.PLAYER_SIZE * 2 / PPM);
        fdef.shape = shape;
        fdef.friction = Utils.PLAYER_FRICTION;
        fdef.filter.categoryBits = Utils.BIT_PLAYER;
        fdef.filter.maskBits = Utils.BIT_GROUND;
        playerBody.createFixture(fdef).setUserData("" + playerNumber + "-player");

        //create foot sensor
        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -Utils.PLAYER_SIZE * 2 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = Utils.BIT_PLAYER;
        fdef.filter.maskBits = Utils.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("" + playerNumber + "-foot");

        createOnHitBox(4 * Utils.PLAYER_SIZE, 0, ("" + playerNumber + "-onhit_R"));
        createOnHitBox(-4 * Utils.PLAYER_SIZE, 0, ("" + playerNumber + "-onhit_L"));
        createOnHitBox(0, 4 * Utils.PLAYER_SIZE, ("" + playerNumber + "-onhit_U"));

        //RENDERING
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

    /**
     * Creates a hitbox used to detect weather or not something collided with the player hit.
     * @param x x position relative to the player.
     * @param y y position relative to the player.
     * @param userData name of the user data, useful in collision detection.
     */
    private void createOnHitBox(int x, int y, String userData) {
        //create on hit damage box sensors
        shape.setAsBox(2 * Utils.PLAYER_SIZE / PPM, 2 * Utils.PLAYER_SIZE / PPM,
                new Vector2(x / PPM, y / PPM), 0);
        fdef.shape = shape;
        fdef.filter.maskBits = Utils.BIT_PLAYER;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData(userData);
    }

    /**
     * Used to jump, making the player launch in the air.
     * @param force the force applied to the player. To much and you'll be a spaceship =D
     */
    public void jumpUp(int force) {
        playerBody.applyForceToCenter(0, force, true);
    }

    /**
     * Used to move the player aside.
     * @param moveRight if the player should move right (if true) or left (if false).
     * @param acceleration the acceleration given to the player during movement.
     *                     Using absolute speed gives a harsh Game Feel while playing.
     */
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

    /**
     * Make the player violently shoot down when his flying into mid-air.
     * @param force the force with which the player will be projected downward. Too much and you'll fall trough the ground =/
     */
    public void shootDown(int force) {
        isShootingDown = true;
        playerBody.applyForceToCenter(0, -force, true);
        currentImage = "DOWN";
    }

    /**
     * Should be called each frame, during player update.
     * Prevents players from spamming commands and lets us have smooth animations.
     */
    public void decreaseHitTimer() {
        timerHit--;
        if (timerHit <= 0) {
            for (Fixture f : playerBody.getFixtureList()) {
                if (f.getUserData() != null) {
                    if (f.getUserData() == ("" + playerNumber + "-onhit_UP"))
                        f.setUserData("" + playerNumber + "-onhit_U");
                    if (f.getUserData() == ("" + playerNumber + "-onhit_RIGHT"))
                        f.setUserData("" + playerNumber + "-onhit_R");
                    if (f.getUserData() == ("" + playerNumber + "-onhit_LEFT"))
                        f.setUserData("" + playerNumber + "-onhit_L");
                }
            }
        }
    }

    /**
     * Called when the "HIT" button is pressed.
     * Changes the hitboxes so it detects and applies collisions.
     * @param directionX the direction in which the hitbox should activate.
     *                   1 is right, -1 is left, 0 is up (think of the x axis).
     */
    public void hit(int directionX) {
        if (timerHit <= 0) {
            timerHit = Utils.PLAYER_TIMER_HIT;

            if (directionX >= 1) {
                for (Fixture f : playerBody.getFixtureList()) {
                    if (f.getUserData() != null && f.getUserData() == ("" + playerNumber + "-onhit_R")) {
                        f.setUserData("" + playerNumber + "-onhit_RIGHT");
                        currentImage = "HIT1";
                    }
                }
            } else if (directionX <= -1) {
                for (Fixture f : playerBody.getFixtureList()) {
                    if (f.getUserData() != null && f.getUserData() == ("" + playerNumber + "-onhit_L")) {
                        f.setUserData("" + playerNumber + "-onhit_LEFT");
                        currentImage = "HIT1_FLIP";
                    }
                }
            } else {
                for (Fixture f : playerBody.getFixtureList()) {
                    if (f.getUserData() != null && f.getUserData() == ("" + playerNumber + "-onhit_U")) {
                        f.setUserData("" + playerNumber + "-onhit_UP");
                        if (playerBody.getLinearVelocity().x > 0) currentImage = "HITUP1";
                        else currentImage = "HITUP1_FLIP";

                    }
                }
            }
        }
    }

    /**
     * This function sends to the server the codes corresponding to the actions the user has made using the player's network attribute.
     * It also sends its position so the clients can keep track of each other.
     */
    public void handleInput() {
        //player jump
        if (MyInput.isPressed(MyInput.BUTTON_JUMP)) {
            network.send(playerNumber, Utils.JUMP_CODE, playerBody.getPosition());
        }

        //player shoot down
        if (MyInput.isPressed(MyInput.BUTTON_SHOOT_DOWN)) {
            network.send(playerNumber, Utils.SHOOT_DOWN_CODE, playerBody.getPosition());
        }

        //player straf
        if (MyInput.isDown(MyInput.BUTTON_RIGHT)) {
            network.send(playerNumber, Utils.RIGHT_CODE, playerBody.getPosition());
        }
        if (MyInput.isDown(MyInput.BUTTON_LEFT)) {
            network.send(playerNumber, Utils.LEFT_CODE, playerBody.getPosition());
        }

        //player hit
        if (MyInput.isPressed(MyInput.BUTTON_HIT)) {
            network.send(playerNumber, Utils.HIT_CODE, playerBody.getPosition());
        }
    }

    /**
     * Updates the logical and rendering state of the player.
     * @param cl the MyContactListener, letting the player know if it can jump or if it gets hit, for example.
     */
    public void update(MyContactListener cl) {
        decreaseHitTimer();
        handleInput();
        if (timerHit <= 0) {
            if (cl.isPlayerOnGround(playerNumber)) {
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

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Texture getCurrentImage() {
        return res.getTexture(currentImage);
    }

    public Vector2 pos() {
        return playerBody.getPosition();
    }

    public void setPos(Vector2 pos) {
        this.playerBody.setTransform(pos, 0);
    }
}
