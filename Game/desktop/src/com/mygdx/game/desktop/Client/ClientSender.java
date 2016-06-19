package com.mygdx.game.desktop.Client;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.mygdx.game.desktop.CommunicationPaquet.Paquet;

/**
 * Created by nicolas on 11.04.16.
 * This class integrates all the elements needed to send packages to the server.
 */
public class ClientSender {

    private Socket socket;

    public ClientSender(Socket s) {
        System.out.println("Creation du sender !");
        socket = s;
    }

    /**
     * Sends a package to the server.
     *
     * @param p the package to be sent.
     */
    public void send(Paquet p) {
        try {
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            writer.writeObject(p);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
