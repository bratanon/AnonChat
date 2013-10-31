package se.stjerneman.anonchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
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
     * Stream to receive data from the client.
     */
    private BufferedReader input;

    /**
     * Stream to send data to the server.
     */
    private PrintWriter output;

    /**
     * Client constructor.
     */
    private Client () {
    }

    /**
     * Gets the input stream for this client.
     * 
     * @return the input stream for this client.
     */
    public BufferedReader getInputStream () {
        return input;
    }

    /**
     * Gets the output stream for this client.
     * 
     * @return the output stream for this client.
     */
    public PrintWriter getOutputStream () {
        return output;
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
     * Starts the client.
     * 
     * @param host
     *            the host to connect to.
     * @param port
     *            the port the host is listening to.
     */
    public void startRunning (String host, int port) {
        this.host = host;
        this.port = port;

        if (this.username.isEmpty()) {
            System.err.println("No username given.");
            return;
        }

        System.out.println("INFO :  Client started.");
        this.connectToHost();
        this.setupInputStream();
        this.setupOutputStream();

        // Sends username to the server.
        this.output.println(this.username);
        this.output.flush();
    }

    /**
     * Connects to the host.
     */
    private void connectToHost () {
        try {
            this.socket = new Socket(this.host, this.port);
            System.out.println("INFO :  Client connected to host.");
        }
        catch (UnknownHostException e) {
            String host = this.host + ":" + this.port;
            System.err.println("Could't find the host (" + host + ").");
            // TODO: Log this!
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e) {
            System.err.println("Error when connecting to host.");
            // TODO: Log this!
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets a stream to receive data from the client.
     */
    private void setupInputStream () {
        try {
            this.input = new BufferedReader(new InputStreamReader(
                    this.socket.getInputStream()));
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
            this.output = new PrintWriter(this.socket.getOutputStream());
        }
        catch (IOException e) {
            System.err.println("Error getting client output stream.");
            // TODO: Log this!
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    // TODO: Is this needed when we add a GUI?
    private class MessageListener implements Runnable {

        @Override
        public void run () {
            String message;
            try {
                while ((message = getInputStream().readLine()) != null) {
                    System.out.println(message);
                }
            }
            catch (SocketException e) {
                // TODO: Log this!
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO: Log this!
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
