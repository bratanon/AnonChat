package se.stjerneman.anonchat.client.ui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * 
 * @author Emil Stjerneman
 * 
 */
public class MenuBar {

    private final JMenuBar menuBar = new JMenuBar();
    private JMenu mnChat = new JMenu("Chat");
    private JMenu mnView = new JMenu("View");
    private JMenu mnHelp = new JMenu("Help");

    private JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
    private JMenuItem mntmSettings = new JMenuItem("Settings");
    private JMenuItem mntmExit = new JMenuItem("Exit");

    private JCheckBoxMenuItem mntmShowUsers = new JCheckBoxMenuItem(
            "Show users");

    private JMenuItem mntmLicense = new JMenuItem("License");
    private JMenuItem mntmAboutChat = new JMenuItem("About Chat");

    public MenuBar () {
        menuBar.add(mnChat);
        menuBar.add(mnView);
        menuBar.add(mnHelp);

        mnChat.add(mntmDisconnect);
        mnChat.addSeparator();
        mnChat.add(mntmSettings);
        mnChat.addSeparator();
        mnChat.add(mntmExit);

        mnView.add(mntmShowUsers);

        mnHelp.add(mntmLicense);
        mnHelp.add(mntmAboutChat);

        mntmShowUsers.setSelected(true);
        mntmDisconnect.setEnabled(false);
        mntmSettings.setEnabled(false);

        mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));

    }

    public JMenuBar getMenuBar () {
        return menuBar;
    }

    public JCheckBoxMenuItem getMntmShowUsers () {
        return mntmShowUsers;
    }

}
