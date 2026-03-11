import chess.*;
import dataaccess.*;
import server.Server;
import service.GameService;
import service.UserService;

public class ServerMain {
    public static void main(String[] args) {
        try {
            int port = (args.length >= 1) ? Integer.parseInt(args[0]) : 8080;

            UserDAO userDAO = new MemoryUserDAO();
            AuthDAO authDAO = new MemoryAuthDAO();
            GameDAO gameDAO = new MemoryGameDAO();

            if (args.length >= 2 && args[1].equals("sql")) {
                userDAO = new SQLUserDAO();
                authDAO = new SQLAuthDAO();
                gameDAO = new SQLGameDAO();
            }

            UserService userService = new UserService(userDAO, authDAO);
            GameService gameService = new GameService(authDAO, gameDAO);
            Server server = new Server(userService, gameService);

            server.run(port);
            System.out.printf("Server started on port %d%n♕ 240 Chess Server", port);
        } catch (Exception e) {
            System.out.printf("Unable to start server%s%n", e.getMessage());
        }
    }
}
