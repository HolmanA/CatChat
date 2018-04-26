package catchat.messages;

import catchat.data.entities.ChatType;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.DataSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesPresenter implements
        MessagesContract.Presenter,
        DataSource.GetGroupChatCallback,
        DataSource.GetDirectChatCallback,
        DataSource.GetMessagesCallback,
        DataSource.SendMessageCallback,
        DataSource.LikeMessageCallback,
        DataSource.UnlikeMessageCallback {

    private DataSource dataSource;
    private MessagesContract.View view;
    private ChatType type;
    private Chat chat;
    private int sentId;

    public MessagesPresenter(DataSource dataSource, MessagesContract.View view) {
        this.dataSource = dataSource;
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
    public void refreshMessages() {
        if (chat != null) {
            switch (type) {
                case GROUP:
                    dataSource.getGroupMessages(chat, this);
                    break;
                case DIRECT:
                    dataSource.getDirectMessages(chat, this);
                    break;
                default:
            }
        }
    }

    @Override
    public void sendMessage() {
        String text;
        if (chat != null && !(text = view.getMessageText()).equals("")) {
            switch (type) {
                case GROUP:
                    dataSource.sendGroupMessage(chat, Integer.toString(sentId), text, this);
                    break;
                case DIRECT:
                    dataSource.sendDirectMessage(chat, Integer.toString(sentId), text, this);
                    break;
                default:
            }
        }
    }

    @Override
    public void likeMessage(Message message) {
        if (message != null && chat != null) {
            switch (type) {
                case GROUP:
                    dataSource.likeGroupMessage(chat, message, this);
                    break;
                case DIRECT:
                    dataSource.likeDirectMessage(chat, message, this);
                    break;
                default:
            }
        }
    }

    @Override
    public void unlikeMessage(Message message) {
        if (message != null && chat != null) {
            switch (type) {
                case GROUP:
                    dataSource.unlikeGroupMessage(chat, message, this);
                    break;
                case DIRECT:
                    dataSource.unlikeDirectMessage(chat, message, this);
                    break;
                default:
            }
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

    @Override
    public void onGroupChatLoaded(Chat chat) {
        type = ChatType.GROUP;
        chatLoaded(chat);
    }

    @Override
    public void onDirectChatLoaded(Chat chat) {
        type = ChatType.DIRECT;
        chatLoaded(chat);
    }

    private void chatLoaded(Chat chat) {
        this.chat = chat;
        view.showChatDetails(this.chat);
        refreshMessages();
    }
}
