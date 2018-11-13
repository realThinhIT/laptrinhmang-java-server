package livestream.models;


import java.io.Serializable;

public class RoomUser implements Serializable {

    private int mId;
    private int mRoomId;
    private int mUserId;
    private int mRequestStatus;
    private int mStatus;

    public RoomUser(int id, int roomId, int userId, int requestStatus, int status) {
        mId = id;
        mRoomId = roomId;
        mUserId = userId;
        mRequestStatus = requestStatus;
        mStatus = status;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getRoomId() {
        return mRoomId;
    }

    public void setRoomId(int roomId) {
        mRoomId = roomId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getRequestStatus() {
        return mRequestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        mRequestStatus = requestStatus;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }
}
