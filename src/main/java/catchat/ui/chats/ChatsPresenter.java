package catchat.ui.chats;

import catchat.data.model.Model;
import catchat.data.entities.chat.Chat;
import catchat.data.model.ModelContract;
import catchat.data.model.chatlist.ChatListContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 5/6/18.
 */
public class ChatsPresenter implements ChatsContract.Presenter, ChatListContract.Listener {
    private ModelContract.Model model;
    private ChatsContract.View view;

    public ChatsPresenter(Model model, ChatsContract.View view) {
        this.model = model;
        this.view = view;
        model.getGroupChatListModel().subscribe(this);
        model.getDirectChatListModel().subscribe(this);
    }

    @Override
    public void start() {
        model.reloadAll();
    }

    @Override
    public void reloadGroupChats() {
        model.getGroupChatListModel().reloadChats();
    }

    @Override
    public void reloadDirectChats() {
        model.getDirectChatListModel().reloadChats();
    }

    @Override
    public void loadMoreGroupChats() {
        model.getGroupChatListModel().loadMoreChats();
    }

    @Override
    public void loadMoreDirectChats() {
        model.getDirectChatListModel().loadMoreChats();
    }

    @Override
    public void selectChat(Chat chat) {
        if (chat != null) {
            model.selectChat(chat);
        }
    }

    @Override
    public void chatListChanged() {
        int groupChatsSize = view.getGroupChatsSize();
        view.clearGroupChatList();
        List<Chat> groupChats = model.getGroupChatListModel().getChats();
        if (groupChats == null) {
            groupChats = new ArrayList<>();
        }
        view.setGroupChats(groupChats);
        view.scrollGroupChatsTo(groupChatsSize);

        int directChatsSize = view.getDirectChatsSize();
        view.clearDirectChatList();
        List<Chat> directChats = model.getDirectChatListModel().getChats();
        if (directChats == null) {
            directChats = new ArrayList<>();
        }
        view.setDirectChats(directChats);
        view.scrollDirectChatsTo(directChatsSize);
    }
}
