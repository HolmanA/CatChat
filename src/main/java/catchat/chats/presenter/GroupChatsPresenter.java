package catchat.chats.presenter;

import catchat.chats.ChatsContract;
import catchat.data.entities.chat.Chat;
import catchat.data.source.DataSource;

/**
 * Created by andrew on 4/15/18.
 */
public class GroupChatsPresenter extends BaseChatsPresenter {
    private DataSource.GetGroupChatCallback chatCallback;

    public GroupChatsPresenter(DataSource dataSource, ChatsContract.View view,
                               DataSource.GetGroupChatCallback chatCallback) {
        super(dataSource, view);
        this.chatCallback = chatCallback;
    }

    @Override
    public void start() {
        refreshChats();
        view.setTitle("Group Chats");
    }

    @Override
    public void refreshChats() {
        view.clearChats();
        lastPageLoaded = 1;
        dataSource.getGroupChats(1, 10, this);
    }

    @Override
    public void loadMoreChats() {
        dataSource.getGroupChats(++lastPageLoaded, 10, this);
    }

    @Override
    public void loadChat(Chat chat) {
        if (chat != null) {
            dataSource.getGroupChat(chat, chatCallback);
        }
    }
}
