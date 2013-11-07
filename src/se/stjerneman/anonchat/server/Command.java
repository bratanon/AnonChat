package se.stjerneman.anonchat.server;

import java.util.Arrays;
import java.util.List;

import se.stjerneman.anonchat.messages.MeMessage;
import se.stjerneman.anonchat.messages.NickMessage;

public class Command {

    /**
     * A list of valid commands.
     */
    private static List<String> commands = Arrays.asList("me", "nick");

    /**
     * Process a command.
     * 
     * @param text
     *            the text send from the client.
     * 
     * @param username
     *            the username of the client that sent the text.
     */
    public static void process (String text, String username) {
        if (!Command.isCommand(text)) {
            return;
        }

        String command = Command.getCommandFromString(text);

        switch (command) {
            case "me":
                Command.performMe(text, username);
                break;
            case "nick":
                Command.performNick(text, username);
                break;
        }
    }

    /**
     * Perform the me command.
     * 
     * @param text
     *            the text send from the client.
     * 
     * @param username
     *            the username of the client that sent the text.
     */
    private static void performMe (String text, String username) {
        text = text.replaceFirst("/me", "").trim();

        if (text.length() == 0) {
            return;
        }

        Server.getInstance().broadcastMessage(new MeMessage(username, text));
    }

    /**
     * Perform the nick command.
     * 
     * @param text
     *            the text send from the client.
     * 
     * @param username
     *            the username of the client that sent the text.
     */
    private static void performNick (String text, String username) {
        String[] splittedText = text.split("\\s");

        if (splittedText.length < 2 || splittedText[1].isEmpty()) {
            return;
        }
        String new_username = splittedText[1].trim();

        Server server = Server.getInstance();
        if (server.changeClientUsername(username.trim(), new_username)) {
            System.out.println("[INFO]: " + username + " is now known as "
                    + new_username);
            server.broadcastMessage(new NickMessage(username, new_username));
        }
    }

    /**
     * Gets the command from a string.
     * 
     * @param text
     *            the text that contains the command.
     * 
     * @return the extracted command string.
     */
    private static String getCommandFromString (String text) {
        String[] splittedText = text.split("\\s");
        return splittedText[0].substring(1);
    }

    /**
     * Tests if a text string is a command.
     * 
     * @param text
     *            the text from the client.
     * 
     * @return true when the text is a command, otherwise false.
     */
    public static boolean isCommand (String text) {
        String command = Command.getCommandFromString(text);

        if (text.startsWith("/") && commands.contains(command)) {
            return true;
        }

        return false;
    }
}
