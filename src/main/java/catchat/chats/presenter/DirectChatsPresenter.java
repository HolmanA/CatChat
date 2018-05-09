package catchat.chats.presenter;

import catchat.chats.ChatsContract;
import catchat.data.entities.chat.Chat;
import catchat.data.source.DataSource;

/**
 * Created by andrew on 4/15/18.
 */
public class DirectChatsPresenter extends BaseChatsPresenter {
    private DataSource.GetDirectChatCallback chatCallback;

    public DirectChatsPresenter(DataSource dataSource,
                                ChatsContract.View view,
                                DataSource.GetDirectChatCallback chatCallback) {
        super(dataSource, view);
        this.chatCallback = chatCallback;
    }

    @Override
    public void start() {
        refreshChats();
        view.setTitle("Direct Chats");
    }

    @Override
    public void refreshChats() {
        view.clearChats();
        lastPageLoaded = 1;
        dataSource.getDirectChats(1, 10, this);
    }

    @Override
    public void loadMoreChats() {
        dataSource.getDirectChats(++lastPageLoaded, 10, this);
    }

    @Override
    public void loadChat(Chat chat) {
        if (chat != null) {
            dataSource.getDirectChat(chat, chatCallback);
        }
    }
}
