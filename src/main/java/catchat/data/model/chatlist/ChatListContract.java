package catchat.data.model.chatlist;

import catchat.data.entities.chat.Chat;

import java.util.List;

/**
 * ChatListContract
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public interface ChatListContract {
    interface Listener {
        void chatListChanged();
    }

    interface Model {
        void subscribe(Listener listener);

        void unsubscribe(Listener listener);

        void unsubscribeAll();

        void reloadChats();

        void loadMoreChats();

        List<Chat> getChats();
    }
}
