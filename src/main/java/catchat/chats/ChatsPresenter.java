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
    private ChatDataSource.GetMessagesCallback messagesCallback;
    private int chatsPage;

    public ChatsPresenter(ChatDataSource dataSource,
                          ChatsContract.View view,
                          ChatDataSource.GetMessagesCallback messagesCallback) {
        this.dataSource = dataSource;
        this.view = view;
        this.messagesCallback = messagesCallback;
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
    public void dataNotAvailable() {

    }

    @Override
    public void notAuthorized() {

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
        System.out.println("Page: " + chatsPage);
        refreshChats();
    }

    @Override
    public void refreshChats() {
        System.out.println("Page: " + chatsPage);
        dataSource.getChats(chatsPage, 5, "", this);

    }

    @Override
    public void loadChat(Chat chat) {
        if (chat != null) {
            dataSource.getMessages(chat.getId(), "", "", messagesCallback);
        }
    }
}
