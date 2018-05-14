package catchat.ui.messages;

import catchat.ui.BasePresenter;
import catchat.ui.BaseView;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public interface MessagesContract {
    interface View extends BaseView<Presenter> {
        void showMessages(List<Message> messages);
        void showNoMessages();
        void clearMessages();
        void clearMemberList();
        void showChatDetails(Chat chat);
        void showNoChatSelected();
        String getMessageText();
        void clearMessageText();
        void showMembers(List<Profile> members);
    }

    interface Presenter extends BasePresenter {
        void refreshMessages();
        void loadMoreMessages();
        void sendMessage();
        void likeMessage(Message message);
        void unlikeMessage(Message message);
    }
}
