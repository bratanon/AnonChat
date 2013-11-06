package se.stjerneman.anonchat.messages;

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
}
