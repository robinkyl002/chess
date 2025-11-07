import chess.*;
import dataaccess.MemoryUserDataAccess;
import dataaccess.UserDAO;
import server.Server;

public class Main {
    public static void main(String[] args) {
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            UserDAO userDataAccess = new MemoryUserDataAccess();
            // var service = new UserService(userDataAccess);
            Server server = new Server();
            server.run(port);

            System.out.printf("Server started on port %d with %s%n", port, userDataAccess.getClass());

            return;
        }
        catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

        System.out.println("♕ 240 Chess Server");
    }
}