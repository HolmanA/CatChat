package catchat.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.source.chats.ChatDataSource;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class ChatsPresenter implements ChatsContract.Presenter, ChatDataSource.ChatsCallback {
    private ChatDataSource dataSource;
    private ChatsContract.View view;
    private ChatDataSource.MessagesCallback chatCallback;
    private int chatsPage;

    public ChatsPresenter(ChatDataSource dataSource,
                          ChatsContract.View view,
                          ChatDataSource.MessagesCallback chatCallback) {
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
    public void dataNotAvailable() {
        System.out.println("Data Not Available");
    }

    @Override
    public void notAuthorized() {
        System.out.println("Not Authorized");
    }

    @Override
    public void unknownResponseCode(String code) {
        System.out.println("Unknown Response Code: " + code);
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
        dataSource.getChats(chatsPage, 5, this);
    }

    @Override
    public void loadChat(Chat chat) {
        if (chat != null) {
            dataSource.getChat(chat, chatCallback);
        }
    }
}
