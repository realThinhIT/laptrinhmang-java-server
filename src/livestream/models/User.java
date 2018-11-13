package livestream.models;

import java.io.Serializable;

public class User implements Serializable {

    private long mId;
    private String mUsername;
    private String mPassword;
    private String mName;
    private String mCreatedAt;

    public User(long id, String username, String password, String name, String createdAt) {
        mId = id;
        mUsername = username;
        mPassword = password;
        mName = name;
        mCreatedAt = createdAt;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }
}
