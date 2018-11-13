package livestream.models;

import java.io.Serializable;

public class RoomMessage implements Serializable {

    private int mId;
    private String mContent;
    private String mCreateAt;
    private User mUser;

    public RoomMessage(int id, String content, User user, String createAt) {
        mId = id;
        mContent = content;
        mCreateAt = createAt;
        mUser = user;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getCreateAt() {
        return mCreateAt;
    }

    public void setCreateAt(String createAt) {
        mCreateAt = createAt;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
