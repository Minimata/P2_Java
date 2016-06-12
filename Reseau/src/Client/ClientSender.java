package Client;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import CommunicationPaquet.Paquet;

/**
 * Created by nicolas on 11.04.16.
 */
public class ClientSender extends Thread{

    private String host;
    private int port;
    private boolean infiniteLoop;
    private Socket socket;
    private Paquet p;
    private ObjectOutputStream writer;

    public ClientSender(Socket s, Paquet p ){
        System.out.println("Creation du sender !");
        socket = s;
        host = "localhost";
        port = 8080;
        infiniteLoop = true;
        this.p = p;
    }

    public void run(){
        send();
    }

    public synchronized void send(){
        int i = 0;
        while(infiniteLoop){
            try{
                //DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                while(i < 2){
                    writer = new ObjectOutputStream(socket.getOutputStream());
                    writer.writeObject(p);
                    writer.flush();
                    i++;
                }
                //writer.close();
                //writer.writeObject(p);
                //infiniteLoop = false;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
