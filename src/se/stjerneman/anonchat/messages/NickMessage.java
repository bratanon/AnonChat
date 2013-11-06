package se.stjerneman.anonchat.messages;

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
}
