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
    public void dataNotAvailable() {
        System.out.println("Data Not Available");
    }

    @Override
    public void notAuthorized() {
        System.out.println("Not Authorized");
    }

    @Override
    public void unknownResponseCode(String code) {
        System.out.println("Unknown Response Code: " + code);
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
        if (dataSource != null) {
            dataSource.getMessages(chat.getId(), "", "", this);
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
}
