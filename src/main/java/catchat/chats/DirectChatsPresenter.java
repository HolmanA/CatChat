package catchat.chats;

import catchat.data.MessageEventBus;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.DataSource;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class DirectChatsPresenter implements
        ChatsContract.Presenter,
        MessageEventBus.Listener,
        DataSource.GetChatsCallback {

    private DataSource dataSource;
    private ChatsContract.View view;
    private DataSource.GetDirectChatCallback chatCallback;
    private int lastPageLoaded;

    public DirectChatsPresenter(DataSource dataSource,
                                ChatsContract.View view,
                                DataSource.GetDirectChatCallback chatCallback) {
        this.dataSource = dataSource;
        this.view = view;
        this.chatCallback = chatCallback;
        lastPageLoaded = 1;
    }

    @Override
    public void start() {
        refreshChats();
        view.setTitle("Direct Chats");
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

    @Override
    public void changed(Message message) {
        refreshChats();
    }
}
