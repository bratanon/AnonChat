package se.stjerneman.anonchat.messages;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Handles "/me" messages.
 * 
 * @author Emil Stjerneman
 * 
 */
public class MeMessage extends CommandMessage {

    private static final long serialVersionUID = -7295808536239003220L;

    /**
     * MeMessage constructor.
     * 
     * @param actorName
     *            the actors username.
     * @param text
     *            the message text.
     */
    public MeMessage (String actorName, String text) {
        super(actorName, text);
    }

    @Override
    public String formatMessage () {
        return String.format("[%s] %s %s", this.getTime(), this.getActorName(),
                this.getText());
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
        StyleConstants.setForeground(style, new Color(156, 0, 156));

        return style;
    }

}
