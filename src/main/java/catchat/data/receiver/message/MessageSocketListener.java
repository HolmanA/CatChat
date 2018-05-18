package catchat.data.receiver.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(MessageSocketListener.class);
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
        log.debug("Creating with AuthToken: {} UserID: {}", authToken, userId);
    }

    void connect() throws Exception {
        log.info("Connecting to {}", URL);
        if (!webSocketClient.isStarted()) {
            webSocketClient.start();
        }
        URI uri = new URI(URL);
        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        webSocketClient.connect(this, uri, upgradeRequest);
    }

    boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        log.debug("Awaiting Close");
        return this.closeLatch.await(duration, unit);
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        log.debug("Binary Received");
    }

    @Override
    public void onWebSocketText(String responseMessage) {
        log.debug("Message Received");
        log.trace(responseMessage);
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
                        String senderId = subjectNode.get("sender_id").asText();
                        String senderName = subjectNode.get("name").asText();
                        String messageText = subjectNode.get("text").asText();
                        Platform.runLater(() -> {
                            for (MessageReceiverContract.Listener listener : listeners) {
                                listener.messageReceived(new NotificationMessage(chatId, senderId, senderName, messageText));
                            }
                        });
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw e;
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        log.debug("Connection Closed");
        this.session = null;
        this.closeLatch.countDown();

        if (statusCode == StatusCode.ABNORMAL) {
            log.warn("{}: {}; Attempting to reconnect", statusCode, reason);
            try {
                connect();
            } catch (Exception e) {
                log.error("Failed to reconnect");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWebSocketConnect(Session session) {
        log.info("Connected to {}", session.getRemoteAddress().getHostName());
        this.session = session;
        sendHandshake();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        log.warn("Connection Error; Attempting to reconnect");
        try {
            connect();
        } catch (Exception e) {
            log.error("Failed to reconnect");
            e.printStackTrace();
        }
    }

    public void setListeners(List<MessageReceiverContract.Listener> listeners) {
        this.listeners = listeners;
    }

    private void sendHandshake() {
        log.debug("Sending Handshake");
        try {
            String message = "[{\"channel\":\"/meta/handshake\",";
            message += "\"version\":\"1.0\",";
            message += "\"supportedConnectionTypes\":[\"websocket\"],";
            message += "\"id\": \"1\"}]";

            Future future = session.getRemote().sendStringByFuture(message);
            future.get(2, TimeUnit.SECONDS);
            log.trace("Handshake: {}", message);
        } catch (Exception e) {
            log.error("Unable to send handshake");
            e.printStackTrace();
        }
    }

    private void sendSubscribe(String clientId) {
        log.debug("Sending subscription message");
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
            log.trace(message);
        } catch (Exception e) {
            log.error("Unable to send subscription message");
            e.printStackTrace();
        }
    }

    private void sendConnect(String clientId) {
        log.debug("Sending connection message");
        try {
            String message = "[{\"channel\":\"/meta/connect\",";
            message += "\"clientId\":\"" + clientId + "\",";
            message += "\"connectionType\":\"websocket\",";
            message += "\"id\": \"3\"}]";

            Future future = session.getRemote().sendStringByFuture(message);
            future.get(2, TimeUnit.SECONDS);
            log.trace(message);
        } catch (Exception e) {
            log.error("Unable to send connection message");
            e.printStackTrace();
        }
    }
}
