package se.stjerneman.anonchat.messages;

public class SignInMessage extends Message {

    private static final long serialVersionUID = -3190856996393798233L;

    public SignInMessage (String username) {
        super(username, "has joined the chat.");
    }

    @Override
    public String formatMessage () {
        // Set format pattern.
        this.getDateFormat().applyLocalizedPattern("HH:mm:ss");

        String time = this.getDateFormat().format(this.getDate());

        return String.format("[%s] * %s %s", time, this.getUsername(),
                this.getText());
    }

}
