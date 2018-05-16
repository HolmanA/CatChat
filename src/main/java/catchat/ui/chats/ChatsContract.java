package catchat.ui.chats;

import catchat.data.entities.chat.Chat;
import catchat.ui.BasePresenter;
import catchat.ui.BaseView;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public interface ChatsContract {
    interface View extends BaseView<Presenter> {
        void setGroupChats(List<Chat> chats);

        void setDirectChats(List<Chat> chats);

        int getGroupChatsSize();

        int getDirectChatsSize();

        void clearGroupChatList();

        void clearDirectChatList();

        void scrollGroupChatsTo(int index);

        void scrollDirectChatsTo(int index);
    }

    interface Presenter extends BasePresenter {
        void reloadGroupChats();

        void reloadDirectChats();

        void loadMoreGroupChats();

        void loadMoreDirectChats();

        void selectChat(Chat chat);
    }
}
