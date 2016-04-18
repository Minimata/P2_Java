import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by steve.nadalin on 18/04/2016.
 */
public class Server {

    public static int port;
    private String in;
    private String out;

     public static void main()throws Exception{
         port = 9090;
         System.out.println("Server started!");
         Room firstRoom = new Room();
         firstRoom.start();
         try {//redirection des connections
             ServerSocket serverSocket = new ServerSocket(port);
             Socket s = serverSocket.accept();
             DataInputStream is = new DataInputStream(s.getInputStream());
             DataOutputStream os = new DataOutputStream(s.getOutputStream());
             System.out.println(is.readChar());
             os.write(port);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}
