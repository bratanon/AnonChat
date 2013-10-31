package se.stjerneman.anonchat.client.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import se.stjerneman.anonchat.client.Client;

public class ConnectDialog extends JDialog {
    private JTextField username;
    private JTextField hostPort;
    private JTextField hostIP;

    public JTextField getUsername () {
        return username;
    }

    public JTextField getHostPort () {
        return hostPort;
    }

    public JTextField getHostIP () {
        return hostIP;
    }

    /**
     * Create the dialog.
     */
    public ConnectDialog () {
        setResizable(false);
        setTitle("Connect to server");
        setBounds(100, 100, 200, 150);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);

        JLabel lblHostIp = new JLabel("Host IP");
        lblHostIp.setBounds(10, 11, 78, 14);
        getContentPane().add(lblHostIp);

        JLabel lblHostPort = new JLabel("Host port");
        lblHostPort.setBounds(10, 36, 78, 14);
        getContentPane().add(lblHostPort);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setLabelFor(username);
        lblUsername.setBounds(10, 61, 78, 14);
        getContentPane().add(lblUsername);

        username = new JTextField();

        username.addFocusListener(new textFieldFocus());
        username.setName("Username");
        username.setBounds(98, 58, 86, 20);
        getContentPane().add(username);
        username.setColumns(10);

        hostPort = new JTextField();
        hostPort.addFocusListener(new textFieldFocus());
        hostPort.setName("Host port");
        hostPort.setText("9999");
        hostPort.setBounds(98, 33, 86, 20);
        getContentPane().add(hostPort);
        hostPort.setColumns(10);

        hostIP = new JTextField();
        hostIP.addFocusListener(new textFieldFocus());
        hostIP.setName("Host IP");
        hostIP.setText("127.0.0.1");
        hostIP.setBounds(98, 8, 86, 20);
        getContentPane().add(hostIP);
        hostIP.setColumns(10);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent arg0) {
                boolean validated = validateTextFields();

                if (validated) {
                    Client client = Client.getInstance();
                    client.setUsername(username.getText());
                    client.startRunning(hostIP.getText(),
                            Integer.parseInt(hostPort.getText()));

                    setVisible(false);
                }
            }
        });
        btnConnect.setBounds(95, 89, 89, 23);
        getContentPane().add(btnConnect);
    }

    private boolean validateTextFields () {
        JTextField[] fields = {
                hostIP,
                hostPort,
                username
        };

        for (JTextField field : fields) {
            if (field.getText().isEmpty()) {
                field.setBackground(new Color(255, 153, 153));

                JOptionPane.showMessageDialog(null,
                        field.getName() + " can't be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private class textFieldFocus extends FocusAdapter {
        @Override
        public void focusGained (FocusEvent e) {
            JTextField field = (JTextField) e.getComponent();
            field.setBackground(new Color(255, 255, 255));
        }
    }
}
