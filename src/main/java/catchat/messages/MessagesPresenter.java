package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.source.chats.ChatDataSource;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesPresenter implements MessagesContract.Presenter, ChatDataSource.GetChatCallback {
    private ChatDataSource dataSource;
    private MessagesContract.View view;

    public MessagesPresenter(MessagesContract.View view) {
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

    @Override
    public void setDataSource(ChatDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
