package se.stjerneman.anonchat.messages;

/**
 * Handles join messages sent form the server.
 * 
 * @author Emil Stjerneman
 * 
 */
public class SignInMessage extends Message {

    private static final long serialVersionUID = -3190856996393798233L;

    /**
     * SignInMessage Constructor.
     * 
     * @param actorName
     *            the actors username.
     */
    public SignInMessage (String actorName) {
        super(actorName, null);
    }

    @Override
    public String formatMessage () {
        return String.format("[%s] * %s has joined.", this.getTime(),
                this.getActorName());
    }
}
