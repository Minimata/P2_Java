import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by steve.nadalin on 11/04/2016.
 */
public class Room extends Thread{

    private final int nbPlayers = 8;
    ServerSocket sSocket;

    public void run(){
        int i = 0;
        try {
            sSocket = new ServerSocket(Server.port);
            while(i<nbPlayers) {
                Socket socket = sSocket.accept();
                Thread t = new CommunicationThread(socket);
                t.start();
                i++;
            }
            System.out.println("Room started!");
            Server.port++;
            Room r = new Room();
            r.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}