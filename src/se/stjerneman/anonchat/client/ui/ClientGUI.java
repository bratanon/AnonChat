package se.stjerneman.anonchat.client.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import se.stjerneman.anonchat.client.Client;

/**
 * 
 * @author Emil Stjerneman
 * 
 */
public class ClientGUI {

    private JFrame frame;
    private final Action sendMessageAction = new SendMessageAction();
    private JTextArea messageArea;
    private JTextPane conversationPane;

    private Client client;

    private JList<String> list;

    private MenuBar menu = new MenuBar();
    private JScrollPane userListScrollPane;

    /**
     * Launch the application.
     */
    public static void main (String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run () {
                try {
                    UIManager
                            .setLookAndFeel(UIManager
                                    .getSystemLookAndFeelClassName());
                    ClientGUI window = new ClientGUI();

                    window.frame.pack();
                    window.frame.setLocationRelativeTo(null);
                    window.frame.setVisible(true);

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
    public ClientGUI () {
        this.showConnectDialog();

        initialize();
        setMenuBarActions();

        new Thread(new ChatMessageListener()).start();
    }

    public JTextArea getMessageArea () {
        return messageArea;
    }

    public JTextPane getConversationPane () {
        return conversationPane;
    }

    public JScrollPane getUserListScrollPane () {
        return userListScrollPane;
    }

    public JList getList () {
        return list;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize () {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setJMenuBar(this.menu.getMenuBar());
        frame.getContentPane().setLayout(null);

        JScrollPane ConverationScrollPane = new JScrollPane();
        ConverationScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ConverationScrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ConverationScrollPane.setCursor(Cursor
                .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        ConverationScrollPane.setAutoscrolls(true);
        ConverationScrollPane.setBounds(10, 11, 560, 412);
        frame.getContentPane().add(ConverationScrollPane);

        conversationPane = new JTextPane();
        conversationPane.setEditable(false);
        ConverationScrollPane.setViewportView(conversationPane);

        userListScrollPane = new JScrollPane();
        userListScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        userListScrollPane.setBounds(580, 81, 194, 415);
        frame.getContentPane().add(userListScrollPane);

        list = new JList<String>();
        userListScrollPane.setViewportView(list);

        JScrollPane messareAreaScrollPane = new JScrollPane();
        messareAreaScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        messareAreaScrollPane.setAutoscrolls(true);
        messareAreaScrollPane.setBounds(10, 434, 560, 62);
        frame.getContentPane().add(messareAreaScrollPane);

        messageArea = new JTextArea();
        messageArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {

                if (e.isShiftDown() && e.getKeyCode() == e.VK_ENTER) {
                    messageArea.append("\n");
                }
                else if (e.getKeyCode() == e.VK_ENTER) {
                    sendMessage();
                    e.consume();
                }

            }
        });
        messareAreaScrollPane.setViewportView(messageArea);

        JButton btnSend = new JButton("Send");
        btnSend.setAction(sendMessageAction);
        btnSend.setBounds(10, 507, 117, 23);
        frame.getContentPane().add(btnSend);
    }

    private class SendMessageAction extends AbstractAction {

        public SendMessageAction () {
            putValue(NAME, "Send message");
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            sendMessage();
        }
    }

    private class ChatMessageListener implements Runnable {
        @Override
        public void run () {
            String message;

            try {
                while ((message = client.getInputStream().readLine()) != null) {
                    conversationPane.setText(conversationPane.getText() + "\n"
                            + message);

                    // Auto-scrolls the JTextPane
                    int pos = conversationPane.getText().length();
                    conversationPane.setCaretPosition(pos);
                }
            }
            catch (IOException e) {
                // TODO: Log this!
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showConnectDialog () {
        ConnectDialog dialog = new ConnectDialog();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        this.client = dialog.getClient();
    }

    private void setMenuBarActions () {
        this.menu.getMntmShowUsers().addItemListener(new ItemListener() {
            public void itemStateChanged (ItemEvent e) {
                boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
                getUserListScrollPane().setVisible(selected);
            }
        });
    }

    private void sendMessage () {
        messageArea.setEnabled(false);
        String message = messageArea.getText();

        if (message.isEmpty()) {
            messageArea.setEnabled(true);
            return;
        }

        messageArea.setText("");

        this.client.getOutputStream().println(message);
        this.client.getOutputStream().flush();

        messageArea.setEnabled(true);
        messageArea.requestFocusInWindow();
    }

}
