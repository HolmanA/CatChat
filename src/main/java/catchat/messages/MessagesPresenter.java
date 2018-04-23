package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.DataSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesPresenter implements MessagesContract.Presenter, DataSource.MessagesCallback {

    private DataSource dataSource;
    private MessagesContract.View view;
    private Chat chat;
    private int sentId;

    public MessagesPresenter(MessagesContract.View view) {
        this.view = view;
        sentId = 1;
    }

    @Override
    public void start() {
        view.showNoChatSelected();
    }

    @Override
    public void stop() {

    }

    @Override
    public void unknownResponseCode(String response) {
        System.out.println(response);
    }

    @Override
    public void onChatLoaded(Chat chat) {
        this.chat = chat;
        view.showChatDetails(this.chat);
        refreshMessages();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void refreshMessages() {
        if (dataSource != null) {
            dataSource.getMessages(chat, "", "", this);
        }
    }

    @Override
    public void sendMessage() {
        String text;
        if (dataSource != null && !(text = view.getMessageText()).equals("")) {
            dataSource.sendMessage(chat.getId(), Integer.toString(sentId++), text, this);
        }
    }

    @Override
    public void likeMessage(Message message) {
        if (message != null && dataSource != null) {
            dataSource.likeMessage(chat.getId(), message.getId(), this);
        }
    }

    @Override
    public void unlikeMessage(Message message) {
        if (message != null && dataSource != null) {
            dataSource.unlikeMessage(chat.getId(), message.getId(), this);
        }
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

    @Override
    public void onMessageSent() {
        view.clearMessageText();
        refreshMessages();
    }

    @Override
    public void onMessageLiked() {
        refreshMessages();
    }

    @Override
    public void onMessageUnliked() {
        refreshMessages();
    }
}
