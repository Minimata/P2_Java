import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by steve.nadalin on 11/04/2016.
 */
public class Room extends Thread{

    public static ServerSocket sSocket;
    private int port;


    public Room(int port){
        this.port = port;
    }

    public void run(){
        String out = "connecté à la salle";
        int i = 0;
        //Room r = new Room();
        //r.start();
        try {
            System.out.println("Room started!");
            sSocket = new ServerSocket(port);
            while(true) {
                try {
                    //System.out.println("passe");
                    Socket socket = sSocket.accept();
                    DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                    System.out.println("test" + socket.getLocalPort());
                    os.writeUTF(out);
                    os.flush();
                    Thread t = new CommunicationThread(socket);
                    t.start();
                    i++;

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