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
        String out = "connecté à la salle";
        int i = 0;
        //Room r = new Room();
        //r.start();
        try {
            sSocket = new ServerSocket(Server.port);
            while(i<nbPlayers) {
                Socket socket = sSocket.accept();
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                os.writeUTF(out);
                os.flush();
                Thread t = new CommunicationThread(socket);
                t.start();
                i++;
            }
            System.out.println("Room started!");
            Server.port++;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}