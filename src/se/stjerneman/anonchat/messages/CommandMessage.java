package se.stjerneman.anonchat.messages;

/**
 * 
 * @author Emil Stjerneman
 * 
 */
public abstract class CommandMessage extends Message {

    private static final long serialVersionUID = -6944814022284452941L;

    /**
     * The targets username.
     */
    private String targetName;

    /**
     * CommandMessage constructor.
     * 
     * @param actorName
     *            the actors username.
     * @param text
     *            the message text.
     */
    public CommandMessage (String actorName, String text) {
        this(actorName, text, actorName);
    }

    /**
     * CommandMessage constructor.
     * 
     * @param actorName
     *            the actors username.
     * @param text
     *            the message text.
     * @param targetName
     *            the targets username.
     */
    public CommandMessage (String actorName, String text, String targetName) {
        super(actorName, text);
        this.targetName = targetName;
    }

    /**
     * Gets the targets username.
     * 
     * @return the targets username.
     */
    public String getTargetName () {
        return targetName;
    }
}
