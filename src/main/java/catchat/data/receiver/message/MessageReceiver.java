package catchat.data.receiver.message;

import catchat.data.DataMediator;
import catchat.data.authentication.OAuthService;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.source.DataSource;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageReceiver implements DataMediator.Listener {
    private MessageSocketListener webSocket;
    private DataMediator dataMediator;
    private OAuthService authService;
    private DataSource dataSource;

    public MessageReceiver(OAuthService authService, DataSource dataSource, DataMediator dataMediator) {
        this.authService = authService;
        this.dataSource = dataSource;
        this.dataMediator = dataMediator;
        dataMediator.subscribe(this);
    }

    public void start() {
        System.out.println("Message Receiver Started");
        dataSource.getUserProfile();
    }

    public void stop() {
        try {
            webSocket.awaitClose(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProfileLoaded(Profile profile) {
        if (webSocket == null) {
            System.out.println("Creating Web Socket");
            webSocket = new MessageSocketListener(authService.getAPIToken(), profile.getId(), dataMediator);
            try {
                webSocket.connect();
                webSocket.awaitClose(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onChatsLoaded(List<Chat> chats) {}
    @Override
    public void onChatLoaded(Chat chat) {}
    @Override
    public void onMessagesLoaded(List<Message> messages) {}
    @Override
    public void onMessageReceived(NotificationMessage message) {}
    @Override
    public void onMessageSent() {}
    @Override
    public void onMessageLiked() {}
    @Override
    public void onMessageUnliked() {}
}
