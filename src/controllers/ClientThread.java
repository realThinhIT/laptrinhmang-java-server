package controllers;

import exception.UserDAOException;
import livestream.models.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
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
                        /** Login **/
                        case 0:
                            User user = loginAccount((User) baseRequest.getData());
                            if (user == null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(0,"Wrong password",null));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(0,"Login success",user));
                            }
                            break;
                        /** Register **/
                        case 1:
                            if (registerAccount((User) baseRequest.getData()) == 0) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(1,"Register failed",null));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(1,"Register success",null));
                            }
                            break;
                        /** Get all user **/
                        case 2:
                            ArrayList<User> allUser = getAllUser();
                            if (getAllUser() != null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(2,"Success",allUser));
                            }
                            break;
                        /** Create new room **/
                        case 3:
                            if (createNewRoom((Room) baseRequest.getData()) == 0){
                                mObjectOutputStream.writeObject(new BaseRequest<>(3,"Create failed",null));
                            } else {
                                if (getRoomById(((Room) baseRequest.getData()).getId()) != null) {
                                    mObjectOutputStream.writeObject(new BaseRequest<>(3,"Create success",getRoomById(((Room) baseRequest.getData()).getId())));
                                } else {
                                    mObjectOutputStream.writeObject(new BaseRequest<>(3,"Create failed",null));
                                }
                            }
                            break;
                        /** Get active rooms **/
                        case 4:
                            ArrayList<Room> activeRooms = getActiveRoom();
                            if(activeRooms != null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(4,"Success",activeRooms));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(4,"Failed",null));
                            }
                            break;
                        /** Get all rooms **/
                        case 5 :
                            ArrayList<Room> allRooms = getAllRoom();
                            if (allRooms.size() == 0) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(5,"Failed",null));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(5,"Success",allRooms));
                            }
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

    private User loginAccount(User user) {
        System.out.println(user.getName() + " " + user.getPassword());
        try {
            new UserDAO().getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private int registerAccount(User user) {
        System.out.println(user.getName() + " " + user.getPassword());
        try {
            return new UserDAO().createNewUser(user.getUsername(), user.getPassword(), user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserDAOException e) {
            try {
                mObjectOutputStream.writeObject(new BaseRequest<>(1,"Username is already registered",null));
                return 0;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return 0;
    }

    private ArrayList<User> getAllUser() {
        try {
            return new UserDAO().getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int createNewRoom(Room room) {
        try {
            return new RoomDAO().createNewRoom(room.getName(),room.getOwner().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Room getRoomById(int roomId) {
        try {
            return new RoomDAO().getRoomById(roomId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ArrayList<Room> getActiveRoom() {
        try {
            return new RoomDAO().getAllRooms(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Room> getAllRoom() {
        ArrayList<Room> allRooms = new ArrayList<>();
        try {
            ArrayList<Room> activeRooms = new RoomDAO().getAllRooms(1);
            ArrayList<Room> dectiveRooms = new RoomDAO().getAllRooms(0);
            if (activeRooms != null) {
                allRooms.addAll(activeRooms);
            }
            if (dectiveRooms != null) {
                allRooms.addAll(dectiveRooms);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allRooms;
    }
}
