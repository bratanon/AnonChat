package se.stjerneman.anonchat.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

import se.stjerneman.anonchat.client.ui.utils.ApplicationIcons;

public class LicensDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -1902437907166805308L;

    /**
     * Create the dialog.
     */
    public LicensDialog () {
        setIconImages(ApplicationIcons.getIcons());
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Licens");
        setBounds(100, 100, 605, 343);
        getContentPane().setLayout(new BorderLayout());
        {
            JPanel buttonPane = new JPanel();
            buttonPane
                    .setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed (ActionEvent arg0) {
                        dispose();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
        {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane
                    .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            {
                JTextPane txtpnDoWhatThe = new JTextPane();
                txtpnDoWhatThe.setFont(UIManager.getFont("TextArea.font"));
                txtpnDoWhatThe
                        .setText("            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE\r\n                    Version 2, December 2004\r\n\r\n Copyright (C) 2004 Sam Hocevar <sam@hocevar.net>\r\n\r\n Everyone is permitted to copy and distribute verbatim or modified\r\n copies of this license document, and changing it is allowed as long\r\n as the name is changed.\r\n\r\n            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE\r\n   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION\r\n\r\n  0. You just DO WHAT THE FUCK YOU WANT TO.");
                scrollPane.setViewportView(txtpnDoWhatThe);
            }
        }
    }

}
