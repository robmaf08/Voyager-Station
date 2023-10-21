package com.voyager.voyager_station.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roberto Maffucci
 */
public class Server extends Thread {

    @Override
    public void run() {
        ServerSocket s = null;
        try {
            s = new ServerSocket(6666);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        Socket socket = null;
        try {
            do {
                socket = s.accept();
                //UUID è utilizzato per creare un id univoco da utilizzare come nome del Thread
                Thread t = new CommanderThread(socket, UUID.randomUUID().toString());
                t.start();
            } while (socket.isConnected());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
