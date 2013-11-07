package se.stjerneman.anonchat.client.ui;

import java.awt.Font;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import se.stjerneman.anonchat.client.Client;

public class UserListGUI {

    private static UserListGUI instace = null;

    private Client client;

    private JScrollPane scrollPaneUserList;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;

    public static UserListGUI getInstance () {
        if (UserListGUI.instace == null) {
            UserListGUI.instace = new UserListGUI();
        }

        return UserListGUI.instace;
    }

    private UserListGUI () {
        this.client = Client.getInstance();
        this.scrollPaneUserList = new JScrollPane();
        scrollPaneUserList
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.userListModel = new DefaultListModel<String>();
        userList = new JList<String>();
        userList.setFont(new Font("Tahoma", Font.PLAIN, 13));
        userList.setModel(this.userListModel);
        scrollPaneUserList.setViewportView(userList);
    }

    public JScrollPane getScrollPaneUserList () {
        return scrollPaneUserList;
    }

    public JList<String> getUserList () {
        return userList;
    }

    public DefaultListModel<String> getUserListModel () {
        return userListModel;
    }

    protected void updateUserList (List<String> userList) {
        this.userListModel.clear();

        for (String username : userList) {
            this.userListModel.addElement(username);
        }
    }
}
