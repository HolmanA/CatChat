package catchat.messages;

import catchat.data.DataMediator;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.receiver.message.NotificationMessage;
import catchat.data.source.DataSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesPresenter implements MessagesContract.Presenter, DataMediator.Listener {
    private DataSource dataSource;
    private MessagesContract.View view;
    private Chat chat;
    private int sentId;
    private Message lastMessage;

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
    public void refreshMessages() {
        if (chat != null) {
            view.clearMessages();
            switch (chat.getType()) {
                case GROUP:
                    dataSource.getGroupMessages(chat, null);
                    break;
                case DIRECT:
                    dataSource.getDirectMessages(chat, null);
                    break;
                default:
            }
        }
    }

    @Override
    public void loadMoreMessages() {
        if (chat != null && lastMessage != null) {
            switch (chat.getType()) {
                case GROUP:
                    dataSource.getGroupMessages(chat, lastMessage);
                    break;
                case DIRECT:
                    dataSource.getDirectMessages(chat, lastMessage);
                    break;
                default:
            }
        }
    }

    @Override
    public void sendMessage() {
        String text;
        if (chat != null && !(text = view.getMessageText()).equals("")) {
            switch (chat.getType()) {
                case GROUP:
                    dataSource.sendGroupMessage(chat, Integer.toString(++sentId), text);
                    break;
                case DIRECT:
                    dataSource.sendDirectMessage(chat, Integer.toString(++sentId), text);
                    break;
                default:
            }
        }
    }

    @Override
    public void likeMessage(Message message) {
        if (message != null && chat != null) {
            switch (chat.getType()) {
                case GROUP:
                    dataSource.likeGroupMessage(chat, message);
                    break;
                case DIRECT:
                    dataSource.likeDirectMessage(chat, message);
                    break;
                default:
            }
        }
    }

    @Override
    public void unlikeMessage(Message message) {
        if (message != null && chat != null) {
            switch (chat.getType()) {
                case GROUP:
                    dataSource.unlikeGroupMessage(chat, message);
                    break;
                case DIRECT:
                    dataSource.unlikeDirectMessage(chat, message);
                    break;
                default:
            }
        }
    }

    @Override
    public void onChatLoaded(Chat chat) {
        this.chat = chat;
        view.clearMemberList();
        view.showChatDetails(this.chat);
        view.showMembers(chat.getMembers());
        refreshMessages();
    }

    @Override
    public void onMessagesLoaded(List<Message> messages) {
        if (messages == null || messages.size() == 0) {
            view.showNoMessages();
        } else {
            Collections.reverse(messages);
            lastMessage = messages.get(0);
            view.showMessages(messages);
        }
    }

    @Override
    public void onMessageReceived(NotificationMessage message) {
        if (message.getChatId().equals(chat.getId())) {
            refreshMessages();
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

    @Override
    public void onProfileLoaded(Profile profile) {}
    @Override
    public void onChatsLoaded(List<Chat> chats) {}
}
