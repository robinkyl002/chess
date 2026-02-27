import chess.*;
import dataaccess.*;
import server.Server;

public class ServerMain {
    public static void main(String[] args) {
        try {
            int port = (args.length >= 1) ? Integer.parseInt(args[0]) : 8080;
            Server server = new Server();
            server.run(port);
            System.out.printf("Server started on port %d%n♕ 240 Chess Server", port);
        } catch (Exception e) {
            System.out.printf("Unable to start server%s%n", e.getMessage());
        }
    }
}
