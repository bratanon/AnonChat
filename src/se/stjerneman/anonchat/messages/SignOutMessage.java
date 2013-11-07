package se.stjerneman.anonchat.messages;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Handles join messages sent form the server.
 * 
 * @author Emil Stjerneman
 * 
 */
public class SignOutMessage extends Message {

    private static final long serialVersionUID = 4821167512462886782L;

    /**
     * SignInMessage Constructor.
     * 
     * @param actorName
     *            the actors username.
     */
    public SignOutMessage (String actorName) {
        super(actorName, null);
    }

    @Override
    public String formatMessage () {
        return String.format("[%s] * %s has disconnected.", this.getTime(),
                this.getActorName());
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
        StyleConstants.setForeground(style, new Color(0, 0, 127));

        return style;
    }
}
