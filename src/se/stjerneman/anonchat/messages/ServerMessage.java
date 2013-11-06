package se.stjerneman.anonchat.messages;

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
        return String.format("[%s] * ", this.getTime(), this.getText());
    }
}
