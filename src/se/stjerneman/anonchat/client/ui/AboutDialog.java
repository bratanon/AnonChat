package se.stjerneman.anonchat.client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AboutDialog extends JDialog {
    /**
     * @wbp.nonvisual location=441,-31
     */
    private final JLabel label = new JLabel("New label");

    /**
     * Launch the application.
     */
    public static void main (String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run () {
                try {
                    AboutDialog dialog = new AboutDialog();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the dialog.
     */
    public AboutDialog () {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setAlwaysOnTop(true);
        setIconImage(Toolkit
                .getDefaultToolkit()
                .getImage(
                        AboutDialog.class
                                .getResource("/se/stjerneman/anonchat/client/ui/icons/chat.png")));
        setResizable(false);
        setTitle("About Chat");
        setBounds(100, 100, 624, 448);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("364px:grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("167px:grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, },
                new RowSpec[] {
                        FormFactory.LINE_GAP_ROWSPEC,
                        RowSpec.decode("147px"),
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"),
                        FormFactory.RELATED_GAP_ROWSPEC, }));

        JTextPane textInfo = new JTextPane();
        textInfo.setEditable(false);
        textInfo.setFocusable(false);
        textInfo.setBackground(UIManager.getColor("ScrollPane.background"));
        textInfo.setText("GBJU13 Chat v1.0\r\n\r\nBuilt for the higher grade in the GUI course.\r\n\r\nCopyright \u00A9 2013 Emil Stjerneman <emil@stjerneman.com>\r\n\r\nThis work is free. You can redistribute it and/or modify it under the\r\nterms of the Do What The Fuck You Want To Public License, Version 2,\r\nas published by Sam Hocevar. See http://www.wtfpl.net/ for more details.");
        panel.add(textInfo, "3, 2, fill, top");

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel
                .setIcon(new ImageIcon(
                        AboutDialog.class
                                .getResource("/se/stjerneman/anonchat/client/ui/icons/chat.png")));
        panel.add(lblNewLabel, "5, 2, right, top");

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, "3, 4, 3, 1, fill, fill");

        JTextArea txtrDoWhatThe = new JTextArea();
        txtrDoWhatThe.setEditable(false);
        txtrDoWhatThe
                .setText("            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE\r\n                    Version 2, December 2004\r\n\r\n Copyright (C) 2004 Sam Hocevar <sam@hocevar.net>\r\n\r\n Everyone is permitted to copy and distribute verbatim or modified\r\n copies of this license document, and changing it is allowed as long\r\n as the name is changed.\r\n\r\n            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE\r\n   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION\r\n\r\n  0. You just DO WHAT THE FUCK YOU WANT TO.");
        scrollPane.setViewportView(txtrDoWhatThe);

    }

}
