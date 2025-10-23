package server;

import io.javalin.*;

public class Server {

    private final Javalin httpHandler;

    public Server() {
        httpHandler = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }

    public void stop() {
        httpHandler.stop();
    }
}
