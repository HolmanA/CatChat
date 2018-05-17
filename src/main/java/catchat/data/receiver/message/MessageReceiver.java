package catchat.data.receiver.message;

import catchat.data.authentication.OAuthService;
import catchat.data.model.userprofile.UserProfileContract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageReceiver implements MessageReceiverContract.Receiver, UserProfileContract.Listener {
    private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);
    private List<MessageReceiverContract.Listener> listeners;
    private MessageSocketListener webSocket;
    private OAuthService authService;
    private UserProfileContract.Model userProfileModel;

    public MessageReceiver(OAuthService authService, UserProfileContract.Model userProfileModel) {
        this.authService = authService;
        this.userProfileModel = userProfileModel;
        userProfileModel.subscribe(this);
        listeners = new ArrayList<>();
    }

    @Override
    public void start() {
        log.info("Starting");
        userProfileModel.loadUserProfile();
    }

    @Override
    public void stop() {
        log.info("Stopping");
        try {
            webSocket.awaitClose(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userProfileChanged() {
        if (webSocket == null) {
            webSocket = new MessageSocketListener(authService.getAPIToken(), userProfileModel.getUserProfile().getId());
            try {
                webSocket.connect();
                webSocket.awaitClose(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Unable to connect to web socket");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void subscribe(MessageReceiverContract.Listener listener) {
        listeners.add(listener);
        setWebSocketListeners();
    }

    @Override
    public void unsubscribe(MessageReceiverContract.Listener listener) {
        listeners.remove(listener);
        setWebSocketListeners();
    }

    @Override
    public void unsubscribeAll() {
        listeners.clear();
        setWebSocketListeners();
    }

    private void setWebSocketListeners() {
        if (webSocket != null) {
            webSocket.setListeners(listeners);
        }
    }
}
