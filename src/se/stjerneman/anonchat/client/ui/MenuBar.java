package se.stjerneman.anonchat.client.ui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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

    private JMenuBar menuBar = new JMenuBar();
    JMenu mnChat = new JMenu("Chat");
    JMenu mnView = new JMenu("View");
    JMenu mnHelp = new JMenu("Help");

    JMenuItem mntmSettings = new JMenuItem("Settings");
    JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
    JMenuItem mntmExit = new JMenuItem("Exit");

    JMenuItem mntmShowUsers = new JMenuItem("Show users");

    JMenuItem mntmLicense = new JMenuItem("License");
    JMenuItem mntmAboutChat = new JMenuItem("About Chat");

    public MenuBar () {
        menuBar.add(mnChat);
        menuBar.add(mnView);
        menuBar.add(mnHelp);

        mnChat.add(mntmSettings);
        mnChat.addSeparator();
        mnChat.add(mntmDisconnect);
        mnChat.addSeparator();
        mnChat.add(mntmExit);

        mnView.add(mntmShowUsers);

        mnHelp.add(mntmLicense);
        mnHelp.add(mntmAboutChat);

        mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));

    }

    public JMenuBar getMenuBar () {
        return menuBar;
    }

    public JMenuBar organizeMenu (String view) {
        switch (view) {
            case "login":
                mntmSettings.setEnabled(false);
                mntmDisconnect.setEnabled(false);
                mnView.setEnabled(false);
                break;
        }

        return this.getMenuBar();
    }
}
