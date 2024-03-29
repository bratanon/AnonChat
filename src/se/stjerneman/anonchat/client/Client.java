package se.stjerneman.anonchat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client class.
 * 
 * @author Emil Stjerneman
 * 
 */
public class Client {

    /**
     * A client instance.
     */
    private static Client instance = null;

    /**
     * The host to connect to.
     */
    private String host;

    /**
     * The port the host is listening to.
     */
    private int port;

    /**
     * This client username.
     */
    private String username;

    /**
     * This clients socket.
     */
    private Socket socket;

    /**
     * Stream to receive data from the user.
     */
    private ObjectInputStream input;

    /**
     * Stream to send data to the server.
     */
    private ObjectOutputStream output;

    /**
     * A connection state. True on connected, otherwise false.
     */
    private boolean connected;

    /**
     * Client constructor.
     */
    private Client () {
    }

    /**
     * Gets an instance of the server.
     * 
     * @return an instance of the server.
     */
    public static Client getInstance () {
        if (Client.instance == null) {
            Client.instance = new Client();
        }

        return Client.instance;
    }

    /**
     * Gets the client username.
     * 
     * @return the client username.
     */
    public String getUsername () {
        return username;
    }

    /**
     * Sets the client username.
     * 
     * @param username
     *            the username of the client.
     */
    public void setUsername (String username) {
        this.username = username;
    }

    /**
     * Gets the input stream for this client.
     * 
     * @return the input stream for this client.
     */
    public ObjectInputStream getInputStream () {
        return input;
    }

    /**
     * Gets the output stream for this client.
     * 
     * @return the output stream for this client.
     */
    public ObjectOutputStream getOutputStream () {
        return output;
    }

    /**
     * Gets the connection state.
     * 
     * @return true when the client is connected, otherwise false.
     */
    public boolean isConnected () {
        return connected;
    }

    /**
     * Starts the client.
     * 
     * @param host
     *            the host to connect to.
     * @param port
     *            the port the host is listening to.
     * @throws IOException
     * @throws UnknownHostException
     * @throws NullPointerException
     */
    public void startRunning (String host, int port)
            throws UnknownHostException, IOException, NullPointerException {
        this.host = host;
        this.port = port;

        if (this.username.isEmpty()) {
            throw new NullPointerException("No username given.");
        }

        System.out.println("INFO :  Client started.");

        this.connectToHost();

        this.setupOutputStream();
        this.setupInputStream();

        this.connected = true;

        // Sends username to the server.
        this.output.writeUTF(this.username);
        this.output.flush();
    }

    /**
     * Connects to the host.
     * 
     * @throws Exception
     */
    private void connectToHost () throws UnknownHostException, IOException {
        try {
            this.socket = new Socket(this.host, this.port);
            System.out.println("INFO :  Client connected to host.");
        }
        catch (UnknownHostException e) {
            throw e;
        }
        catch (IOException e) {
            throw e;
        }
    }

    /**
     * Gets a stream to receive data from the client.
     */
    private void setupInputStream () {
        try {
            this.input = new ObjectInputStream(this.socket.getInputStream());
        }
        catch (IOException e) {
            System.err.println("Error getting client input stream.");
            // TODO: Log this!
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets a stream to send data to the server.
     */
    private void setupOutputStream () {
        try {
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
        }
        catch (IOException e) {
            System.err.println("Error getting client output stream.");
            // TODO: Log this!
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
