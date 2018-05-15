package catchat.data.receiver.message;

import catchat.data.authentication.OAuthService;
import catchat.data.model.userprofile.UserProfileContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageReceiver implements MessageReceiverContract.Receiver, UserProfileContract.Listener {
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
        System.out.println("Message Receiver Started");
        userProfileModel.loadUserProfile();
    }

    @Override
    public void stop() {
        try {
            webSocket.awaitClose(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userProfileChanged() {
        if (webSocket == null) {
            System.out.println("Creating Web Socket");
            webSocket = new MessageSocketListener(authService.getAPIToken(), userProfileModel.getUserProfile().getId());
            try {
                webSocket.connect();
                webSocket.awaitClose(5, TimeUnit.SECONDS);
            } catch (Exception e) {
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
