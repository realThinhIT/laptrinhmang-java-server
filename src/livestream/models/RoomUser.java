package livestream.models;


import java.io.Serializable;

public class RoomUser implements Serializable {

    private long mId;
    private long mRoomId;
    private long mUserId;
    private long mRequestStatus;
    private long mStatus;

    public RoomUser(long id, long roomId, long userId, long requestStatus, long status) {
        mId = id;
        mRoomId = roomId;
        mUserId = userId;
        mRequestStatus = requestStatus;
        mStatus = status;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getRoomId() {
        return mRoomId;
    }

    public void setRoomId(long roomId) {
        mRoomId = roomId;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public long getRequestStatus() {
        return mRequestStatus;
    }

    public void setRequestStatus(long requestStatus) {
        mRequestStatus = requestStatus;
    }

    public long getStatus() {
        return mStatus;
    }

    public void setStatus(long status) {
        mStatus = status;
    }
}
