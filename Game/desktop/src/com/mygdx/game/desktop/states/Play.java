package com.mygdx.game.desktop.states;

import static com.mygdx.game.desktop.handlers.Utils.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.desktop.handlers.Utils;
import com.mygdx.game.desktop.handlers.GameStateManager;
import com.mygdx.game.desktop.handlers.MyContactListener;
import com.mygdx.game.desktop.handlers.MyInput;
import com.mygdx.game.desktop.main.Player;

/**
 * Created by alexandre on 09.05.2016.
 */
public class Play extends GameState{

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dcam;

    private MyContactListener cl;

    private Player player;

    public Play(GameStateManager gsm){

        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer();

        //create World walls
        createPlatform(Utils.V_WIDTH / 2, 5, Utils.V_WIDTH / Utils.SCALE, 5);
        createPlatform(Utils.V_WIDTH / 2, Utils.V_HEIGHT - 5, Utils.V_WIDTH / Utils.SCALE, 5);
        createPlatform(5, Utils.V_HEIGHT / 2, 5, Utils.V_HEIGHT / Utils.SCALE);
        createPlatform(Utils.V_WIDTH - 5, Utils.V_HEIGHT / 2, 5, Utils.V_HEIGHT / Utils.SCALE);

        //create platforms
        createPlatform(Utils.V_WIDTH / 2, Utils.V_HEIGHT / 2, 60, 5);
        createPlatform(30, Utils.V_HEIGHT / 4, 20, 5);
        createPlatform(Utils.V_WIDTH - 30, Utils.V_HEIGHT / 4, 20, 5);

        //create Player
        player = new Player(world);

        //set up box2d cam
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Utils.V_WIDTH / PPM, Utils.V_HEIGHT / PPM);
    }

    private void createPlatform(int posX, int posY, int width, int height) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(posX / PPM, posY / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / PPM, height / PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        fdef.filter.categoryBits = Utils.BIT_GROUND;
        fdef.filter.maskBits = Utils.BIT_PLAYER;
        body.createFixture(fdef).setUserData("ground");
    }

    public void handleInput(){
        //player jump
        if(MyInput.isPressed(MyInput.BUTTON_JUMP)) {
            if(cl.isPlayerOnGround()) player.jumpUp(Utils.PLAYER_JUMP_FORCE);
            else if (cl.canPlayerDoubleJump()) {
                player.jumpUp(Utils.PLAYER_JUMP_FORCE / 2);
                cl.setDoubleJump(false);
            }
        }

        //player straf
        if(MyInput.isDown(MyInput.BUTTON_RIGHT)) player.moveOnSide(true, Utils.PLAYER_ACCELERATION);
        if(MyInput.isDown(MyInput.BUTTON_LEFT)) player.moveOnSide(false, Utils.PLAYER_ACCELERATION);
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
