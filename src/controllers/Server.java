package controllers;

import livestream.models.RoomMessage;
import livestream.models.RoomUser;
import livestream.models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Server implements ClientThread.MessageCallBack {
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

                ClientThread clientThread = new ClientThread(clientSocket, this, this);
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

    public Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    @Override
    public void call(List<RoomUser> userList, RoomMessage roomMessage) {
        for (int i=0; i<=userList.size(); i++) {
            int j = i;
            mThreadNameUserHashMap.forEach((key, value) -> {
                if (userList.get(j).getUserId() == value.getId()) {
                    ClientThread clientThread = (ClientThread) getThreadByName(key);
                    clientThread.pingNewMessage(roomMessage);
                }
            }
            );
        }
    }
}
