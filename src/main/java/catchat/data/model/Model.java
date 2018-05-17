package catchat.data.model;

import catchat.data.entities.chat.Chat;
import catchat.data.model.chat.ChatContract;
import catchat.data.model.chat.DirectChatModel;
import catchat.data.model.chat.GroupChatModel;
import catchat.data.model.chatlist.ChatListContract;
import catchat.data.model.chatlist.DirectChatListModel;
import catchat.data.model.chatlist.GroupChatListModel;
import catchat.data.model.userprofile.UserProfileContract;
import catchat.data.model.userprofile.UserProfileModel;
import catchat.data.receiver.message.MessageReceiverContract;
import catchat.data.receiver.message.NotificationMessage;
import catchat.data.source.ApiInvoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/29/18.
 */
public class Model implements ModelContract.Model, ChatContract.Listener, MessageReceiverContract.Listener {
    private static final Logger log = LoggerFactory.getLogger(Model.class);
    private List<ModelContract.Listener> listeners;
    private ApiInvoker invoker;
    private ChatListContract.Model groupChatListModel;
    private ChatListContract.Model directChatListModel;
    private UserProfileContract.Model userProfileModel;
    private ChatContract.Model selectedChatModel;

    public Model(ApiInvoker invoker) {
        this.invoker = invoker;
        listeners = new ArrayList<>();
        groupChatListModel = new GroupChatListModel(invoker);
        directChatListModel = new DirectChatListModel(invoker);
        userProfileModel = new UserProfileModel(invoker);
    }

    @Override
    public void messageReceived(NotificationMessage message) {
        log.debug("Message Received: {}", message);
        String userProfileId = getUserProfileModel().getUserProfile().getId();
        if (!userProfileId.equals(message.getSenderId())) {
            log.trace("Reloading Chats");
            String selectedChatId = (selectedChatModel == null) ? "" : selectedChatModel.getChat().getId();
            if (selectedChatId.equals(message.getId())) {
                log.trace("Reloading Messages");
                selectedChatModel.reloadMessages();
            }
            groupChatListModel.reloadChats();
            directChatListModel.reloadChats();
        }
    }

    @Override
    public void selectChat(Chat chat) {
        log.debug("Chat Selected: {}", chat.getName());
        String prevSelectedId = (selectedChatModel == null) ? "" : selectedChatModel.getChat().getId();
        String newSelectedId = chat.getId();

        log.trace("Old: {}", prevSelectedId);
        log.trace("New: {}", newSelectedId);
        if (!prevSelectedId.equals(newSelectedId)) {
            switch (chat.getType()) {
                case GROUP:
                    selectedChatModel = new GroupChatModel(invoker, chat);
                    break;
                case DIRECT:
                    selectedChatModel = new DirectChatModel(invoker, chat);
                    break;
                default:
            }
            selectedChatModel.subscribe(this);
            for (ModelContract.Listener listener : listeners) {
                log.trace("Selection Changed: {}", listener);
                listener.selectionChanged();
            }
        } else {
            for (ModelContract.Listener listener : listeners) {
                log.trace("Same Selection: {}", listener);
                listener.sameChatSelected();
            }
        }
    }

    @Override
    public ChatListContract.Model getGroupChatListModel() {
        return groupChatListModel;
    }

    @Override
    public ChatListContract.Model getDirectChatListModel() {
        return directChatListModel;
    }

    @Override
    public UserProfileContract.Model getUserProfileModel() {
        return userProfileModel;
    }

    @Override
    public ChatContract.Model getSelectedChatModel() {
        return selectedChatModel;
    }

    @Override
    public void subscribe(ModelContract.Listener listener) {
        log.trace("Subscribe {}", listener);
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(ModelContract.Listener listener) {
        log.trace("Unsubscribe {}", listener);
        listeners.remove(listener);
    }

    @Override
    public void unsubscribeAll() {
        log.trace("Unsubscribe All");
        listeners.clear();
    }

    @Override
    public void reloadAll() {
        log.debug("Reload All");
        groupChatListModel.reloadChats();
        directChatListModel.reloadChats();
        userProfileModel.loadUserProfile();
        if (selectedChatModel != null) {
            selectedChatModel.reloadMessages();
        }
    }

    @Override
    public void messageSent() {
        log.debug("Message Sent");
        groupChatListModel.reloadChats();
        directChatListModel.reloadChats();
    }

    /**
     * Currently unused
     */
    @Override
    public void chatChanged() {
    }
}
