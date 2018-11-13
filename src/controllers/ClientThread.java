package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {

    private Socket mClientSocket;
    private Server mServer;
    private ObjectInputStream mObjectInputStream;
    private ObjectOutputStream mObjectOutputStream;

    public ClientThread(Socket s, Server server) {
        super(s.getInetAddress().getHostAddress());
        mClientSocket = s;
        mServer = server;
        System.out.println("Client Remote Address: " + mClientSocket.getRemoteSocketAddress().toString() + " connected");
    }

    @Override
    public void run() {
        super.run();

        try {
            mObjectInputStream = new ObjectInputStream(mClientSocket.getInputStream());
            mObjectOutputStream = new ObjectOutputStream(mClientSocket.getOutputStream());

            while (true) {
                String o = (String) mObjectInputStream.readObject();
                if (o != null) {
                    System.out.println(o);
                } else {
                    System.out.println("null");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("------- Client Error -------\n" + e);
            System.out.println("Client remote address: " + mClientSocket.getRemoteSocketAddress());
            if (mObjectInputStream != null && mObjectOutputStream != null) {
                try {
                    mObjectInputStream.close();
                    mObjectOutputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (mClientSocket != null) {
                try {
                    mClientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println("-----------------------------");
        }

        mServer.removeClientSocket(getName());
        System.out.println("Client remote address: " + mClientSocket.getRemoteSocketAddress() + " disconnected");
    }
}
