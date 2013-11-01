package se.stjerneman.anonchat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import se.stjerneman.anonchat.utils.ChatMessage;

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
    public ClientThread (Socket clientSocket) {
        this.clientSocket = clientSocket;

        this.setupOutputStream();
        this.setupInputStream();

        try {
            this.username = this.input.readUTF();
        }
        catch (IOException e) {
            e.printStackTrace();
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
            // TODO: Handle bytes here..
            while (!this.clientSocket.isInputShutdown()) {
                byte sentByte = this.input.readByte();

                // Common message.
                if (sentByte == 1) {
                    String message = (String) this.input.readObject();
                    ChatMessage cm = new ChatMessage(message, this.username);
                    this.server.broadcast(cm, (byte) 1);
                }

            }
        }
        catch (ClassNotFoundException e) {}
        catch (SocketException e) {
            // TODO: Close this tread and all streams to it.
            return;
        }
        catch (IOException e) {
            System.err.println("Error when sending message.");
            e.printStackTrace();
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
            System.err.println("Error getting client input stream.");
            e.printStackTrace();
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
            System.err.println("Error getting client output stream.");
            e.printStackTrace();
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
            this.output.writeByte(byteToSend);

            this.output.writeObject(object);

        }
        catch (IOException e) {
            try {
                this.clientSocket.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
