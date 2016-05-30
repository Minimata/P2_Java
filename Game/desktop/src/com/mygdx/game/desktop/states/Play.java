package com.mygdx.game.desktop.states;

import static com.mygdx.game.desktop.handlers.Utils.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
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
public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dcam;

    private MyContactListener cl;

    private Player player;

    private TiledMap tileMap;
    private OrthoCachedTiledMapRenderer tmr;
    private float tileSize;

    private float offset;

    public Play(GameStateManager gsm) {

        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer();

        // Load tile map
        tileMap = new TmxMapLoader().load("res/maps/Arena.tmx");
        tmr = new OrthoCachedTiledMapRenderer(tileMap);

        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Platforms");
        tileSize = layer.getTileWidth(); //square
        offset = tileSize / 2;

        levelCreation();

        //create Player
        player = new Player(world);

        //set up box2d cam
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Utils.V_WIDTH / PPM, Utils.V_HEIGHT / PPM);
    }

    private void levelCreation() {
        //create World walls
        createPlatform(Utils.V_WIDTH / 2, offset, Utils.V_WIDTH, tileSize); //GROUND
        createPlatform(Utils.V_WIDTH / 2, Utils.V_HEIGHT - offset, Utils.V_WIDTH, tileSize); //CEILING
        createPlatform(offset, Utils.V_HEIGHT / 2, tileSize, Utils.V_HEIGHT); //LEFT WALL
        createPlatform(Utils.V_WIDTH - offset, Utils.V_HEIGHT / 2, tileSize, Utils.V_HEIGHT); // RIGHT WALL

        //create platforms
        //Horizontals platforms
        createPlatform(10 * tileSize, 6 * tileSize + offset, 8 * tileSize, tileSize);
        createPlatform(4 * tileSize + offset, 10 * tileSize + offset, 7 * tileSize, tileSize);
        createPlatform(14 * tileSize + offset, 16 * tileSize + offset, 7 * tileSize, tileSize);
        createPlatform(15 * tileSize + offset, 20 * tileSize + offset, 3 * tileSize, tileSize);
        createPlatform(3 * tileSize, 23 * tileSize + offset, 4 * tileSize, tileSize);
        createPlatform(17 * tileSize + offset, 23 * tileSize + offset, 3 * tileSize, tileSize);

        //Vertical platforms
        createPlatform(6 * tileSize + offset, 5 * tileSize, tileSize, 2 * tileSize);
        createPlatform(16 * tileSize + offset, 22 * tileSize, tileSize, 2 * tileSize);
        createPlatform(17 * tileSize + offset, 14 * tileSize + offset, tileSize, 3 * tileSize);

        //Mirror
        //Horizontals platforms
        createPlatform(Utils.V_WIDTH - 10 * tileSize, 6 * tileSize + offset, 8 * tileSize, tileSize);
        createPlatform(Utils.V_WIDTH - tileSize - 4 * tileSize + offset, 10 * tileSize + offset, 7 * tileSize, tileSize);
        createPlatform(Utils.V_WIDTH - tileSize - 14 * tileSize + offset, 16 * tileSize + offset, 7 * tileSize, tileSize);
        createPlatform(Utils.V_WIDTH - tileSize - 15 * tileSize + offset, 20 * tileSize + offset, 3 * tileSize, tileSize);
        createPlatform(Utils.V_WIDTH - 3 * tileSize, 23 * tileSize + offset, 4 * tileSize, tileSize);
        createPlatform(Utils.V_WIDTH - tileSize - 17 * tileSize + offset, 23 * tileSize + offset, 3 * tileSize, tileSize);

        //Vertical platforms
        createPlatform(Utils.V_WIDTH - tileSize - 6 * tileSize + offset, 5 * tileSize, tileSize, 2 * tileSize);
        createPlatform(Utils.V_WIDTH - tileSize - 16 * tileSize + offset, 22 * tileSize, tileSize, 2 * tileSize);
        createPlatform(Utils.V_WIDTH - tileSize - 17 * tileSize + offset, 14 * tileSize + offset, tileSize, 3 * tileSize);
    }

    private void createPlatform(float x, float y, float w, float h) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x / PPM, y / PPM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / PPM / Utils.SCALE, h / PPM / Utils.SCALE);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = Utils.BIT_GROUND;
        fdef.filter.maskBits = Utils.BIT_PLAYER;

        world.createBody(bdef).createFixture(fdef);
    }

    public void handleInput() {
        //player jump
        if (MyInput.isPressed(MyInput.BUTTON_JUMP)) {
            if (cl.isPlayerOnGround()) player.jumpUp(Utils.PLAYER_JUMP_FORCE);
            else if (cl.canPlayerDoubleJump()) {
                player.jumpUp(Utils.PLAYER_JUMP_FORCE / 2);
                cl.setDoubleJump(false);
            }
        }

        //player straf
        if (MyInput.isDown(MyInput.BUTTON_RIGHT)) player.moveOnSide(true, Utils.PLAYER_ACCELERATION);
        if (MyInput.isDown(MyInput.BUTTON_LEFT)) player.moveOnSide(false, Utils.PLAYER_ACCELERATION);
    }

    public void update(float dt) {
        handleInput();

        world.step(dt, 6, 2);
    }

    public void render() {
        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw world
        b2dr.render(world, b2dcam.combined);
    }

    public void dispose() {

    }

}
