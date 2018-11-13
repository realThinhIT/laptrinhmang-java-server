package livestream.models;

import java.io.Serializable;

public class RoomMessage implements Serializable {

    private long mId;
    private String mContent;
    private long mUserId;
    private long mRoomId;
    private String mCreateAt;

    public RoomMessage(long id, String content, long userId, long roomId, String createAt) {
        mId = id;
        mContent = content;
        mUserId = userId;
        mRoomId = roomId;
        mCreateAt = createAt;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public long getRoomId() {
        return mRoomId;
    }

    public void setRoomId(long roomId) {
        mRoomId = roomId;
    }

    public String getCreateAt() {
        return mCreateAt;
    }

    public void setCreateAt(String createAt) {
        mCreateAt = createAt;
    }
}
