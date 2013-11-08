package se.stjerneman.anonchat.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import se.stjerneman.anonchat.client.Client;
import se.stjerneman.anonchat.messages.Message;

/**
 * 
 * @author Emil Stjerneman
 * 
 * 
 */
public class ClientWindow {

    // public static ClientWindow instace = null;

    protected Client client;

    private JFrame frame;
    private JButton btnSend;
    private JTextArea messageTextArea;
    private JList<String> clientList;
    private DefaultListModel<String> clientListModel;
    private JTextPane chatLogTextPane;
    private final Action sendMessageAction = new SendMessageAction();
    private JScrollPane clientListScrollPane;

    /**
     * Launch the application.
     */
    public static void main (String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run () {
                try {
                    String UIName = UIManager.getSystemLookAndFeelClassName();
                    UIManager.setLookAndFeel(UIName);

                    ClientWindow window = new ClientWindow();
                    window.frame.pack();
                    window.frame.setLocationRelativeTo(null);
                    window.frame.setVisible(true);

                    ConnectDialog cd = new ConnectDialog(window);
                    cd.setVisible(true);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ClientWindow () {
        initialize();
    }

    public JButton getBtnSend () {
        return btnSend;
    }

    public JTextArea getMessageTextArea () {
        return messageTextArea;
    }

    public DefaultListModel<String> getClientListModel () {
        return clientListModel;
    }

    public JList<String> getClientList () {
        return clientList;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize () {
        frame = new JFrame();
        frame.setIconImages(ApplicationIcons.getIcons());
        frame.setJMenuBar(new ClientWindowMenu(this).getMenuBar());
        frame.setTitle("AnonChat");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setPreferredSize(new Dimension(800, 600));

        JSplitPane mainSplitPane = new JSplitPane();
        mainSplitPane.setResizeWeight(1.0);
        frame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);

        JSplitPane logSplitPane = new JSplitPane();
        logSplitPane.setResizeWeight(1.0);
        logSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setLeftComponent(logSplitPane);

        JScrollPane chatLogScrollPane = new JScrollPane();
        chatLogScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatLogScrollPane.setMinimumSize(new Dimension(100, 100));
        logSplitPane.setLeftComponent(chatLogScrollPane);

        chatLogTextPane = new JTextPane();
        chatLogTextPane.setEditable(false);
        chatLogScrollPane.setViewportView(chatLogTextPane);

        JPanel sendPanel = new JPanel();
        sendPanel.setMinimumSize(new Dimension(10, 95));
        sendPanel.setPreferredSize(new Dimension(10, 95));
        logSplitPane.setRightComponent(sendPanel);
        sendPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane messageScrollPane = new JScrollPane();
        messageScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sendPanel.add(messageScrollPane, BorderLayout.CENTER);

        messageTextArea = new JTextArea();
        messageTextArea.setMinimumSize(new Dimension(4, 60));
        messageTextArea.setRows(3);
        messageTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {

                if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    messageTextArea.append("\n");
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                    e.consume();
                }
            }
        });
        messageScrollPane.setViewportView(messageTextArea);

        JPanel buttonPanel = new JPanel();
        sendPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        btnSend = new JButton("Send");
        btnSend.setAction(sendMessageAction);
        buttonPanel.add(btnSend);

        clientListScrollPane = new JScrollPane();
        clientListScrollPane.setPreferredSize(new Dimension(150, 2));
        clientListScrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        clientListScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        clientListScrollPane.setMinimumSize(new Dimension(100, 10));
        mainSplitPane.setRightComponent(clientListScrollPane);

        clientListModel = new DefaultListModel<String>();
        clientList = new JList<String>();
        clientList.setPreferredSize(new Dimension(100, 100));
        clientList.setModel(clientListModel);

        clientListScrollPane.setViewportView(clientList);
    }

    public JTextPane getChatLogTextPane () {
        return chatLogTextPane;
    }

    public JScrollPane getClientListScrollPane () {
        return clientListScrollPane;
    }

    protected void startListen () {
        new Thread(new IncomingListener()).start();
    }

    private void sendMessage () {
        getMessageTextArea().setEnabled(false);
        String message = getMessageTextArea().getText();

        if (message.isEmpty()) {
            getMessageTextArea().setEnabled(true);
            return;
        }

        getMessageTextArea().setText("");

        client.sendMessage(message);

        getMessageTextArea().setEnabled(true);
        getMessageTextArea().requestFocusInWindow();
    }

    private class SendMessageAction extends AbstractAction {

        private static final long serialVersionUID = -8905572626321773564L;

        public SendMessageAction () {
            putValue(NAME, "Send");
        }

        public void actionPerformed (ActionEvent e) {
            sendMessage();
        }
    }

    /**
     * Listen for incoming packets from the server.
     * 
     * @author Emil Stjerneman
     * 
     */
    private class IncomingListener implements Runnable {
        JTextPane chatLog = getChatLogTextPane();

        @Override
        public void run () {

            try {
                while (true) {
                    byte sentByte = client.getInputStream().readByte();

                    switch (sentByte) {
                        case 1:
                            catchMessageObject();
                            break;
                        case 2:
                            catchUserListObject();
                            break;
                    }
                }
            }
            catch (SocketException e) {
                // TODO: Make the message area non editable.
                // TODO: Switch menu items.
                JOptionPane.showMessageDialog(null, "You got disconnected.",
                        "Disconnected", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (ClassNotFoundException e) {
                // TODO: Log this!
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO: Log this!
                e.printStackTrace();
            }
        }

        private void catchMessageObject () throws ClassNotFoundException,
                IOException {
            Message msg = (Message) client.getInputStream().readObject();

            StyledDocument doc = chatLog.getStyledDocument();

            try {
                String text = "\n" + msg.formatMessage();
                doc.insertString(doc.getLength(), text, msg.getStyle(doc));
            }
            catch (BadLocationException e) {}

            chatLog.setCaretPosition(doc.getLength());
        }

        private void catchUserListObject () throws ClassNotFoundException,
                IOException {

            List<String> clientNames = (List<String>) client
                    .getInputStream()
                    .readObject();

            clientListModel.clear();

            for (String username : clientNames) {
                clientListModel.addElement(username);
            }
        }
    }

}
