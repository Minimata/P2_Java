package CommunicationPaquet;

import java.io.Serializable;

/**
 * Created by nicolas on 12.06.16.
 */
public class Paquet implements Serializable{
    private short persoID;
    private short actionID;
    private int posX;
    private int posY;
    static  final long serialVersionUID = 1234567899L;

    public Paquet(){
        persoID = 1;
        actionID = 2;
        posX = 2;
        posY = 1;
    }

    public short getPersoID() {
        return persoID;
    }

    public void setPersoID(short persoID) {
        if(persoID >= 0) {
            this.persoID = persoID;
        }
        //todo else exception
    }

    public short getActionID() {
        return actionID;
    }

    public void setActionID(short actionID) {
        if(actionID >= 0) {
            this.actionID = actionID;
        }
        //todo else exception
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String toString(){
        return "PersoID: " + persoID + " ActionID: " + actionID + " posX: " + posX + " posY: " + posY;
    }
}
