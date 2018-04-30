package catchat.data.receiver.message;

import catchat.data.entities.message.Message;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageReceiver {
    interface MessageReceivedCallback {
        void onMessageReceived();
    }

    private static final String URL = "ws://push.groupme.com/faye";
    private MessageWebSocket webSocket;
    private MessageReceivedCallback callback;
    private String authToken;
    private String userId;

    public MessageReceiver(String authToken, String userId, MessageReceivedCallback callback) {
        this.authToken = authToken;
        this.userId = userId;
        this.callback = callback;
    }

    public void start() {
        WebSocketClient clientSocket = new WebSocketClient();
        webSocket = new MessageWebSocket(authToken, userId, callback);
        try {
            clientSocket.start();
            URI uri = new URI(URL);
            ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
            clientSocket.connect(webSocket, uri, upgradeRequest);
            System.out.println("Connecting to: " + uri);
            webSocket.awaitClose(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        webSocket.close();
    }
}
