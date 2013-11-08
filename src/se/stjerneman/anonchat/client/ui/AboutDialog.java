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

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

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
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("About AnonChat");
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        getContentPane().add(contentPanel);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(56dlu;default)"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(97dlu;default)"),
                FormFactory.RELATED_GAP_COLSPEC, },
                new RowSpec[] {
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("fill:default"),
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("fill:max(11dlu;default)"),
                        RowSpec.decode("20dlu"),
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, }));

        JLabel image = new JLabel("");
        image.setIcon(new ImageIcon(
                AboutDialog.class
                        .getResource("/se/stjerneman/anonchat/client/ui/icons/chatIcon64.png")));
        contentPanel.add(image, "2, 2, 2, 4, left, top");

        JLabel lblAnonchat = new JLabel("AnonChat");
        lblAnonchat.setFont(new Font("Tahoma", Font.BOLD, 22));
        contentPanel.add(lblAnonchat, "4, 2, right, default");

        JLabel lblNewLabel = new JLabel("version 1.0");
        lblNewLabel.setForeground(Color.GRAY);
        contentPanel.add(lblNewLabel, "4, 4, right, default");

        JLabel lblCopywrite = new JLabel("Copyright © 2013 Emil Stjerneman");
        contentPanel.add(lblCopywrite, "2, 6, 3, 1, center, default");

        JLabel lblBuiltForThe = new JLabel(
                "Built for the higher grade in the GUI course.");
        lblBuiltForThe.setForeground(Color.GRAY);
        contentPanel.add(lblBuiltForThe, "2, 8, 3, 1, center, default");

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

        pack();
    }
}
