import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * Created by nicolas on 11.04.16.
 */
public class Main {

    private static String host;
    private static final int portbase = 9090;
    private static int port;

    public static void main(String[] args) {
        port = 0;
        if(initiateConnection()){
           /* try{
                sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }*/

            createNetwork();
        }

    }

    public static boolean initiateConnection(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Entrez l'adresse ip du serveur: ");
        host = scan.nextLine();
        try{
            Socket sock = new Socket(host, portbase);
            DataInputStream scanner = new DataInputStream(sock.getInputStream());
            port = scanner.readInt();
            System.out.println(port);
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void createNetwork(){
        try{
            System.out.println("Creation du socket !");
            Socket socket = new Socket(host, port);
            ClientListener c = new ClientListener(socket);
            c.start();
            ClientSender d = new ClientSender(socket);
            d.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
