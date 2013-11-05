package se.stjerneman.anonchat.messages;

public class RegularMessage extends Message {

    private static final long serialVersionUID = 4821167512462886782L;

    public RegularMessage (String username, String text) {
        super(username, text);
    }

    @Override
    public String formatMessage () {
        // Set format pattern.
        this.getDateFormat().applyLocalizedPattern("HH:mm:ss");

        String time = this.getDateFormat().format(this.getDate());

        return String.format("[%s] <%s> %s", time, this.getUsername(),
                this.getText());
    }

}
