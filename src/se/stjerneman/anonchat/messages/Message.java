package se.stjerneman.anonchat.messages;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base class for messages.
 * 
 * @author Emil Stjerneman
 * 
 */
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 335428924161138563L;

    /**
     * The username that is sending this message.
     */
    private String username;

    /**
     * The actual message text.
     */
    private String text;

    /**
     * Date when this message were sent.
     */
    private Date date;

    /**
     * A SimpleDateFormat object that will be used when formating messages.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    /**
     * Message constructor.
     * 
     * @param text
     *            the message text.
     */
    public Message (String text) {
        this(null, text);
    }

    /**
     * Message constructor.
     * 
     * @param username
     *            the username that is sending this message.
     * @param text
     *            the message text.
     */
    public Message (String username, String text) {
        this.username = username;
        this.text = text;
        this.date = new Date();
    }

    /**
     * Gets the username who is sending this message.
     * 
     * @return the username who is sending this message.
     */
    public String getUsername () {
        return username;
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
     * Gets the date when this message were sent.
     * 
     * @return the date when this message were sent.
     */
    public Date getDate () {
        return date;
    }

    /**
     * Gets the SimpleDateFormat object.
     * 
     * @return the SimpleDateFormat object for this message.
     */
    public SimpleDateFormat getDateFormat () {
        return dateFormat;
    }

    /**
     * Formats a message into a nice string.
     * 
     * @return a nice formated message.
     */
    public abstract String formatMessage ();
}
