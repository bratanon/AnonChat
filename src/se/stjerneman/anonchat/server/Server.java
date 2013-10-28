package se.stjerneman.anonchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Emil Stjerneman
 * 
 */
public class Server {

    /**
     * A server instance.
     */
    private static Server instance = null;

    /**
     * The port number this server is listening on for client connections.
     */
    private int port;

    /**
     * This servers socket.
     */
    private ServerSocket serverSocket;

    /**
     * A list of connected client.
     */
    private List<ClientThread> connectedClients = new ArrayList<ClientThread>();

    /**
     * Server constructor.
     */
    private Server () {
    }

    /**
     * Gets an instance of the server.
     * 
     * @return an instance of the server.
     */
    public static Server getInstance () {
        if (Server.instance == null) {
            Server.instance = new Server();
        }

        return Server.instance;
    }

    /**
     * Sets the server port number.
     * 
     * @param port
     *            the port number this server is listening on for client
     *            connections. Valid range is 1 and 65535.
     */
    public void setServerPort (int port) {
        if (port < 1 || port > 65535) {
            System.err.println("Port out of range. Valid range is 1 and 65535");
            return;
        }
        this.port = port;
    }

    /**
     * Starts the server.
     */
    public void startServer () {
        if (this.port == 0) {
            System.err.println("There's no port specified.");
            return;
        }

        System.out.println("INFO : Server started");
        this.startListening();

        new Thread(new ServerTalk()).start();

        while (true) {
            Socket clientSocket = this.receiveConnection();

            ClientThread client = new ClientThread(clientSocket);
            Thread clientThread = new Thread(client);
            this.connectedClients.add(client);
            clientThread.start();

            this.broadcast("<SERVER>", "User <" + client.getUsername()
                    + "> joined.");
        }
    }

    /**
     * Creates a server socket that clients can connection to.
     */
    private void startListening () {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("INFO : Server listening to port " + this.port);
        }
        catch (IOException | SecurityException e) {
            System.err.println("Couldn't open server socket.");
            e.printStackTrace();
        }
    }

    /**
     * Waits for a client connection, then create a new client socket.
     * 
     * @return A client socket.
     */
    private Socket receiveConnection () {
        try {
            Socket clientSocket = this.serverSocket.accept();
            System.out.println("Connection accepted from "
                    + clientSocket.getInetAddress().getHostAddress());
            return clientSocket;
        }
        catch (IOException | SecurityException e) {
            System.err.println("Error on receiving incoming connection.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Send the message to all connected clients.
     * 
     * @param username
     *            the client username that sends the message.
     * @param message
     *            the message from the client to send to all clients.
     */
    protected void broadcast (String username, String message) {
        for (ClientThread client : this.connectedClients) {
            client.sendMessage(this.formatMessage(username, message));
        }
    }

    /**
     * 
     * @param username
     *            the client username that sends the message.
     * @param message
     *            the message from the client to send to all clients.
     * @return a formated message.
     */
    private String formatMessage (String username, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(new Date());

        return String.format("[%s] %s - %s", time, username, message);
    }

    /**
     * Provide the server with the possibility to talk to all clients.
     * 
     * @author Emil Stjerneman
     * 
     */
    private class ServerTalk implements Runnable {

        @Override
        public void run () {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    System.in));

            String message = null;
            try {
                while ((message = input.readLine()) != null) {
                    // TODO: Format message as a object instead.
                    broadcast("SERVER:", message);
                }
            }
            catch (IOException e) {
                System.err.println("Server input error.");
            }
        }
    }

}
