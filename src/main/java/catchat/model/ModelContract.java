package catchat.model;

import catchat.data.source.entities.chat.Chat;
import catchat.model.chat.ChatContract;
import catchat.model.chatlist.ChatListContract;
import catchat.model.userprofile.UserProfileContract;

/**
 * ModelContract
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public interface ModelContract {
    interface Listener {
        void selectionChanged();

        void sameChatSelected();
    }

    interface Model {
        void subscribe(Listener listener);

        void unsubscribe(Listener listener);

        void unsubscribeAll();

        void selectChat(Chat chat);

        void reloadAll();

        ChatListContract.Model getGroupChatListModel();

        ChatListContract.Model getDirectChatListModel();

        UserProfileContract.Model getUserProfileModel();

        ChatContract.Model getSelectedChatModel();
    }
}
