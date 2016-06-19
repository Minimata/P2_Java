package com.mygdx.game.desktop.handlers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.desktop.Client.ClientListener;
import com.mygdx.game.desktop.Client.ClientSender;
import com.mygdx.game.desktop.CommunicationPaquet.Paquet;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by alexandre on 18.06.2016.
 * This class implements all the uses of the network for the game, client side only.
 */
public class Network {

    private ClientListener cListener;
    private ClientSender cSender;
    private static String host;
    private static final int portbase = 8080;
    private static int port;

    public Network() {
        setupNetwork();
    }

    /**
     * Setups the network.
     */
    public void setupNetwork() {
        port = 0;
        if(initiateConnection()){
            createNetwork();
        }
    }

    /**
     * this function checks if the connection can be correctly established.
     * @return true if everything's fine, false otherwise. Might get the server exploded from time to time.
     */
    public boolean initiateConnection(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Entrez l'adresse ip du serveur: ");
        host = scan.nextLine();
        try{
            Socket sock = new Socket(host, portbase);
            DataInputStream scanner = new DataInputStream(sock.getInputStream());
            port = scanner.readInt();
            System.out.println(port);
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates the network if everything is running correctly.
     */
    public void createNetwork(){
        try{
            System.out.println("Creation du socket !");
            Socket socket = new Socket(host, port);
            cListener = new ClientListener(socket);
            cListener.start();
            cSender = new ClientSender(socket);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This function create a package to send to the server using the ClientSender.
     * @param playerNumber number of the player sending the message.
     * @param actionCode code of the action that triggered this call.
     * @param playerPos position of the player at the moment of the action.
     */
    public void send(int playerNumber, int actionCode, Vector2 playerPos) {
        Paquet p = new Paquet(playerNumber, actionCode, playerPos);
        cSender.send(p);
    }

    /**
     * Gets all received packages.
     * CAREFUL : this comes from a threaded class, and might be a critical resource.
     * @return a list of packages.
     */
    public ArrayList<Paquet> getPaquetsReceived() {
        return cListener.getPaquetsReceived();
    }

    /**
     * Empties the list of packages, making it clean for new ones to come.
     * CAREFUL : this comes from a threaded class, and might be a critical resource.
     */
    public void emptyPaquets() {
        cListener.emptyPaquets();
    }
}
