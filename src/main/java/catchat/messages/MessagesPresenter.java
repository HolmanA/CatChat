package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.chats.ChatDataSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesPresenter implements MessagesContract.Presenter, ChatDataSource.MessagesCallback {

    private ChatDataSource dataSource;
    private MessagesContract.View view;
    private Chat chat;

    public MessagesPresenter(MessagesContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showNoChatSelected();
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
        this.chat = chat;
        view.showChatDetails(this.chat);
        refreshMessages();
    }

    @Override
    public void setDataSource(ChatDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void refreshMessages() {
        dataSource.getMessages(chat.getId(), "", "", this);
    }

    @Override
    public void onMessagesLoaded(List<Message> messages) {
        if (messages == null || messages.size() == 0) {
            view.showNoMessages();
        } else {
            Collections.reverse(messages);
            view.showMessages(messages);
        }
    }
}
