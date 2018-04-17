package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.chats.ChatDataSource;
import catchat.data.source.messages.MessageDataSource;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesPresenter implements MessagesContract.Presenter, ChatDataSource.GetChatCallback {
    private ChatDataSource dataSource;
    private MessagesContract.View view;

    public MessagesPresenter(ChatDataSource dataSource, MessagesContract.View view) {
        this.dataSource = dataSource;
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void dataNotAvailable() {
    }

    @Override
    public void notAuthorized() {

    }

    @Override
    public void onChatLoaded(Chat chat) {
        view.showChatDetails(chat);
    }
}
