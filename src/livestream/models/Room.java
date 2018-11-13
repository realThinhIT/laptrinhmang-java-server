package livestream.models;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {

    private int mId;
    private String mName;
    private User mOwner;
    private String mCreatedAt;
    private long mStatus;
    private List<RoomMessage> mRoomMessages;
    private List<RoomUser> mRoomUsers;

    public Room(int id, String name, User owner, String createdAt, long status, List<RoomMessage> roomMessages, List<RoomUser> roomUsers) {
        mId = id;
        mName = name;
        mOwner = owner;
        mCreatedAt = createdAt;
        mStatus = status;
        mRoomMessages = roomMessages;
        mRoomUsers = roomUsers;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public User getOwner() {
        return mOwner;
    }

    public void setOwner(User owner) {
        mOwner = owner;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public long getStatus() {
        return mStatus;
    }

    public void setStatus(long status) {
        mStatus = status;
    }

    public List<RoomMessage> getRoomMessages() {
        return mRoomMessages;
    }

    public void setRoomMessages(List<RoomMessage> roomMessages) {
        mRoomMessages = roomMessages;
    }

    public List<RoomUser> getRoomUsers() {
        return mRoomUsers;
    }

    public void setRoomUsers(List<RoomUser> roomUsers) {
        mRoomUsers = roomUsers;
    }
}
