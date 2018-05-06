package catchat.data.receiver.message;

import catchat.data.auth.OAuthService;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.source.DataSource;

import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageReceiver implements DataSource.GetUserProfileCallback {
    public interface MessageReceivedCallback {
        void onMessageReceived(Message message);
    }

    private MessageSocketListener webSocket;
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
        try {
            webSocket.awaitClose(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserProfileLoaded(Profile profile) {
        webSocket = new MessageSocketListener(authService.getAPIToken(), profile.getId(), callback);
        try {
            webSocket.connect();
            webSocket.awaitClose(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unknownResponseCode(String response) {
    }
}
