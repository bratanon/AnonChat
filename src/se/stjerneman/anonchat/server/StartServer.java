package se.stjerneman.anonchat.server;

public class StartServer {

    public static void main (String[] args) {
        Server server = Server.getInstance();
        server.setServerPort(666);
        server.startServer();
    }
}
