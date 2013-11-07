package se.stjerneman.anonchat.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1055802987502741111L;

    List<String> userList;

    public UserList () {
        this.userList = new ArrayList<String>();
    }

    public List<String> getUserList () {
        return userList;
    }

    public void add (String username) {
        this.userList.add(username);
    }

    public void reset () {
        this.userList.clear();
    }

}
