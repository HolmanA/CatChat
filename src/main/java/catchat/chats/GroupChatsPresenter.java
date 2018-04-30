package catchat.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.receiver.message.MessageChangeListener;
import catchat.data.source.DataSource;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class GroupChatsPresenter implements
        ChatsContract.Presenter,
        MessageChangeListener,
        DataSource.GetChatsCallback {

    private DataSource dataSource;
    private ChatsContract.View view;
    private DataSource.GetGroupChatCallback chatCallback;
    private int lastPageLoaded;

    public GroupChatsPresenter(DataSource dataSource,
                               ChatsContract.View view,
                               DataSource.GetGroupChatCallback chatCallback) {
        this.dataSource = dataSource;
        this.view = view;
        this.chatCallback = chatCallback;
        lastPageLoaded = 1;
    }

    @Override
    public void start() {
        refreshChats();
        view.setTitle("Group Chats");
    }

    @Override
    public void stop() {
    }

    @Override
    public void unknownResponseCode(String response) {
        System.out.println(response);
    }

    @Override
    public void onChatsLoaded(List<Chat> chats) {
        view.showChats(chats);
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

    @Override
    public void changed() {
        refreshChats();
    }
}
