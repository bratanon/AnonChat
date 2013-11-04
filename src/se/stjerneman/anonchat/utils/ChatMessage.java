package se.stjerneman.anonchat.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1829126110767527702L;

    public static final int MESSAGE = 1;
    public static final int SIGNIN = 2;
    public static final int SIGNOUT = 3;
    public static final int SERVER_MESSAGE = 4;

    /**
     * The type of this massage.
     */
    private int type;

    /**
     * The actual message.
     */
    private String message;

    /**
     * A formated time when the message were sent.
     */
    private String time;

    /**
     * The username of the user who sent this message.
     */
    private String username;

    public ChatMessage (String message, String username) {
        this(message, username, MESSAGE);
    }

    public ChatMessage (String message, String username, int type) {
        this.message = message;
        this.username = username;
        this.type = type;
        this.time = new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public int getType () {
        return type;
    }

    @Override
    public String toString () {
        switch (this.type) {
            case SIGNIN:
                return this.formatSignInMessage();
            case SIGNOUT:
                return this.formatSignOutMessage();
            case SERVER_MESSAGE:
                return formatServerMessage();
            case MESSAGE:
                return formatMessage();
            default:
                return "ERROR";
        }
    }

    private String formatSignInMessage () {
        return String.format("[%s] %s joined.",
                this.time, this.username);
    }

    private String formatSignOutMessage () {
        return String.format("[%s] %s disconnected.",
                this.time, this.username);
    }

    private String formatServerMessage () {
        return String.format("[%s] <----SERVER----> %s", this.time,
                this.message);
    }

    private String formatMessage () {
        return String.format("[%s] <%s> %s", this.time, this.username,
                this.message);
    }
}
