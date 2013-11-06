package se.stjerneman.anonchat.messages;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Emil Stjerneman
 * 
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 335428924161138563L;

    /**
     * The actors username.
     */
    private String actorName;

    /**
     * The actual message text.
     */
    private String text;

    /**
     * Timestamp when this message were sent.
     */
    private String time;

    /**
     * A SimpleDateFormat object that will be used when formating messages.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    /**
     * Message constructor.
     * 
     * @param actorName
     *            the actors username.
     * @param text
     *            the message text.
     */
    public Message (String actorName, String text) {
        this.actorName = actorName;
        this.text = text;

        this.dateFormat.applyLocalizedPattern("HH:mm:ss");
        this.time = this.dateFormat.format(new Date());
    }

    /**
     * Gets the actor who is sending this message.
     * 
     * @return the actor who is sending this message.
     */
    public String getActorName () {
        return actorName;
    }

    /**
     * Gets the message text.
     * 
     * @return the message text.
     */
    public String getText () {
        return text;
    }

    /**
     * Gets the timestamp when this message were sent.
     * 
     * @return the timestamp when this message were sent.
     */
    public String getTime () {
        return time;
    }

    /**
     * Formats a message into a nice string.
     * 
     * @return a nice formated message.
     */
    public String formatMessage () {
        return String.format("[%s] <%s> %s", this.getTime(),
                this.getActorName(), this.getText());
    }

}
