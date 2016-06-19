package com.mygdx.game.desktop.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import com.mygdx.game.desktop.CommunicationPaquet.Paquet;

/**
 * Created by steve.nadalin on 11/04/2016.
 */
public class Room extends Thread{

    private Collection<Socket> outSocket = new LinkedList<>();
    protected LinkedList<Paquet> toSend = new LinkedList<>();
    public static ServerSocket sSocket;
    private int port;
    private ObjectOutputStream oos;

    public Room(int port){
        this.port = port;
    }

    /**
     * Send every paquet of the list to all players
     */
    private void sendPaquet(){
        if(toSend.size()!=0){
            Paquet paquetToSend = toSend.getFirst();
            toSend.removeFirst();
            System.out.println("envoi depuis la room: "+paquetToSend.toString());
            for(Socket socket : outSocket){
                try{
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(paquetToSend);
                    oos.flush();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets the paquet to send to others players
     * @param p paquet
     */
    public synchronized void setPaquet(Paquet p){
        this.toSend.add(p);
    }

    /**
     * Connect players qith et communication thread and then
     * sends every paquet received
     */
    public void run(){
        String out = "connecté à la salle";
        int i = 0;
        try {
            System.out.println("Server.Room started!");
            sSocket = new ServerSocket(port);
            while(true) {
                try {
                    if(i<Server.nbPlayers){
                        Socket socket = sSocket.accept();
                        outSocket.add(socket);
                        Thread t = new CommunicationThread(socket,this);
                        t.start();
                        i++;
                    }else{
                        sendPaquet();
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}