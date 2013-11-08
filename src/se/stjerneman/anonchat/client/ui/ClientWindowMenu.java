package se.stjerneman.anonchat.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import se.stjerneman.anonchat.client.Client;

/**
 * 
 * @author Emil Stjerneman
 * 
 */
public class ClientWindowMenu {

    private ClientWindow clientWindow;

    private final JMenuBar menuBar = new JMenuBar();
    private JMenu mnChat = new JMenu("Chat");
    private JMenu mnView = new JMenu("View");
    private JMenu mnHelp = new JMenu("Help");

    private JMenuItem mntmConnect = new JMenuItem("Connect");
    private JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
    private JMenuItem mntmSettings = new JMenuItem("Settings");
    private JMenuItem mntmExit = new JMenuItem("Exit");

    private JCheckBoxMenuItem mntmShowUsers = new JCheckBoxMenuItem(
            "Show users");

    private JMenuItem mntmAboutChat = new JMenuItem("About AnonChat");
    private JMenuItem mntmLicens = new JMenuItem("Licens");

    public ClientWindowMenu (ClientWindow cw) {
        clientWindow = cw;

        menuBar.add(mnChat);
        menuBar.add(mnView);
        menuBar.add(mnHelp);

        mnChat.add(mntmConnect);
        mnChat.add(mntmDisconnect);
        mnChat.addSeparator();
        mnChat.add(mntmSettings);
        mnChat.addSeparator();
        mnChat.add(mntmExit);

        mnView.add(mntmShowUsers);

        mnHelp.add(mntmLicens);
        mnHelp.add(mntmAboutChat);

        mntmShowUsers.setSelected(true);
        mntmConnect.setVisible(false);

        mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));

        mntmConnect.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                ConnectDialog cd = new ConnectDialog(clientWindow);
                cd.setVisible(true);
                toggleElements(true);

                clientWindow.startListen();
            }
        });

        mntmDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                Client.getInstance().disconnect();
                toggleElements(false);

                clientWindow.getClientListModel().clear();
            }
        });

        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                System.exit(0);
            }
        });

        mntmLicens.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                LicensDialog ld = new LicensDialog();
                ld.setVisible(true);
                ld.pack();
            }
        });

        mntmAboutChat.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                new AboutDialog().setVisible(true);
            }
        });

        mntmShowUsers.addItemListener(new ItemListener() {
            public void itemStateChanged (ItemEvent e) {
                clientWindow.getClientListScrollPane().setVisible(
                        mntmShowUsers.isSelected());
            }
        });
    }

    public JMenuBar getMenuBar () {
        return menuBar;
    }

    private void toggleElements (boolean bool) {
        mntmConnect.setVisible(!bool);
        mntmDisconnect.setVisible(bool);
        clientWindow.getMessageTextArea().setEnabled(bool);
        clientWindow.getBtnSend().setEnabled(bool);
    }
}
