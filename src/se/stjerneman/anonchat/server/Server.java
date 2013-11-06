package se.stjerneman.anonchat.server;

import static se.stjerneman.anonchat.utils.Debug.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import se.stjerneman.anonchat.messages.Message;
import se.stjerneman.anonchat.messages.ServerMessage;
import se.stjerneman.anonchat.messages.SignInMessage;
import se.stjerneman.anonchat.messages.SignOutMessage;
import se.stjerneman.anonchat.utils.UserList;

/**
 * A singleton server class.
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
    private HashMap<String, ClientThread> clients;

    /**
     * A list of connected client threads.
     */
    private HashMap<String, Thread> clientThreads;

    /**
     * Server constructor.
     */
    private Server () {
        this.clients = new HashMap<String, ClientThread>();
        this.clientThreads = new HashMap<String, Thread>();
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
            System.err.println("[ERROR] : Port out of range. Valid range is 1"
                    + "and 65535");
            return;
        }
        this.port = port;
    }

    /**
     * Starts the server.
     */
    public void startServer () {
        if (this.port == 0) {
            System.err.println("[ERROR] : There's no port specified.");
            return;
        }

        debug("[INFO] : Server started");
        this.startListening();

        // Adds the possibility for the server to talk to the connected clients.
        new Thread(new ServerTalk()).start();

        while (true) {
            // Wait for new connections.
            Socket clientSocket = this.receiveConnection();

            if (clientSocket == null) {
                continue;
            }

            String clientUUID = UUID.randomUUID().toString();

            // Start a new ClientThread.
            ClientThread client = new ClientThread(clientUUID, clientSocket);
            Thread clientThread = new Thread(client);

            clientThread.start();
            clientThread.setName("Client " + clientUUID);

            debug("[INFO] : JOIN - " + client.getUsername());

            // Add the client to the list of connected clients.
            this.clients.put(clientUUID, client);
            // Add the client thread to the list of connected client threads.
            this.clientThreads.put(clientUUID, clientThread);

            this.broadcast(new SignInMessage(client.getUsername()), (byte) 1);

            this.broadcastUserList();
        }
    }

    /**
     * Creates a server socket that clients can connection to.
     */
    private void startListening () {
        try {
            this.serverSocket = new ServerSocket(this.port);
            debug("[INFO] : Server listening to port " + this.port);
        }
        catch (IOException | SecurityException e) {
            System.err.println("[ERROR] : Couldn't open server socket.");
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
            String clientHost = clientSocket.getInetAddress().getHostAddress();

            debug("[INFO] : Connection accepted from " + clientHost);
            return clientSocket;
        }
        catch (IOException | SecurityException e) {
            System.err.println("[ERROR] : Receiving connection failed.");
        }

        return null;
    }

    /**
     * Sends an object to all connected clients.
     * 
     * @param object
     *            the object to send to all clients.
     */
    private void broadcast (Object object, byte byteToSend) {
        for (Entry<String, ClientThread> entry : this.clients.entrySet()) {
            entry.getValue().send(object, byteToSend);
        }
    }

    /**
     * Sends a message to all connected clients.
     * 
     * @param message
     *            the message to send to all clients.
     */
    protected void broadcastMessage (Message message) {
        this.broadcast(message, (byte) 1);
    }

    /**
     * Sends a UserList object to all clients.
     */
    // TODO : Rename.
    private void broadcastUserList () {
        UserList userList = new UserList();
        for (Entry<String, ClientThread> entry : this.clients.entrySet()) {
            userList.add(entry.getValue().getUsername());
        }

        this.broadcast(userList, (byte) 2);
    }

    /**
     * Stops and close a client thread.
     * 
     * @param uuid
     *            the client UUID.
     */
    protected void stopClient (String uuid) {
        Thread t;
        ClientThread ct;

        if ((ct = this.clients.get(uuid)) != null) {

            debug("[INFO] : Closing client socket for " + uuid);
            ct.close();
            this.clients.remove(uuid);

            this.broadcast(new SignOutMessage(ct.getUsername()), (byte) 1);
        }

        if ((t = this.clientThreads.get(uuid)) != null) {
            debug("[INFO] : Closing thread for " + uuid);
            try {
                // This thread is IO blocked until something is sent to the
                // client.
                t.join(1000);
            }
            catch (InterruptedException e) {}
            finally {
                this.clientThreads.remove(uuid);
            }
        }

        this.broadcastUserList();
    }

    /**
     * Change the username for a client. disallowed
     * 
     * @param username
     *            the clients current username.
     * @param new_username
     *            the new username for the client.
     * 
     * @return true on success, otherwise false.
     */
    protected boolean changeClientUsername (String username, String new_username) {
        String uuid = this.getUserUUID(username);

        List<String> disallowed = Arrays.asList("server", "admin", "mod");

        boolean isnull = (uuid == null);
        boolean isDisallowed = disallowed.contains(new_username.toLowerCase());
        boolean isSame = (username.toLowerCase().equals(new_username
                .toLowerCase()));

        if (isnull || isDisallowed || isSame) {
            return false;
        }

        for (Entry<String, ClientThread> entry : this.clients.entrySet()) {
            if (entry.getValue().getUsername().toLowerCase()
                    .equals(new_username.toLowerCase())) {
                return false;
            }
        }

        this.clients.get(uuid).changeUserName(new_username);

        return true;
    }

    /**
     * Gets the UUID for the given username.
     * 
     * @param username
     *            the username to act on.
     * 
     * @return the UUID for the given username.
     */
    protected String getUserUUID (String username) {
        for (Entry<String, ClientThread> entry : this.clients.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                return entry.getKey();
            }
        }

        return null;
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
                    System.out.println("MSG 1 :" + message);
                    broadcast(new ServerMessage(message), (byte) 1);
                }
            }
            catch (IOException e) {
                System.err.println("[ERROR] : Server input error.");
            }
        }
    }
}
