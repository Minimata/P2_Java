package com.mygdx.game.desktop.states;

import static com.mygdx.game.desktop.handlers.Utils.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.desktop.handlers.*;
import com.mygdx.game.desktop.main.Player;
import com.mygdx.game.desktop.CommunicationPaquet.*;
import java.util.ArrayList;

/**
 * Created by alexandre on 09.05.2016.
 * This class implements the GameState during which the game really happens, with fighting and all.
 * This is currently the only state implemented.
 */
public class Play extends GameState {

    //NETWORK
    private Network network;

    //WORLD
    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;

    //PLAYERS
    private ArrayList<Player> players;
    private MyContactListener cl;

    //LEVEL
    private TiledMap tileMap;
    private OrthoCachedTiledMapRenderer tmr;
    private float tileSize;
    private float offset;
    private Texture backTex;
    private Sprite backSprite;

    /**
     * GameState Play constructor.
     * @param gsm useful for changing GameStates (menu, pause or play states for exemple).
     *            Here, Play is the only GameState at this stage.
     */
    public Play(GameStateManager gsm) {

        super(gsm);

        //NETWORK
        network = new Network();

        //WORLD
        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        //LEVEL
        tileMap = new TmxMapLoader().load("res/maps/Arena.tmx");
        tmr = new OrthoCachedTiledMapRenderer(tileMap);
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Platforms");
        tileSize = layer.getTileWidth(); //square
        offset = tileSize / 2;
        backTex = new Texture("res/images/background3.png");
        backSprite = new Sprite(backTex);

        levelCreation();

        //PLAYERS
        players = new ArrayList<Player>(Utils.MAX_NUMBER_PLAYERS);
        for(int i = 0; i < Utils.MAX_NUMBER_PLAYERS; i++) {
            players.add(i, new Player(world, i));
        }

        //CAMERA
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Utils.V_WIDTH / PPM, Utils.V_HEIGHT / PPM);
    }

    /**
     * Creates level.
     */
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

    /**
     * Create a platform with given height and width at given position.
     * @param x position on the x axis
     * @param y position on the y axis
     * @param w width of the platform
     * @param h height of the platform
     */
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

    /**
     * This function will go trough all packages received since last call and will feed it to every player input.
     */
    private void handleAllInputs() {
        for (Paquet paquet : network.getPaquetsReceived()) {
            for(Player player : players) {
                handleInput(player, paquet);
            }
        }
        network.emptyPaquets();
    }

    /**
     * This function feeds the package to the player so it can plays the inputs correctly, depending on what the server is sending.
     * @param player the player reading the package.
     * @param paquet the package containing the input infos.
     */
    public void handleInput(Player player, Paquet paquet) {
        //sets player position in the scene.
        player.setPos(paquet.getPos());


        if (paquet.playerJump(player.getPlayerNumber())) {
            if (cl.isPlayerOnGround(player.getPlayerNumber())) player.jumpUp(Utils.PLAYER_JUMP_FORCE);
            else if (cl.canPlayerDoubleJump(player.getPlayerNumber())) {
                player.jumpUp(Utils.PLAYER_JUMP_FORCE_DOUBLE_JUMP);
                cl.setDoubleJump(player.getPlayerNumber(), false);
            }
        }

        //player shoot down
        if(paquet.playerShootDown(player.getPlayerNumber())) {
            if(!(cl.isPlayerOnGround(player.getPlayerNumber()))) player.shootDown(Utils.PLAYER_SHOOT_DOWN_FORCE);
        }

        //player straf
        if (paquet.playerRight(player.getPlayerNumber())) player.moveOnSide(true, Utils.PLAYER_ACCELERATION);
        if (paquet.playerLeft(player.getPlayerNumber())) player.moveOnSide(false, Utils.PLAYER_ACCELERATION);

        //player hit
        if(paquet.playerHit(player.getPlayerNumber())) {
            if(paquet.playerLeft(player.getPlayerNumber())) {
                player.hit(-1);
            }
            else if (paquet.playerRight(player.getPlayerNumber())) {
                player.hit(1);
            }
            else player.hit(0);
        }
    }

    /**
     * Function called every frame for the libgdx GameLoop.
     * This will make the world "move" int ime and will handle inputs as well as calling the update of its moving children (meaning the players).
     * @param dt delta time since last call.
     */
    public void update(float dt) {
        world.step(dt, 6, 2);
        handleAllInputs();
        for(Player player : players) {
            player.update(cl);
        }
    }

    /**
     * This function will display the textures and sprite on the window.
     */
    public void render() {
        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        sb.draw(backSprite, 0, 0, Utils.V_WIDTH, Utils.V_HEIGHT);
        sb.end();

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw world
        b2dr.render(world, b2dcam.combined);

        sb.begin();

        for(Player player : players) {
            sb.draw((player.getCurrentImage()), (player.pos().x) * Utils.PPM - Utils.PLAYER_SIZE - 5,
                    (player.pos().y) * Utils.PPM - 2*Utils.PLAYER_SIZE - 3,
                    player.getCurrentImage().getWidth() / 1.5f,
                    player.getCurrentImage().getHeight() / 1.5f);
        }
        sb.end();
    }

    /**
     * Used to destroy the scene and window and everything. Not implemented here yet.
     */
    public void dispose() {

    }

}
