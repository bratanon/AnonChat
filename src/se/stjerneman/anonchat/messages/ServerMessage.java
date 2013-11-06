package se.stjerneman.anonchat.messages;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Handles regular messages sent from the server.
 * 
 * @author Emil Stjerneman
 * 
 */
public class ServerMessage extends Message {

    private static final long serialVersionUID = 8160856092267344980L;

    /**
     * ServerMessage constructor.
     * 
     * @param text
     *            the message text.
     */
    public ServerMessage (String text) {
        super(null, text);
    }

    @Override
    public String formatMessage () {
        return String.format("[%s] * %s ", getTime(), getText());
    }

    @Override
    public Style getStyle (StyledDocument doc) {
        Style style = doc.getStyle(this.getClass().getName());
        return (style != null) ? style : setStyle(doc);
    }

    @Override
    public Style setStyle (StyledDocument doc) {
        Style style = doc.addStyle(this.getClass().getName(), null);

        StyleConstants.setBold(style, true);
        StyleConstants.setForeground(style, new Color(0, 181, 173));

        return style;
    }
}
