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
    JMenu mnChat = new JMenu("Chat");
    JMenu mnView = new JMenu("View");
    JMenu mnHelp = new JMenu("Help");

    JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
    JMenuItem mntmSettings = new JMenuItem("Settings");
    JMenuItem mntmExit = new JMenuItem("Exit");

    JCheckBoxMenuItem mntmShowUsers = new JCheckBoxMenuItem("Show users");

    JMenuItem mntmLicense = new JMenuItem("License");
    JMenuItem mntmAboutChat = new JMenuItem("About Chat");

    public MenuBar() {
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

        mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));

    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JMenuBar organizeMenu(String view) {
        switch (view) {
            case "start":
                mntmDisconnect.setEnabled(false);
                mntmSettings.setEnabled(false);
                break;
        }

        return this.getMenuBar();
    }
}
