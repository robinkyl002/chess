package handler.websocket;

import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket connection closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        System.out.println("Websocket connection opened");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {

    }

    private void connect() {

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}
