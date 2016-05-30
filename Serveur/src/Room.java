import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by steve.nadalin on 11/04/2016.
 */
public class Room extends Thread{

    private Collection<ObjectOutputStream> outStreams = new LinkedList<>();
    protected LinkedList<Paquet> toSend = new LinkedList<>();
    public static ServerSocket sSocket;
    private int port;

    public Room(int port){
        this.port = port;
    }

    private void sendPaquet(){
        System.out.println(toSend.size());
        if(toSend.size()!=0){
            Paquet paquetToSend = toSend.getFirst();
            toSend.removeFirst();
            System.out.println("envoi depuis la room: "+paquetToSend.toString());
            for(ObjectOutputStream oos : outStreams){
                try{
                    oos.writeObject(paquetToSend);
                    oos.flush();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }else{
            //ne fait rien
        }
    }

    public synchronized void setPaquet(Paquet p){
        this.toSend.add(p);
    }

    public void run(){
        String out = "connecté à la salle";
        int i = 0;
        try {
            System.out.println("Room started!");
            sSocket = new ServerSocket(port);
            while(true) {
                try {
                    if(i<Server.nbPlayers){
                        //System.out.println("passe");
                        Socket socket = sSocket.accept();
                        outStreams.add(new ObjectOutputStream(socket.getOutputStream()));
                        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                        System.out.println("test" + socket.getLocalPort());
                        os.writeUTF(out);
                        os.flush();
                        Thread t = new CommunicationThread(socket,this);
                        t.start();
                        i++;
                    }else{
                        sendPaquet();
                    }


                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}