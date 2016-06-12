package Server;

import java.io.ObjectInputStream;
import java.net.Socket;
import CommunicationPaquet.Paquet;

public class CommunicationThread extends Thread{

    private Socket socket;
    private Paquet paquet;
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
            ois = new ObjectInputStream(socket.getInputStream());
            while(running){
                //..... envoi et réception des données

                //oos = new ObjectOutputStream(socket.getOutputStream());
                paquet = (Paquet) ois.readObject();
                myRoom.setPaquet(paquet);
                System.out.println(paquet.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
