package se.stjerneman.anonchat.messages;

public class ServerMessage extends Message {

    private static final long serialVersionUID = 8160856092267344980L;

    public ServerMessage (String text) {
        super(text);
    }

    @Override
    public String formatMessage () {
        // Set format pattern.
        this.getDateFormat().applyLocalizedPattern("HH:mm:ss");

        String time = this.getDateFormat().format(this.getDate());

        return String.format("[%s] * %s", time, this.getText());
    }
}
