package catchat.ui.chats;

import catchat.ui.BasePresenter;
import catchat.ui.BaseView;
import catchat.data.entities.chat.Chat;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public interface ChatsContract {
    interface View extends BaseView<Presenter> {
        void showChats(List<Chat> chats);
        void showNoChats();
        void clearChats();
        void setTitle(String text);
    }

    interface Presenter extends BasePresenter {
        void refreshChats();
        void loadMoreChats();
        void loadChat(Chat chat);
    }
}
