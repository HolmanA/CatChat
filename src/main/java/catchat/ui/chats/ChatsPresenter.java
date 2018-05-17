package catchat.ui.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.model.Model;
import catchat.data.model.ModelContract;
import catchat.data.model.chatlist.ChatListContract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 5/6/18.
 */
public class ChatsPresenter implements ChatsContract.Presenter, ChatListContract.Listener {
    private static final Logger log = LoggerFactory.getLogger(ChatsPresenter.class);
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
        log.debug("Starting");
        model.reloadAll();
    }

    @Override
    public void selectGroupChatsTitle() {
        if (view.groupChatsVisible()) {
            log.debug("Hiding Group Chats");
            view.hideGroupChats();
        } else {
            log.debug("Showing Group Chats");
            view.showGroupChats();
        }
    }

    @Override
    public void selectDirectChatsTitle() {
        if (view.directChatsVisible()) {
            log.debug("Hiding Direct Chats");
            view.hideDirectChats();
        } else {
            log.debug("Showing Direct Chats");
            view.showDirectChats();
        }
    }

    @Override
    public void reloadGroupChats() {
        log.debug("Reloading Group Chats");
        model.getGroupChatListModel().reloadChats();
    }

    @Override
    public void reloadDirectChats() {
        log.debug("Reloading Direct Chats");
        model.getDirectChatListModel().reloadChats();
    }

    @Override
    public void loadMoreGroupChats() {
        log.debug("Loading more Group Chats");
        model.getGroupChatListModel().loadMoreChats();
    }

    @Override
    public void loadMoreDirectChats() {
        log.debug("Loading more Direct Chats");
        model.getDirectChatListModel().loadMoreChats();
    }

    @Override
    public void selectChat(Chat chat) {
        if (chat != null) {
            log.debug("Selecting Chat: {}", chat.getName());
            switch (chat.getType()) {
                case GROUP:
                    log.debug("Clearing Direct Chat Selection");
                    view.clearDirectChatSelection();
                    break;
                case DIRECT:
                    log.debug("Clearing Group Chat Selection");
                    view.clearGroupChatSelection();
                    break;
                default:
            }
            model.selectChat(chat);
        }
    }

    @Override
    public void chatListChanged() {
        log.debug("Chat List Changed");
        int groupChatsSize = view.getGroupChatsSize();
        view.clearGroupChatList();
        List<Chat> groupChats = model.getGroupChatListModel().getChats();
        if (groupChats == null) {
            groupChats = new ArrayList<>();
        }
        view.setGroupChats(groupChats);
        view.scrollGroupChatsTo(groupChatsSize);

        log.trace("Set Group Chats To: {}", groupChats);
        log.trace("Scrolled Group Chats To: {}", groupChatsSize);

        int directChatsSize = view.getDirectChatsSize();
        view.clearDirectChatList();
        List<Chat> directChats = model.getDirectChatListModel().getChats();
        if (directChats == null) {
            directChats = new ArrayList<>();
        }
        view.setDirectChats(directChats);
        view.scrollDirectChatsTo(directChatsSize);

        log.trace("Set Direct Chats To: {}", directChats);
        log.trace("Scrolled Direct Chats To: {}", directChatsSize);
    }
}
