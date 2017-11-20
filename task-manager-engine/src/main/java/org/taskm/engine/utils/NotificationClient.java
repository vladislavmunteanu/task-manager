package org.taskm.engine.utils;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import org.taskm.engine.EngineException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NotificationClient {


    private static Logger logger = Logger.getLogger(NotificationClient.class);
    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    private StompSession stompSession;

    public NotificationClient() throws EngineException {

        try {
            this.stompSession =  this.connect().get();
        } catch (InterruptedException | ExecutionException e) {
           logger.error("Failed to connect 'Notification Client'",e);
           throw new EngineException("Failed to connect 'Notification Client'",e);
        }

    }

    private ListenableFuture<StompSession> connect() {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        String port = System.getProperty("port");

        String url = "ws://{host}:{port}/console/notification";
        return stompClient.connect(url, headers, new MyHandler(), "localhost", port);
    }

    private class MyHandler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Notification Client connected");
        }
    }

    public void sendMessage(String message) {

        String jsonHello = "{ \"name\" : \" "+message+"\" }";
        this.stompSession.send("/console/notification", jsonHello.getBytes());
    }
}
