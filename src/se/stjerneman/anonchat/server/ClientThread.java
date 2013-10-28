package se.stjerneman.anonchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

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
    private BufferedReader input;

    /**
     * Stream to send data to the client.
     */
    private PrintWriter output;

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

        this.setupInputStream();
        this.setupOutputStream();

        try {
            this.username = this.input.readLine();
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
     * Runs the thread.
     */
    public void run () {
        try {
            String message = null;
            while ((message = this.input.readLine().trim()) != null) {
                this.server.broadcast(username, message);
            }
        }
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
            this.input = new BufferedReader(new InputStreamReader(
                    this.clientSocket.getInputStream()));
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
            this.output = new PrintWriter(this.clientSocket.getOutputStream());
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
    protected void sendMessage (String message) {
        // Tests if the client still is connected.
        if (this.clientSocket.isClosed()) {
            // TODO: Remove sockets and close everything.
            return;
        }

        this.output.println(message);
        this.output.flush();
    }

}
