package se.stjerneman.anonchat.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class AboutDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 5576803747588972025L;

    /**
     * Create the dialog.
     */
    public AboutDialog () {
        setIconImages(ApplicationIcons.getIcons());
        setModalityType(ModalityType.APPLICATION_MODAL);

        setResizable(false);
        setTitle("About AnonChat");
        setBounds(100, 100, 267, 231);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblTitle = new JLabel("AnonChat");
        lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblTitle.setForeground(new Color(0, 0, 128));
        lblTitle.setBounds(104, 11, 129, 37);
        contentPanel.add(lblTitle);

        JLabel lblImage = new JLabel("");
        lblImage.setIcon(new ImageIcon(
                AboutDialog.class
                        .getResource("/se/stjerneman/anonchat/client/ui/icons/chatIcon64.png")));
        lblImage.setBounds(10, 11, 64, 64);
        contentPanel.add(lblImage);

        JLabel lblCopywrite = new JLabel(
                "Copyright \u00A9 2013 Emil Stjerneman\r\n");
        lblCopywrite.setBounds(47, 94, 166, 14);
        contentPanel.add(lblCopywrite);

        JLabel lblVersion = new JLabel("version 1.0");
        lblVersion.setForeground(Color.GRAY);
        lblVersion.setBounds(179, 49, 54, 14);
        contentPanel.add(lblVersion);

        JLabel lblBuiltForThe = new JLabel(
                "Built for the higher grade in the GUI course.");
        lblBuiltForThe.setForeground(Color.LIGHT_GRAY);
        lblBuiltForThe.setBounds(23, 119, 210, 14);
        contentPanel.add(lblBuiltForThe);

        JPanel buttonPane = new JPanel();
        buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnClose);

    }
}
