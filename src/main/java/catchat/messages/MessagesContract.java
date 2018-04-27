package catchat.messages;

import catchat.BasePresenter;
import catchat.BaseView;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public interface MessagesContract {
    interface View extends BaseView<Presenter> {
        void showMessages(List<Message> messages);
        void showNoMessages();
        void clearMessages();
        void showChatDetails(Chat chat);
        void showNoChatSelected();
        String getMessageText();
        void clearMessageText();
    }

    interface Presenter extends BasePresenter {
        void refreshMessages();
        void loadMoreMessages();
        void sendMessage();
        void likeMessage(Message message);
        void unlikeMessage(Message message);
    }
}
