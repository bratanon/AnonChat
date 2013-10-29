package se.stjerneman.anonchat.client.ui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ConnectDialog extends JDialog {
    private JTextField textField_1;
    private JTextField textField_2;

    /**
     * Launch the application.
     */
    public static void main (String[] args) {
        try {
            ConnectDialog dialog = new ConnectDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ConnectDialog () {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(null);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setBounds(0, 229, 434, 33);
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane);
            {
                JButton okButton = new JButton("Connect");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }

        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setText("127.0.0.1");
        textField.setBounds(196, 70, 86, 20);
        getContentPane().add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setText("666");
        textField_1.setBounds(196, 101, 86, 20);
        textField_1.setHorizontalAlignment(JTextField.RIGHT);
        getContentPane().add(textField_1);
        textField_1.setColumns(10);

        JLabel lblNewLabel = new JLabel("Host IP");
        lblNewLabel.setBounds(140, 73, 46, 14);
        getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Host port");
        lblNewLabel_1.setBounds(140, 104, 46, 14);
        getContentPane().add(lblNewLabel_1);

        textField_2 = new JTextField();
        textField_2.setBounds(140, 159, 142, 20);
        getContentPane().add(textField_2);
        textField_2.setColumns(10);
    }
}
