package com.mygdx.game.desktop.CommunicationPaquet;

import java.io.Serializable;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.desktop.handlers.Utils;

/**
 * Created by nicolas on 12.06.16.
 * This class implements the serializable objects being sent through the sockets between clients and server.
 */
public class Paquet implements Serializable{
    private int persoID;
    private int actionID;
    private Vector2 pos;
    static final long serialVersionUID = 1234567899L;

    public Paquet(int playerNumber, int actionCode, Vector2 playerPos){
        persoID = playerNumber;
        actionID = actionCode;
        pos = playerPos;
    }

    public boolean playerJump(int playerNumber) {
        return (persoID == playerNumber && actionID == Utils.JUMP_CODE);
    }

    public boolean playerRight(int playerNumber) {
        return (persoID == playerNumber && actionID == Utils.RIGHT_CODE);
    }

    public boolean playerLeft(int playerNumber) {
        return (persoID == playerNumber && actionID == Utils.LEFT_CODE);
    }

    public boolean playerShootDown(int playerNumber) {
        return (persoID == playerNumber && actionID == Utils.SHOOT_DOWN_CODE);
    }

    public boolean playerHit(int playerNumber) {
        return (persoID == playerNumber && actionID == Utils.HIT_CODE);
    }

    public Vector2 getPos() {
        return pos;
    }

    public String toString(){
        return "PersoID: " + persoID + " ActionID: " + actionID + " pos: " + pos;
    }
}
