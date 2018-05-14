package catchat.data;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.receiver.message.NotificationMessage;
import catchat.data.source.DataSource;
import catchat.data.source.GroupMeDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/29/18.
 */
public class DataMediator {
    public interface Listener {
        void onChatsLoaded(List<Chat> chats);
        void onChatLoaded(Chat chat);
        void onMessagesLoaded(List<Message> messages);
        void onMessageReceived(NotificationMessage message);
        void onMessageSent();
        void onMessageLiked();
        void onMessageUnliked();
        void onProfileLoaded(Profile profile);
    }
    
    private List<Listener> listeners;

    public DataMediator() {
        listeners = new ArrayList<>();
    }

    public void subscribe(Listener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(Listener listener) {
        listeners.remove(listener);
    }

    public void unsubscribeAll() {
        listeners.clear();
    }

    public void chatsLoaded(List<Chat> chats) {
        for (Listener listener : listeners) {
            listener.onChatsLoaded(chats);
        }
    }

    public void chatLoaded(Chat chat) {
        for (Listener listener : listeners) {
            listener.onChatLoaded(chat);
        }
    }

    public void messagesLoaded(List<Message> messages) {
        for (Listener listener : listeners) {
            listener.onMessagesLoaded(messages);
        }
    }

    public void messageReceived(NotificationMessage message) {
        for (Listener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }

    public void messageSent() {
        for (Listener listener : listeners) {
            listener.onMessageSent();
        }
    }

    public void messageLiked() {
        for (Listener listener : listeners) {
            listener.onMessageLiked();
        }
    }

    public void messageUnliked() {
        for (Listener listener : listeners) {
            listener.onMessageUnliked();
        }
    }

    public void profileLoaded(Profile profile) {
        for (Listener listener : listeners) {
            listener.onProfileLoaded(profile);
        }
    }

    public void unknownResponseCode(String response) {
        System.err.println(response);
    }
}
