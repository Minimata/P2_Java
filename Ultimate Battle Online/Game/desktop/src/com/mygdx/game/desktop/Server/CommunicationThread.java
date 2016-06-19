package com.mygdx.game.desktop.Server;

import java.io.ObjectInputStream;
import java.net.Socket;
import com.mygdx.game.desktop.CommunicationPaquet.Paquet;

public class CommunicationThread extends Thread{

    private Socket socket;
    private Room myRoom;

    public CommunicationThread(Socket s, Room r){
        socket = s;
        myRoom = r;
    }

    public void run(){
        ObjectInputStream ois;
        //ObjectOutputStream oos;
        boolean running = true;
        System.out.println("client dans la salle");
        try{
            //ois = new ObjectInputStream(socket.getInputStream());
            while(running){
                //..... envoi et réception des données

                //oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                Paquet paquet = (Paquet) ois.readObject();
                myRoom.setPaquet(paquet);
                System.out.println(paquet.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
