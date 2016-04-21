import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by nicolas on 11.04.16.
 */
public class ClientSender extends Thread{

    private String host;
    private int port;
    private boolean infiniteLoop;
    private Socket socket;
    private Paquet p;

    public ClientSender(Socket s){
        System.out.println("Creation du sender !");
        socket = s;
        host = "localhost";
        port = 9090;
        infiniteLoop = true;
        p = new Paquet();
    }

    public void run(){
        while(infiniteLoop){
            try{
                ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
                writer.writeObject(p);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
