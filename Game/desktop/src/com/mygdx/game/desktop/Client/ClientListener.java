package com.mygdx.game.desktop.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.mygdx.game.desktop.CommunicationPaquet.Paquet;

/**
 * Created by nicolas on 11.04.16.
 * This class listens to all packages coming from the server.
 * It extends the Thread class so it can run indefinitely.
 */
public class ClientListener extends Thread {

    private Socket socket;

    /**
     * CAREFUL : this might be critical resources, and it is taken each frame by a non.threaded class.
     */
    private ArrayList<Paquet> paquetsReceived;

    /**
     * Constructor of the listener.
     * @param s socket from which the listener will get its packages.
     */
    public ClientListener(Socket s){
        System.out.println("Creation du listener ! ");
        socket = s;
    }

    public void run() {
        listen();
    }

    /**
     * Infinite loop listening to the server packages, adding them to a list of packages.
     */
    public synchronized void listen(){
        while(true){
            try{
                ObjectInputStream scanner = new ObjectInputStream(socket.getInputStream());
                Paquet order = (Paquet) scanner.readObject();
                paquetsReceived.add(order);
                System.out.println(order.toString());
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the packages received from last clear.
     * CAREFUL : might be critical resource.
     * @return the list of packages.
     */
    public ArrayList<Paquet> getPaquetsReceived() {
        return paquetsReceived;
    }

    /**
     * Empties the package list when all have been read.
     */
    public void emptyPaquets() {
        paquetsReceived.clear();
    }
}
