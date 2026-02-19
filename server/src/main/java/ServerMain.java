import chess.*;
import dataaccess.*;
import server.Server;

public class ServerMain {
    public static void main(String[] args) {
        int port = (args.length >= 1) ? Integer.parseInt(args[0]) : 8080;
        Server server = new Server();
        server.run(port);
        System.out.println("♕ 240 Chess Server");
    }
}
