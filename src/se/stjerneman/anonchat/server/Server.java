package se.stjerneman.anonchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import se.stjerneman.anonchat.utils.ChatMessage;

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

            System.out.println("[DEBUG] : JOIN - " + client.getUsername());
            // Send login notification.
            this.broadcast(new ChatMessage(null, client
                    .getUsername(), ChatMessage.SIGNIN), (byte) 1);

            this.sendUserList();
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
     * @param message
     *            the message from the client to send to all clients.
     */
    protected void broadcast (Object object, byte byteToSend) {
        for (ClientThread client : this.connectedClients) {
            client.send(object, byteToSend);
        }
    }

    private void sendUserList () {
        UserList userList = new UserList();
        for (ClientThread client : this.connectedClients) {
            userList.add(client.getUsername());
        }

        this.broadcast(userList, (byte) 2);
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
                    broadcast(new ChatMessage(message, null,
                            ChatMessage.SERVER_MESSAGE), (byte) 1);
                }
            }
            catch (IOException e) {
                System.err.println("Server input error.");
            }
        }
    }
}
