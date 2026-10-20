package server;

import chess.*;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
        System.out.println("Server running on port 8080");
    }
}
