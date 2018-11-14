package controllers;

import livestream.models.RoomMessage;
import livestream.models.RoomUser;
import livestream.models.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        for (String key : mThreadNameUserHashMap.keySet()) keys.add(key);
        for (User value : mThreadNameUserHashMap.values()) users.add(value);
        for (int i=0; i<userList.size(); i++) {
            RoomUser roomUser = userList.get(i);
            for (int j = 0; j<users.size(); j++) {
                System.out.println(users.get(j).toString());
//                if (roomUser.getUserId() == users.get(j).getId()) {
//                    ClientThread clientThread = (ClientThread) getThreadByName(keys.get(j));
//                    clientThread.pingNewMessage(roomMessage);
//                }
            }
        }

    }
}
