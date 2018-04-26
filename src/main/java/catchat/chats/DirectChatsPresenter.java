package catchat.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.source.DataSource;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class DirectChatsPresenter implements
        ChatsContract.Presenter,
        DataSource.GetChatsCallback {

    private DataSource dataSource;
    private ChatsContract.View view;
    private DataSource.GetDirectChatCallback chatCallback;
    private int chatsPage;

    public DirectChatsPresenter(DataSource dataSource,
                                ChatsContract.View view,
                                DataSource.GetDirectChatCallback chatCallback) {
        this.dataSource = dataSource;
        this.view = view;
        this.chatCallback = chatCallback;
        chatsPage = 1;
    }

    @Override
    public void start() {
        refreshChats();
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
        view.showGroups(chats);
        view.setPageNumber(chatsPage);
    }

    @Override
    public void prevPage() {
        chatsPage = (--chatsPage > 1) ? chatsPage : 1;
        refreshChats();
    }

    @Override
    public void nextPage() {
        chatsPage++;
        refreshChats();
    }

    @Override
    public void refreshChats() {
        dataSource.getDirectChats(this);
    }

    @Override
    public void loadChat(Chat chat) {
        if (chat != null) {
            dataSource.getDirectChat(chat, chatCallback);
        }
    }
}
