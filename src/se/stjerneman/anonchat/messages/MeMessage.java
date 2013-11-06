package se.stjerneman.anonchat.messages;

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

}
