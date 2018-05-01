package catchat.data.receiver.message;

import catchat.data.auth.OAuthService;
import catchat.data.entities.profile.Profile;
import catchat.data.source.DataSource;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageReceiver implements DataSource.GetUserProfileCallback {
    interface MessageReceivedCallback {
        void onMessageReceived();
    }

    private static final String URL = "ws://push.groupme.com/faye";
    private MessageWebSocket webSocket;
    private MessageReceivedCallback callback;
    private OAuthService authService;
    private DataSource dataSource;

    public MessageReceiver(OAuthService authService, DataSource dataSource, MessageReceivedCallback callback) {
        this.authService = authService;
        this.dataSource = dataSource;
        this.callback = callback;
    }

    public void start() {
        dataSource.getUserProfile(this);
    }

    public void stop() {
        webSocket.close();
    }

    @Override
    public void onUserProfileLoaded(Profile profile) {
        WebSocketClient clientSocket = new WebSocketClient();
        webSocket = new MessageWebSocket(authService.getAPIToken(), profile.getId(), callback);
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

    @Override
    public void unknownResponseCode(String response) {

    }
}
