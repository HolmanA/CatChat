package catchat.data.receiver.message;

import catchat.data.model.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageSocketListener implements WebSocketListener {
    private static final String URL = "ws://push.groupme.com/faye";
    private final CountDownLatch closeLatch;
    private WebSocketClient webSocketClient;
    private String authToken;
    private String userId;
    private Session session;
    private ObjectMapper mapper;
    private List<MessageReceiverContract.Listener> listeners;

    MessageSocketListener(String authToken, String userId) {
        this.closeLatch = new CountDownLatch(1);
        this.webSocketClient = new WebSocketClient();
        this.authToken = authToken;
        this.userId = userId;
        mapper = new ObjectMapper();
    }

    void connect() throws Exception {
        if (!webSocketClient.isStarted()) {
            webSocketClient.start();
        }
        URI uri = new URI(URL);
        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        System.out.println("Connecting to: " + uri);
        webSocketClient.connect(this, uri, upgradeRequest);
    }

    boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {}

    @Override
    public void onWebSocketText(String responseMessage) {
        try {
            JsonNode responseTree = mapper.readTree(responseMessage);
            JsonNode responseNode = responseTree.get(0);
            String responseId = responseNode.get("id").asText();
            String clientId = responseNode.get("clientId").asText();

            switch (responseId) {
                case "1":
                    sendSubscribe(clientId);
                    break;
                case "2":
                    sendConnect(clientId);
                    break;
                default:
                    JsonNode dataNode;
                    JsonNode subjectNode;
                    if ((dataNode = responseNode.get("data")) != null
                            && (subjectNode = dataNode.get("subject")) != null) {

                        String chatId = (subjectNode.has("chat_id") ? subjectNode.get("chat_id").asText() : subjectNode.get("group_id").asText());
                        String senderName = subjectNode.get("name").asText();
                        String messageText = subjectNode.get("text").asText();
                        Platform.runLater(() -> {
                            for (MessageReceiverContract.Listener listener : listeners) {
                                listener.messageReceived(new NotificationMessage(chatId, messageText, senderName));
                            }
                        });
                    }
            }
        } catch (IOException e) {
            System.err.println("Message: " + responseMessage);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Message " + responseMessage);
            throw e;
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("Connection Closed: (" + statusCode + ") " + reason);
        this.session = null;
        this.closeLatch.countDown();

        if (statusCode == StatusCode.ABNORMAL) {
            try {
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWebSocketConnect(Session session) {
        System.out.println("Connected to Message Push Server");
        this.session = session;
        sendHandshake();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println("Connection Error; Attempting to reconnect");
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListeners(List<MessageReceiverContract.Listener> listeners) {
        this.listeners = listeners;
    }

    private void sendHandshake() {
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

    private void sendSubscribe(String clientId) {
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

    private void sendConnect(String clientId) {
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
