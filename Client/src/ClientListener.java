import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by nicolas on 11.04.16.
 */
public class ClientListener extends Thread {

    private String host;
    private int port;
    private boolean infiniteLoop = true;
    private Paquet order;
    private Socket socket;

    public ClientListener(Socket s){
        System.out.println("Creation du listener ! ");
        socket = s;
        host = "localhost";
        port = 9090;
    }

    public void run() {
        try {
            System.out.println("Listener started");
            while (infiniteLoop) {
                try{
                    ObjectInputStream scanner = new ObjectInputStream(socket.getInputStream());
                    order = (Paquet) scanner.readObject();
                    System.out.println(order.toString());
                    infiniteLoop = false;
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
