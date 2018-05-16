package catchat.ui.messages;

import catchat.data.entities.message.Message;
import catchat.ui.BasePresenter;
import catchat.ui.BaseView;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public interface MessagesContract {
    interface View extends BaseView<Presenter> {
        void showChatPane();

        void hideChatPane();

        boolean chatPainVisible();

        void setMessages(List<Message> messages);

        int getMessagesSize();

        void clearMessages();

        void scrollMessagesTo(int index);

        String getMessageText();

        void clearMessageText();
        //void showNoMessages();
        //void clearMemberList();
        //void showChatDetails(Chat chat);
        //void showNoChatSelected();
        //void showMembers(List<MemberProfile> members);
    }

    interface Presenter extends BasePresenter {
        void reloadMessages();

        void loadMoreMessages();

        void sendMessage();

        void likeMessage(Message message);

        void unlikeMessage(Message message);
    }
}
