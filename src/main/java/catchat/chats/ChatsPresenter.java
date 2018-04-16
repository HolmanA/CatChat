package catchat.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.source.chats.ChatDataSource;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class ChatsPresenter implements ChatsContract.Presenter, ChatDataSource.GetChatsCallback {
    private ChatDataSource dataSource;
    private ChatsContract.View view;

    public ChatsPresenter(ChatDataSource dataSource, ChatsContract.View view) {
        this.dataSource = dataSource;
        this.view = view;
    }

    @Override
    public void start() {
        refreshGroups();
    }

    @Override
    public void stop() {
    }

    @Override
    public void refreshGroups() {
        dataSource.getChats(1, 10, "", this);
    }

    @Override
    public void loadGroup(Chat group) {

    }

    @Override
    public void dataNotAvailable() {

    }

    @Override
    public void notAuthorized() {

    }

    @Override
    public void onChatsLoaded(List<Chat> chats) {
        view.showGroups(chats);
    }
}
