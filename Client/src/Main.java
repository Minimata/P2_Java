import java.io.IOException;
import java.net.Socket;

/**
 * Created by nicolas on 11.04.16.
 */
public class Main {

    private static String host;
    private static int port;

    public static void main(String[] args) {
        createNetwork();
    }

    public static void createNetwork(){
        host = "localhost";
        port = 9090;

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
