package controllers;

import livestream.models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private ServerSocket mServerSocket;
    private HashMap<String, User> mThreadNameUserHashMap = new HashMap<>();

    public void openServer() {
        try {
            mServerSocket = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println("------- Server Error -------\n" + e.getMessage() + "\n---------------------");
        }
    }

    public void listening() {
        while (true) {
            try {
                Socket clientSocket = mServerSocket.accept();

                ClientThread clientThread = new ClientThread(clientSocket, this);
                addClientSocket(clientSocket);
                clientThread.run();
            } catch (IOException e) {
                System.out.println("------- Client Error -------\n" + e.getMessage() + "\n---------------------");
            }
        }
    }

    public void close() {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            System.out.println("------- Server Error -------\n" + e.getMessage() + "\n---------------------");
        }
    }

    public void removeClientSocket(String remoteSocketAddress) {
        mThreadNameUserHashMap.remove(remoteSocketAddress);
    }

    private void addClientSocket(Socket clientSocket) {
        mThreadNameUserHashMap.put(clientSocket.getRemoteSocketAddress().toString(), null);
    }
}
