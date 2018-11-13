package controllers;

import exception.UserDAOException;
import livestream.models.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {

    private Socket mClientSocket;
    private Server mServer;
    private ObjectInputStream mObjectInputStream;
    private ObjectOutputStream mObjectOutputStream;
    private MessageCallBack mMessageCallback;

    public ClientThread(Socket s, Server server, MessageCallBack messageCallBack) {
        super(s.getInetAddress().getHostAddress());
        mClientSocket = s;
        mServer = server;
        mMessageCallback = messageCallBack;
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
                        /** Login
                         * RequestedObj: User
                         * ReturnedObj: User
                         * */
                        case 0 :
                            User user = loginAccount((User) baseRequest.getData());
                            if (user == null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(0,"Wrong password",null));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(0,"Login success",user));
                            }
                            break;
                        /** Register
                         *  RequestedObj: User
                         *  ReturnedObj: null
                         *  */
                        case 1 :
                            if (registerAccount((User) baseRequest.getData()) == 0) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(1,"Register failed",null));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(1,"Register success",null));
                            }
                            break;
                        /** Get all user
                         *  RequestedObj:
                         *  ReturnedObj: Arraylist<User>
                         *  */
                        case 2 :
                            ArrayList<User> allUser = getAllUser();
                            if (getAllUser() != null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(2,"Success",allUser));
                            }
                            break;
                        /** Create new room
                         * RequestedObj: Room
                         * ReturnedObj: Room
                         *  */
                        case 3 :
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
                        /** Get active rooms
                         * RequestedObj:
                         * ReturnedObj: Arraylist<Room>
                         * */
                        case 4 :
                            ArrayList<Room> activeRooms = getActiveRoom();
                            if(activeRooms != null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(4,"Success",activeRooms));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(4,"Failed",null));
                            }
                            break;
                        /** Get all rooms
                         * RequestedObj:
                         * ReturnedObj: Arraylist<Room>
                         * */
                        case 5 :
                            ArrayList<Room> allRooms = getAllRoom();
                            if (allRooms.size() == 0) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(5,"Failed",null));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(5,"Success",allRooms));
                            }
                            break;
                        /** Update room status (0: deactive, 1: active ) *
                         * RequestedObj: Room
                         * ReturnedObj: null
                         * */
                        case 6 :
                            if (updateRoomStatus((Room) baseRequest.getData())) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(6,"Update success",null));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(6,"Update failed",null));
                            }
                            break;
                        /** Create new message *
                         * RequestedObj: RoomMessage
                         * ReturnedObj: RoomMessage
                         * */
                        case 7 :
                            RoomMessage newMessage = createNewMessage((RoomMessage) baseRequest.getData());
                            Room room = getRoomById(newMessage.getmRoomId());
                            if (newMessage != null && room != null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(7,"Create success",newMessage));
                                mMessageCallback.call(room.getRoomUsers(), newMessage);
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(7,"Create failed",null));
                            }
                            break;
                        /** Get all message *
                         * RequestedObj: Room
                         * ReturnedObj: Arraylist<RoomMessage>
                         * */
                        case 8 :
                            ArrayList<RoomMessage> allRoomMessage = getAllMessages((Room) baseRequest.getData());
                            if (allRoomMessage != null) {
                                mObjectOutputStream.writeObject(new BaseRequest<>(8,"Success",allRoomMessage));
                            } else {
                                mObjectOutputStream.writeObject(new BaseRequest<>(8,"Failed",null));
                            }
                            break;git ad
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

    private boolean updateRoomStatus(Room room) {
        try {
            return new RoomDAO().updateRoomStatus(room.getId(),room.getStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private RoomMessage createNewMessage(RoomMessage message) {
        try {
            return new RoomMessageDAO().createNewMessageInRoom(message.getmRoomId(),message.getUser().getId(),message.getContent());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<RoomMessage> getAllMessages(Room room) {
        try {
            return new RoomMessageDAO().getAllMessagesByRoomId(room.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void pingNewMessage(RoomMessage roomMessage) {
        try {
            mObjectOutputStream.writeObject(new BaseRequest<>(8,"New message",roomMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface MessageCallBack {
        void call(List<RoomUser> userList, RoomMessage roomMessage);
    }
}
