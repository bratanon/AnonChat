package se.stjerneman.anonchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
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
    private HashMap<String, ClientThread> connectedClients;

    /**
     * A list of connected client threads.
     */
    private HashMap<String, Thread> connectedClientsThreads;

    /**
     * Server constructor.
     */
    private Server () {
        this.connectedClients = new HashMap<String, ClientThread>();
        this.connectedClientsThreads = new HashMap<String, Thread>();
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
            // TODO: Log this.
            System.err
                    .println("[ERROR] Port out of range. Valid range is 1 and 65535");
            return;
        }
        this.port = port;
    }

    /**
     * Starts the server.
     */
    public void startServer () {
        if (this.port == 0) {
            // TODO: Log this.
            System.err.println("[ERROR] There's no port specified.");
            return;
        }
        // TODO: Log this.
        System.out.println("[INFO] : Server started");
        this.startListening();

        // Adds the possibility for the server to talk to the connected clients.
        new Thread(new ServerTalk()).start();

        while (true) {
            // Wait for new connections.
            Socket clientSocket = this.receiveConnection();

            String clientUUID = UUID.randomUUID().toString();

            // Start a new ClientThread.
            ClientThread client = new ClientThread(clientUUID, clientSocket);
            Thread clientThread = new Thread(client);
            clientThread.start();
            clientThread.setName("Client " + clientUUID);
            // Add the client to the list of connected clients.
            this.connectedClients.put(clientUUID, client);
            // Add the client thread to the list of connected client threads.
            this.connectedClientsThreads.put(clientUUID, clientThread);

            System.out.println("[INFO] : JOIN - " + client.getUsername());

            this.broadcastMessage(new SignInMessage(client.getUsername()));

            this.broadcastUserList();
        }
    }

    /**
     * Creates a server socket that clients can connection to.
     */
    private void startListening () {
        try {
            this.serverSocket = new ServerSocket(this.port);
            // TODO: Log this.
            System.out.println("INFO : Server listening to port " + this.port);
        }
        catch (IOException | SecurityException e) {
            System.err.println("Couldn't open server socket.");
            // TODO: Log this and close the application.
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
            // TODO: Log this.
            System.out.println("Connection accepted from " + clientHost);
            return clientSocket;
        }
        catch (IOException | SecurityException e) {
            System.err.println("Error on receiving incoming connection.");
            // TODO: Log this and close the application.
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
        for (Entry<String, ClientThread> entry : this.connectedClients
                .entrySet()) {
            // String uuid = entry.getKey();
            ClientThread client = entry.getValue();
            client.send(object, byteToSend);
        }
    }

    /**
     * Sends a message to all connect clients.
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
    private void broadcastUserList () {
        UserList userList = new UserList();
        for (Entry<String, ClientThread> entry : this.connectedClients
                .entrySet()) {

            ClientThread client = entry.getValue();
            userList.add(client.getUsername());
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

        if ((ct = this.connectedClients.get(uuid)) != null) {

            System.out.println("[INFO] : Closing client socket for " + uuid);
            ct.close();
            this.connectedClients.remove(uuid);

            this.broadcastMessage(new SignOutMessage(ct.getUsername()));
        }

        if ((t = this.connectedClientsThreads.get(uuid)) != null) {
            System.out.println("[INFO] : Closing thread for " + uuid);
            try {
                // This thread is IO blocked until something is sent to the
                // client.
                t.join(1000);
            }
            catch (InterruptedException e) {}
            finally {
                this.connectedClientsThreads.remove(uuid);
            }
        }

        this.broadcastUserList();
    }

    /**
     * Removes disconnected clients from the list and closes their threads and
     * streams.
     */
    private void removeDisconnectedClients () {
        // TODO: Create this.
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
                    broadcastMessage(new ServerMessage(message));
                }
            }
            catch (IOException e) {
                System.err.println("Server input error.");
                // TODO: Log this and close the application.
            }
        }
    }

    private void listThreads () {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

        for (Thread th : threadArray) {
            System.out.println(th.getName());
        }
    }
}
