package livestream.models;

import java.io.Serializable;
import java.util.ArrayList;

public class AllUser implements Serializable {

    private ArrayList<User> mUserList;

    public AllUser(ArrayList<User> mUserList) {
        this.mUserList = mUserList;
    }

    public ArrayList<User> getmUserList() {
        return mUserList;
    }

    public void setmUserList(ArrayList<User> mUserList) {
        this.mUserList = mUserList;
    }
}
