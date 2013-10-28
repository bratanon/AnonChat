package se.stjerneman.anonchat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {
    public static final int MESSAGE = 1;
    public static final int SIGNIN = 50;
    public static final int SIGNOUT = 100;
    public static final int SERVER_MESSAGE = 1000;

    private int type;

    private String message;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private String time;

    public ChatMessage (int type, String message) {
        this.setType(type);
        this.setMessage(message);
        this.time = dateFormat.format(new Date());
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}
