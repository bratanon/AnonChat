package se.stjerneman.anonchat.messages;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class NickMessage extends CommandMessage {

    private static final long serialVersionUID = -4606162196775720390L;

    /**
     * CommandMessage constructor.
     * 
     * @param actorName
     *            the actors username.
     * @param targetName
     *            the targets username.
     */
    public NickMessage (String actorName, String targetName) {
        super(actorName, null, targetName);
    }

    @Override
    public String formatMessage () {
        return String.format("[%s] %s is now known as %s", this.getTime(),
                this.getActorName(), this.getTargetName());
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
        StyleConstants.setForeground(style, new Color(0, 147, 0));

        return style;
    }
}
