package Server;

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
    public final static int nbPlayers = 2;

     public static void main(String[] args) throws Exception{

         int i=0;
         String ip = InetAddress.getLocalHost().getHostAddress();
         System.out.println(ip);
         port = 8080;
         portBase = 8080;
         System.out.println("Server.Server started!");

         try(ServerSocket serverSocket = new ServerSocket(portBase)){
             while(true) {
                 try {//redirection des connection

                     Socket s = serverSocket.accept();
                     System.out.println("un client est là");
                     DataOutputStream os = new DataOutputStream(s.getOutputStream());
                     if (i % nbPlayers == 0) {
                         i = 0;
                         port++;
                         Room newRoom = new Room(port);
                         newRoom.start();
                     }
                     System.out.println(port);
                     os.writeInt(port);
                     os.flush();
                     i++;
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
