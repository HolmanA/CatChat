package catchat.chats.presenter;

import catchat.chats.ChatsContract;
import catchat.data.MessageEventBus;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.DataSource;

import java.util.List;

/**
 * Created by andrew on 5/6/18.
 */
public abstract class BaseChatsPresenter implements ChatsContract.Presenter, MessageEventBus.Listener, DataSource.GetChatsCallback {

    protected DataSource dataSource;
    protected ChatsContract.View view;
    protected int lastPageLoaded;

    protected BaseChatsPresenter(DataSource dataSource,
                                ChatsContract.View view) {
        this.dataSource = dataSource;
        this.view = view;
        lastPageLoaded = 1;
    }

    @Override
    public void onChatsLoaded(List<Chat> chats) {
        view.showChats(chats);
    }

    @Override
    public void changed(Message message) {
        refreshChats();
    }

    @Override
    public void unknownResponseCode(String response) {
        System.err.println(response);
    }
}
