package se.stjerneman.anonchat.server;

import se.stjerneman.anonchat.utils.Debug;

public class StartServer {

    public static void main (String[] args) {
        Debug.setDebug(true);
        Server server = Server.getInstance();
        server.setServerPort(6789);
        server.startServer();
    }
}
