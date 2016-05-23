import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created by steve.nadalin on 18/04/2016.
 */
public class Server {

    public static int port;
    public static int portBase;
    private final static int nbPlayers = 8;

     public static void main(String[] args) throws Exception{
         int i=0;
         String ip = InetAddress.getLocalHost().getHostAddress();
         System.out.println(ip);
         //String out = "Connection à la salle en cours";
         //String in;
         port = 9090;
         portBase = 9090;
         System.out.println("Server started!");
         try(ServerSocket serverSocket = new ServerSocket(portBase)){
         while(true) {
             try {//redirection des connection

                 Socket s = serverSocket.accept();
                 System.out.println("un client est là");
                 //DataInputStream is = new DataInputStream(s.getInputStream());
                 DataOutputStream os = new DataOutputStream(s.getOutputStream());
                 //while(i<nbPlayers){
                 //in = is.readUTF();
                 //System.out.println(in);
                 //os.writeUTF(out);
                 //os.flush();
                 if (i % nbPlayers == 0) {
                     i = 0;
                     port++;
                     Room newRoom = new Room(port);
                     newRoom.start();
                     //System.out.println("coucou");
                     sleep(500);
                 }
                 System.out.println(port);
                 os.writeInt(port);
                 os.flush();
                 i++;

                 // }
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         }
         catch (IOException e){
             e.printStackTrace();
         }
     }
}
