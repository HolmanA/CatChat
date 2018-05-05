package catchat.data.receiver.message;

import catchat.data.entities.message.NotificationMessage;
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
    private String authToken;
    private String userId;
    private Session session;
    private ObjectMapper mapper;
    private MessageReceiver.MessageReceivedCallback callback;

    public MessageWebSocket(String authToken, String userId, MessageReceiver.MessageReceivedCallback callback) {
        this.closeLatch = new CountDownLatch(1);
        this.authToken = authToken;
        this.userId = userId;
        this.callback = callback;
        mapper = new ObjectMapper();
    }

    public void close() {
        session.close(StatusCode.NORMAL, "Session Closed");
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    @SuppressWarnings("unused")
    public void onClose(int statusCode, String message) {
        System.out.println("Connection Closed: (" + statusCode + ") " + message);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    @SuppressWarnings("unused")
    public void onConnect(Session session) {
        System.out.println("Connected to Message Push Server");
        this.session = session;
        handshake();
    }

    @OnWebSocketMessage
    @SuppressWarnings("unused")
    public void onMessage(String responseMessage) {
        System.out.println("Received: " + responseMessage);
        try {
            JsonNode responseTree = mapper.readTree(responseMessage);
            JsonNode responseNode = responseTree.get(0);
            String responseId = responseNode.get("id").asText();
            String clientId = responseNode.get("clientId").asText();

            switch (responseId) {
                case "1":
                    subscribe(clientId);
                    break;
                case "2":
                    connect(clientId);
                    break;
                default:
                    JsonNode subjectNode = responseNode.get("data").get("subject");
                    String senderName = subjectNode.get("name").asText();
                    String messageText = subjectNode.get("text").asText();
                    Platform.runLater(() -> {
                        callback.onMessageReceived(new NotificationMessage(messageText, senderName));
                    });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handshake() {
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

    private void subscribe(String clientId) {
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

    private void connect(String clientId) {
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
