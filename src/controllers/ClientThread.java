package controllers;

import livestream.models.BaseRequest;
import livestream.models.User;

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
                BaseRequest baseRequest = (BaseRequest) mObjectInputStream.readObject();
                if (baseRequest != null) {
                    switch (baseRequest.getTypeRequest()) {
                        case 0:
                            loginAccount((User) baseRequest.getData());
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.println(mClientSocket.getRemoteSocketAddress() + " send wrong data format");
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

    private void loginAccount(User user) {
        System.out.println(user.getName() + " " + user.getPassword());
    }
}
