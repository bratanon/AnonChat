package se.stjerneman.anonchat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import se.stjerneman.anonchat.messages.Message;

/**
 * Client thread.
 * 
 * @author Emil Stjerneman
 * 
 */
class ClientThread implements Runnable {

    /**
     * A server instance.
     */
    private Server server = Server.getInstance();

    /**
     * This client universally unique identifier.
     */
    private final String uuid;

    /**
     * This client socket.
     */
    private Socket clientSocket;

    /**
     * Stream to receive data from the client.
     */
    private ObjectInputStream input;

    /**
     * Stream to send data to the client.
     */
    private ObjectOutputStream output;

    /**
     * This client username.
     */
    private String username;

    /**
     * Client thread constructor.
     * 
     * @param clientSocket
     *            this client socket.
     */
    public ClientThread (String clientUUID, Socket clientSocket) {
        this.uuid = clientUUID;
        this.clientSocket = clientSocket;

        // Setup streams, for some reason we have to setup the output first,
        // else it will thrown an exception.
        this.setupOutputStream();
        this.setupInputStream();

        // Get the username from the client.
        try {
            this.username = (String) this.input.readObject();
        }
        catch (IOException e) {
            System.err.println("[ERROR] Couldn't get the username from the"
                    + "client.");
            this.server.stopClient(this.uuid);
        }
        catch (ClassNotFoundException e) {
            // Will never happen as String is in the java core.
        }

        // Send the UUID to the client.
        try {
            this.output.writeObject(this.uuid);
        }
        catch (IOException e) {
            System.err.println("[ERROR] Couldn't send the uuid to the client.");
            this.server.stopClient(this.uuid);
        }

    }

    /**
     * Gets this client's username.
     * 
     * @return this client's username.
     */
    public String getUsername () {
        return this.username;
    }

    /**
     * Runs the thread and wait for client input.
     */
    public void run () {
        try {
            while (!this.clientSocket.isInputShutdown()) {
                String text = (String) this.input.readObject();

                if (Command.isCommand(text)) {
                    Command.process(text, this.username);
                    continue;
                }

                this.server.broadcastMessage(new Message(this.username, text));
            }
        }
        catch (ClassNotFoundException e) {
            // Will never happen as String is in the java core.
        }
        catch (SocketException e) {
            this.server.stopClient(this.uuid);
        }
        catch (IOException e) {
            // Client probably disconnected.
        }
        finally {
            this.server.stopClient(this.uuid);
        }
    }

    /**
     * Gets a stream to send data to the client.
     */
    private void setupOutputStream () {
        try {
            this.output = new ObjectOutputStream(
                    this.clientSocket.getOutputStream());
        }
        catch (IOException e) {
            System.err.println("[ERROR] Error getting client output stream.");
            this.server.stopClient(this.uuid);
        }
    }

    /**
     * Gets a stream to receive data from the client.
     */
    private void setupInputStream () {
        try {
            this.input = new ObjectInputStream(
                    this.clientSocket.getInputStream());
        }
        catch (IOException e) {
            System.err.println("[ERROR] Error getting client input stream.");
            this.server.stopClient(this.uuid);
        }
    }

    /**
     * Writes a message to the client output stream.
     * 
     * @param message
     *            a message to send to the client.
     */
    protected void send (Object object, byte byteToSend) {
        try {
            this.output.flush();
            this.output.writeByte(byteToSend);
            this.output.flush();
            this.output.writeObject(object);
        }
        catch (IOException e) {
            System.err.println("[ERROR] IOException when writing to client.");
            this.server.stopClient(this.uuid);
        }
    }

    /**
     * Changes the username for this client.
     * 
     * @param new_username
     *            the new username.
     */
    protected void changeUserName (String new_username) {
        this.username = new_username;
    }

    /**
     * Closes this clients socket and the streams.
     */
    protected void close () {
        try {
            this.clientSocket.close();
        }
        catch (IOException e) {
            // Don't handle this.
        }
    }
}
