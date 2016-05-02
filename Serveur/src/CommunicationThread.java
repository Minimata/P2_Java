import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationThread extends Thread{

    private Socket socket;

    public CommunicationThread(Socket s){
        socket = s;
    }

    public void run(){
        ObjectInputStream ois;
        ObjectOutputStream oos;
        boolean running = true;
        System.out.println("client dans la salle");

        try{
            while(running){
                //..... envoi et réception des données

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
