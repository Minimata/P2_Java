package com.mygdx.game.desktop.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.mygdx.game.desktop.CommunicationPaquet.Paquet;

/**
 * Created by nicolas on 11.04.16.
 * This class listens to all packages coming from the server.
 * It extends the Thread class so it can run indefinitely.
 */
public class ClientListener extends Thread {

    private Socket socket;
    private ReentrantReadWriteLock lock;

    /**
     * CAREFUL : this might be critical resources, and it is taken each frame by a non.threaded class.
     */
    private ArrayList<Paquet> paquetsReceived = new ArrayList<Paquet>();

    /**
     * Constructor of the listener.
     * @param s socket from which the listener will get its packages.
     */
    public ClientListener(Socket s){
        System.out.println("Creation du listener ! ");
        socket = s;
        lock = new ReentrantReadWriteLock();
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
                if(order != null) {
                    System.out.println(order.toString());
                    paquetsReceived.add(order);
                }
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
        try{
            lock.readLock().lock();
            return paquetsReceived;
        }finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Empties the package list when all have been read.
     */
    public void emptyPaquets() {
        try{
            lock.writeLock().lock();
            paquetsReceived.clear();
        }finally {
            lock.writeLock().unlock();
        }

    }
}
