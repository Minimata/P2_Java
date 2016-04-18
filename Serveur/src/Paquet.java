import java.io.Serializable;

/**
 * Created by steve.nadalin on 18/04/2016.
 */
public class Paquet implements Serializable{

    private short persoID;
    private short actionID;
    private int posX;
    private int posY;

    public Paquet(){
        //test
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
}
