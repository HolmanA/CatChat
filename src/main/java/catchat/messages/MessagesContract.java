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
        void showChatDetails(Chat chat);
    }

    interface Presenter extends BasePresenter {}
}
