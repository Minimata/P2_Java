package com.mygdx.game.desktop.states;

import static com.mygdx.game.desktop.handlers.B2DVars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.desktop.handlers.B2DVars;
import com.mygdx.game.desktop.handlers.GameStateManager;
import com.mygdx.game.desktop.handlers.MyContactListener;
import com.mygdx.game.desktop.handlers.MyInput;
import com.mygdx.game.desktop.main.Game;

/**
 * Created by alexandre on 09.05.2016.
 */
public class Play extends GameState{

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dcam;

    private Body playerBody;
    private MyContactListener cl;

    public Play(GameStateManager gsm){

        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer();

        //create Platform
        BodyDef bdef = new BodyDef();
        bdef.position.set(160 / PPM, 120 / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 5 / PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        fdef.filter.categoryBits = B2DVars.BIT_GROUND;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setUserData("ground");

        //create Player
        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(5 / PPM, 5 / PPM);
        fdef.shape = shape;
        fdef.restitution = 0.0f;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        playerBody.createFixture(fdef).setUserData("player");

        //create foot sensor
        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -5 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");

        //set up box2d cam
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
    }

    public void handleInput(){
        //player jump
        if(MyInput.isPressed(MyInput.BUTTON_JUMP)) {
            if(cl.isPlayerOnGround()) jumpUp(200);
            else if (cl.canPlayerDoubleJump()) {
                jumpUp(100);
                cl.setDoubleJump(false);
            }
        }

        //player straf
        if(MyInput.isDown(MyInput.BUTTON_RIGHT)) moveOnSide(true, 1);
        if(MyInput.isDown(MyInput.BUTTON_LEFT)) moveOnSide(false, 1);
    }

    public void jumpUp(int force) {
        playerBody.applyForceToCenter(0, force, true);
    }

    public void moveOnSide(boolean moveRight, int velocity) {
        if(moveRight) playerBody.setLinearVelocity(velocity, playerBody.getLinearVelocity().y);
        else playerBody.setLinearVelocity(-velocity, playerBody.getLinearVelocity().y);
    }

    public void update(float dt){
        handleInput();

        world.step(dt, 6, 2);
    }
    public void render(){
        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw world
        b2dr.render(world, b2dcam.combined);
    }
    public void dispose(){

    }

}
