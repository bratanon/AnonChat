package se.stjerneman.anonchat.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import se.stjerneman.anonchat.client.Client;
import se.stjerneman.anonchat.utils.ChatMessage;
import se.stjerneman.anonchat.utils.UserList;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * 
 * @author Emil Stjerneman
 * 
 * 
 */
public class ClientGUI {
    private Client client;

    private JFrame frmChat;

    private JTextPane chatPane;
    private JTextArea messageArea;
    private JLabel lblUUID;
    private JButton btnSend;

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

                    ClientGUI window = new ClientGUI();
                    window.frmChat.pack();
                    window.frmChat.setLocationRelativeTo(null);
                    window.frmChat.setVisible(true);

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

        this.initialize();
        this.startListening();
    }

    public JFrame getFrmChat () {
        return frmChat;
    }

    public JTextPane getChatPane () {
        return chatPane;
    }

    public JTextArea getMessageArea () {
        return messageArea;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize () {
        frmChat = new JFrame();
        frmChat.setIconImage(Toolkit
                .getDefaultToolkit()
                .getImage(
                        ClientGUI.class
                                .getResource("/se/stjerneman/anonchat/client/ui/icons/chat.png")));
        frmChat.setTitle("Chat");
        frmChat.setBounds(100, 100, 450, 300);
        frmChat.setJMenuBar(new MenuBar(this).getMenuBar());
        frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmChat.setMinimumSize(new Dimension(800, 600));
        frmChat.setPreferredSize(new Dimension(800, 600));
        frmChat.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("200px"),
                FormFactory.RELATED_GAP_COLSPEC, },
                new RowSpec[] {
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"),
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, }));

        JPanel panel = new JPanel();
        frmChat.getContentPane().add(panel, "2, 2, fill, fill");
        panel.setLayout(new BorderLayout(0, 7));

        JScrollPane scrollPaneChatWindow = new JScrollPane();
        scrollPaneChatWindow.setAutoscrolls(true);
        scrollPaneChatWindow
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPaneChatWindow, BorderLayout.CENTER);

        chatPane = new JTextPane();
        chatPane.setFont(new Font("Arial", Font.PLAIN, 11));
        chatPane.setEditable(false);
        scrollPaneChatWindow.setViewportView(chatPane);

        JScrollPane scrollPaneMessage = new JScrollPane();
        scrollPaneMessage
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPaneMessage, BorderLayout.SOUTH);

        messageArea = new JTextArea();
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setWrapStyleWord(true);
        messageArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {

                if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    messageArea.append("\n");
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                    e.consume();
                }

            }
        });
        messageArea.setRows(3);
        scrollPaneMessage.setViewportView(messageArea);
        frmChat.getContentPane().add(
                UserListGUI.getInstance().getScrollPaneUserList(),
                "4, 2, fill, fill");

        JPanel panel_1 = new JPanel();
        frmChat.getContentPane().add(panel_1, "2, 4, fill, fill");
        panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent arg0) {
                sendMessage();
            }
        });
        panel_1.add(btnSend);

        lblUUID = new JLabel("NO UUID");
        panel_1.add(lblUUID);

        lblUUID.setText(this.client.getUUID());
    }

    private void showConnectDialog () {
        ConnectDialog dialog = new ConnectDialog();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        this.client = dialog.getClient();
    }

    private void sendMessage () {
        messageArea.setEnabled(false);
        String message = messageArea.getText();

        if (message.isEmpty()) {
            messageArea.setEnabled(true);
            return;
        }

        messageArea.setText("");

        client.sendMessage(message);

        messageArea.setEnabled(true);
        messageArea.requestFocusInWindow();
    }

    private class IncommingListener implements Runnable {
        @Override
        public void run () {
            ChatMessage message;

            try {
                while (client.isRunning()) {
                    byte sentByte = client.getInputStream().readByte();

                    if (sentByte == 1) {
                        message = (ChatMessage) client.getInputStream()
                                .readObject();
                        getChatPane().setText(getChatPane().getText()
                                + "\n"
                                + message);

                        // Auto-scrolls the JTextPane
                        int pos = getChatPane().getText().length();
                        getChatPane().setCaretPosition(pos);
                    }

                    if (sentByte == 2) {
                        UserListGUI.getInstance().updateUserList(
                                ((UserList) client.getInputStream()
                                        .readObject()).getUserList());
                    }
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                if (client.isRunning()) {
                    // TODO: Log this!
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public JLabel getLblUUID () {
        return lblUUID;
    }

    public JButton getBtnSend () {
        return btnSend;
    }

    public void startListening () {
        new Thread(new IncommingListener()).start();
    }
}
