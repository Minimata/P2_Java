package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import CommunicationPaquet.Paquet;

/**
 * Created by nicolas on 11.04.16.
 */
public class ClientListener extends Thread {

    private boolean infiniteLoop = true;
    private Paquet order;
    private Socket socket;
    private ObjectInputStream scanner;

    public ClientListener(Socket s){
        System.out.println("Creation du listener ! ");
        socket = s;
    }

    public void run() {
        listen();
    }

    public synchronized void listen(){
        while(infiniteLoop){
            try{
                scanner = new ObjectInputStream(socket.getInputStream());
                order = (Paquet) scanner.readObject();
                System.out.println(order.toString());
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
