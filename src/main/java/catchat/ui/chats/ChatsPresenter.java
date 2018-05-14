package catchat.ui.chats;

import catchat.data.DataMediator;
import catchat.data.entities.ChatType;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.receiver.message.NotificationMessage;
import catchat.data.source.DataSource;

import java.util.List;

/**
 * Created by andrew on 5/6/18.
 */
public class ChatsPresenter implements ChatsContract.Presenter, DataMediator.Listener {
    private ChatType type;
    private DataSource dataSource;
    private ChatsContract.View view;
    private int lastPageLoaded;

    public ChatsPresenter(ChatType type, DataSource dataSource, ChatsContract.View view) {
        this.type = type;
        this.dataSource = dataSource;
        this.view = view;
        lastPageLoaded = 1;
    }

    @Override
    public void start() {
        refreshChats();
        switch (type) {
            case DIRECT:
                view.setTitle("Direct Chats");
                break;
            case GROUP:
                view.setTitle("Group Chats");
                break;
            default:
        }
    }

    @Override
    public void refreshChats() {
        view.clearChats();
        lastPageLoaded = 1;
        switch (type) {
            case DIRECT:
                dataSource.getDirectChats(1, 10);
                break;
            case GROUP:
                dataSource.getGroupChats(1, 10);
                break;
            default:
        }
    }

    @Override
    public void loadMoreChats() {
        switch (type) {
            case DIRECT:
                dataSource.getDirectChats(++lastPageLoaded, 10);
                break;
            case GROUP:
                dataSource.getGroupChats(++lastPageLoaded, 10);
                break;
            default:
        }
    }

    @Override
    public void loadChat(Chat chat) {
        if (chat != null) {
            switch (type) {
                case DIRECT:
                    dataSource.getDirectChat(chat);
                    break;
                case GROUP:
                    dataSource.getGroupChat(chat);
                    break;
                default:
            }
        }
    }

    @Override
    public void onChatsLoaded(List<Chat> chats) {
        if (!chats.isEmpty() && chats.get(0).getType() == type) {
            view.showChats(chats);
        }
    }

    @Override
    public void onMessageReceived(NotificationMessage message) {
        refreshChats();
    }

    @Override
    public void onMessageSent() {
        refreshChats();
    }

    @Override
    public void onMessagesLoaded(List<Message> messages) {}
    @Override
    public void onChatLoaded(Chat chat) {}
    @Override
    public void onMessageLiked() {}
    @Override
    public void onMessageUnliked() {}
    @Override
    public void onProfileLoaded(Profile profile) {}
}
