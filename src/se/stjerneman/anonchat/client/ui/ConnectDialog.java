package se.stjerneman.anonchat.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import se.stjerneman.anonchat.client.Client;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ConnectDialog extends JDialog {

    private static final long serialVersionUID = -4911605103198889817L;

    private final JTextField username;
    private final JTextField hostPort;
    private final JTextField hostIP;

    private Client client;

    /**
     * Create the dialog.
     */
    public ConnectDialog () {
        setIconImages(ApplicationIcons.getIcons());
        setMinimumSize(new Dimension(280, 150));
        setTitle("Connect to server");
        setBounds(100, 100, 280, 150);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent ev) {
                System.exit(0);
            }
        });

        username = new JTextField();

        username.addFocusListener(new textFieldFocus());
        getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("78px"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("89px:grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, },
                new RowSpec[] {
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.LINE_GAP_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.LINE_GAP_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        RowSpec.decode("23px"), }));
        username.setName("Username");
        getContentPane().add(username, "4, 6, fill, fill");
        username.setColumns(10);

        hostPort = new JTextField();
        hostPort.addFocusListener(new textFieldFocus());
        hostPort.setName("Host port");
        hostPort.setText("6789");
        getContentPane().add(hostPort, "4, 4, fill, fill");
        hostPort.setColumns(10);

        hostIP = new JTextField();
        hostIP.addFocusListener(new textFieldFocus());
        hostIP.setName("Host IP");
        hostIP.setText("127.0.0.1");
        getContentPane().add(hostIP, "4, 2, fill, fill");
        hostIP.setColumns(10);

        JLabel lblHostIp = new JLabel("Host IP");
        getContentPane().add(lblHostIp, "2, 2, fill, center");

        JLabel lblHostPort = new JLabel("Host port");
        getContentPane().add(lblHostPort, "2, 4, fill, center");

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setLabelFor(username);
        getContentPane().add(lblUsername, "2, 6, fill, center");

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (validateTextFields() && establishConnection()) {
                    dispose();
                }
            }
        });
        getContentPane().add(btnConnect, "4, 8, right, bottom");
    }

    public String getUsername () {
        return username.getText().trim();
    }

    public int getHostPort () {
        return Integer.parseInt(hostPort.getText().trim());
    }

    public String getHostIP () {
        return hostIP.getText().trim();
    }

    public Client getClient () {
        return client;
    }

    private boolean establishConnection () {
        this.client = Client.getInstance();
        this.client.setUsername(this.getUsername());

        try {
            this.client.startRunning(this.getHostIP(), this.getHostPort());
            return true;
        }
        catch (UnknownHostException e) {
            String host = this.getHostIP() + ":" + this.getHostPort();
            this.showErrorMessage("Could't find the host (" + host + ").");
            return false;
        }
        catch (IOException e) {
            this.showErrorMessage("Error when connecting to host.");
            return false;
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
            this.showErrorMessage("No username given.");
            return false;
        }
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

                JOptionPane.showMessageDialog(this,
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

    /**
     * Shows an error message to the user.
     * 
     * @param text
     *            the message text.
     */
    private void showErrorMessage (String text) {
        JOptionPane.showMessageDialog(this, text, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}
