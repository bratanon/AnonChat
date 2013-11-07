package se.stjerneman.anonchat.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import se.stjerneman.anonchat.client.Client;

/**
 * 
 * @author Emil Stjerneman
 * 
 */
public class MenuBar {

    private Client client;

    private ClientWindow clientGUI;

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

    public MenuBar (ClientWindow cgui) {
        this.clientGUI = cgui;
        this.client = Client.getInstance();
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
                try {
                    client.connect();
                    mntmConnect.setVisible(false);
                    mntmDisconnect.setVisible(true);

                    clientGUI.getMessageArea().setEnabled(true);

                    clientGUI.getChatPane().setText("");
                    clientGUI.getChatPane().setEnabled(true);

                    clientGUI.getBtnSend().setEnabled(true);

                    clientGUI.startListening();

                }
                catch (NullPointerException | IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "Error when connecting to host.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mntmDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                client.disconnect();
                mntmConnect.setVisible(true);
                mntmDisconnect.setVisible(false);

                clientGUI.getMessageArea().setText("");
                clientGUI.getMessageArea().setEnabled(false);

                // clientGUI.getChatPane().setText("Disconnected...");
                clientGUI.getChatPane().setEnabled(false);

                clientGUI.getBtnSend().setEnabled(false);

                UserListGUI.getInstance().getUserListModel().clear();
            }
        });

        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                System.exit(0);
            }
        });

        mntmLicens.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                new LicensDialog().setVisible(true);
            }
        });

        mntmAboutChat.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                new AboutDialog().setVisible(true);
            }
        });

        mntmShowUsers.addItemListener(new ItemListener() {
            public void itemStateChanged (ItemEvent e) {
                UserListGUI.getInstance().getScrollPaneUserList()
                        .setVisible(mntmShowUsers.isSelected());
            }
        });

    }

    public JMenuBar getMenuBar () {
        return menuBar;
    }

    public JCheckBoxMenuItem getMntmShowUsers () {
        return mntmShowUsers;
    }

}
