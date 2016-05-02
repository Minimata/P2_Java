import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by steve.nadalin on 18/04/2016.
 */
public class Server {

    public static int port;

     public static void main(String[] args) throws Exception{
         String out = "Connection à la salle en cours";
         String in;
         port = 9090;
         System.out.println("Server started!");
         try {//redirection des connections
             ServerSocket serverSocket = new ServerSocket(port);
             Socket s = serverSocket.accept();
             DataInputStream is = new DataInputStream(s.getInputStream());
             DataOutputStream os = new DataOutputStream(s.getOutputStream());
             in = is.readUTF();
             System.out.println(in);
             os.writeUTF(out);
             os.flush();
             Room firstRoom = new Room();
             firstRoom.start();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}
