package catchat.data.receiver.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class MessageWebSocket {
    private final CountDownLatch closeLatch;
    private int count;
    private String authToken;
    private String userId;
    private Session session;
    private String clientId;
    private ObjectMapper mapper;
    private MessageReceiver.MessageReceivedCallback callback;

    public MessageWebSocket(String authToken, String userId, MessageReceiver.MessageReceivedCallback callback) {
        this.closeLatch = new CountDownLatch(1);
        this.authToken = authToken;
        this.userId = userId;
        this.callback = callback;
        count = 0;
        mapper = new ObjectMapper();
    }

    public void close() {
        session.close(StatusCode.NORMAL, "Thank you for your service");
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String message) {
        System.out.println("Connection Closed: (" + statusCode + ") " + message);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to Message Push Server");
        this.session = session;
        handshake();
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        switch (count) {
            case 0:
                try {
                    JsonNode responseTree = mapper.readTree(message);
                    clientId = responseTree.get(0).get("clientId").asText();
                    count++;
                    subscribe();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JsonNode responseTree = mapper.readTree(message);
                    boolean success = responseTree.get(0).get("successful").asBoolean();
                    if (success) {
                        count++;
                        connect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Platform.runLater(() -> callback.onMessageReceived());
        }
    }

    private void handshake() {
        if (session != null) {
            try {
                String message = "[{\"channel\":\"/meta/handshake\",";
                message += "\"version\":\"1.0\",";
                message += "\"supportedConnectionTypes\":[\"websocket\"],";
                message += "\"id\": \"1\"}]";

                Future future = session.getRemote().sendStringByFuture(message);
                future.get(2, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void subscribe() {
        if (session != null) {
            try {
                String message = "[{\"channel\":\"/meta/subscribe\",";
                message += "\"clientId\":\"" + clientId + "\",";
                message += "\"subscription\":\"/user/" + userId + "\",";
                message += "\"id\": \"2\",";
                message += "\"ext\": {";
                message += "\"access_token\": \"" + authToken + "\",";
                message += "\"timestamp\": \"" + System.currentTimeMillis() + "\"}}]";

                Future future = session.getRemote().sendStringByFuture(message);
                future.get(2, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() {
        if (session != null) {
            try {
                String message = "[{\"channel\":\"/meta/connect\",";
                message += "\"clientId\":\"" + clientId + "\",";
                message += "\"connectionType\":\"websocket\",";
                message += "\"id\": \"3\"}]";

                Future future = session.getRemote().sendStringByFuture(message);
                future.get(2, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
