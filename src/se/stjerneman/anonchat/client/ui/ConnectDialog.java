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
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import se.stjerneman.anonchat.client.Client;
import se.stjerneman.anonchat.client.ui.settings.UISettings;
import se.stjerneman.anonchat.client.ui.utils.ApplicationIcons;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ConnectDialog extends JDialog {

    private static final long serialVersionUID = -4911605103198889817L;

    private ClientWindow clientWindow;

    private final JTextField username;
    private final JTextField hostPort;
    private final JTextField hostIP;
    private JCheckBox saveSettings;

    private Properties settings;

    /**
     * Create the dialog.
     */
    public ConnectDialog (ClientWindow cw) {
        setMinimumSize(new Dimension(280, 175));

        settings = UISettings.getInstance().getSettings();

        this.clientWindow = cw;
        setIconImages(ApplicationIcons.getIcons());
        setTitle("Connect to server");
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
                FormFactory.RELATED_GAP_COLSPEC, },
                new RowSpec[] {
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.LINE_GAP_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.LINE_GAP_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("23px"),
                        FormFactory.RELATED_GAP_ROWSPEC, }));
        username.setName("Username");
        getContentPane().add(username, "4, 6, fill, fill");
        username.setColumns(10);
        username.setText(settings.getProperty("username"));

        hostPort = new JTextField();
        hostPort.addFocusListener(new textFieldFocus());
        hostPort.setName("Host port");
        hostPort.setText(settings.getProperty("hostPort"));
        getContentPane().add(hostPort, "4, 4, fill, fill");
        hostPort.setColumns(10);

        hostIP = new JTextField();
        hostIP.addFocusListener(new textFieldFocus());
        hostIP.setName("Host IP");

        hostIP.setText(settings.getProperty("hostIP"));
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
                if (getSaveSettings().isSelected()) {
                    if (!saveConnectionDetails()) {
                        JOptionPane.showMessageDialog(null,
                                "Couldn't save the settings.properties file.",
                                "Error saving settings.",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (validateTextFields() && establishConnection()) {
                    dispose();
                }
            }
        });

        saveSettings = new JCheckBox("Set these settings as default.");
        getContentPane().add(saveSettings, "4, 8");
        getContentPane().add(btnConnect, "4, 10, right, bottom");

    }

    public int getHostPort () {
        return Integer.parseInt(hostPort.getText().trim());
    }

    public String getHostIP () {
        return hostIP.getText().trim();
    }

    public String getUsername () {
        return username.getText().trim();
    }

    public JCheckBox getSaveSettings () {
        return saveSettings;
    }

    /**
     * Try establish a connection to the host.
     * 
     * @return true when connected successfully, otherwise false.
     */
    private boolean establishConnection () {
        Client client = Client.getInstance();

        // TODO: Fix username validation.
        client.setUsername(getUsername());

        try {
            client.startRunning(getHostIP(), getHostPort());

            clientWindow.client = client;
            clientWindow.startListen();
            return true;
        }
        catch (UnknownHostException e) {
            String host = getHostIP() + ":" + getHostPort();
            showErrorMessage("Could't find the host (" + host + ").");
        }
        catch (IOException e) {
            showErrorMessage("Error when connecting to host.");
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
            showErrorMessage("No username given.");
        }
        catch (IllegalArgumentException e) {
            showErrorMessage("The username is invalid or already in use.");
        }

        return false;
    }

    /**
     * Validate text fields.
     * 
     * @return true if all fields validated, otherwise false.
     */
    private boolean validateTextFields () {
        JTextField[] fields = {
                hostIP,
                hostPort,
                username
        };

        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
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

    private boolean saveConnectionDetails () {
        settings.setProperty("hostIP", getHostIP());
        settings.setProperty("hostPort", Integer.toString(getHostPort()));
        settings.setProperty("username", getUsername());

        return UISettings.saveSettings();
    }
}
