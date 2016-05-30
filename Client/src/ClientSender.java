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
    private ObjectOutputStream writer;

    public ClientSender(Socket s, Paquet p ){
        System.out.println("Creation du sender !");
        socket = s;
        host = "localhost";
        port = 9090;
        infiniteLoop = true;
        this.p = p;
    }

    public void run(){
        int i = 0;
        while(infiniteLoop){
            try{
                //DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                if(i == 0){
                    writer = new ObjectOutputStream(socket.getOutputStream());
                    writer.writeObject(p);
                    writer.flush();
                    i++;
                }
                //writer.writeObject(p);
                //infiniteLoop = false;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
