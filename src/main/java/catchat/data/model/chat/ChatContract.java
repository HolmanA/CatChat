package catchat.data.model.chat;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;

import java.util.List;

/**
 * ChatContract
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public interface ChatContract {
    interface Listener {
        void chatChanged();
        void messageSent();
    }

    interface Model {
        void subscribe(Listener listener);
        void unsubscribe(Listener listener);
        void unsubscribeAll();
        void loadMoreMessages();
        void reloadMessages();
        void sendMessage(String messageText);
        void clearMessages();
        void likeMessage(Message message);
        void unlikeMessage(Message message);
        Chat getChat();
        List<Message> getMessages();
    }
}
