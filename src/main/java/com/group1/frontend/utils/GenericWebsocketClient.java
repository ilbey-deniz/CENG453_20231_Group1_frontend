package com.group1.frontend.utils;

import javafx.application.Platform;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class GenericWebsocketClient extends WebSocketClient {
    private OnMessageHandler onMessageHandler;
    public interface OnMessageHandler {
        void handle(String message);
    }

    public GenericWebsocketClient(URI serverUri) {
        super(serverUri);
    }

    public void setMessageHandler(OnMessageHandler handler) {
        this.onMessageHandler = handler;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        Platform.runLater(() -> {
            System.out.println("Received message: " + s);
            if (onMessageHandler != null) {
                onMessageHandler.handle(s);
            }
        });
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
