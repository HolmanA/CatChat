package catchat.data.model;

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
import catchat.data.entities.chat.Chat;
import catchat.data.source.ApiInvoker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/29/18.
 */
public class Model implements ModelContract.Model, MessageReceiverContract.Listener {
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
        if (selectedChatModel != null && message.getId().equals(selectedChatModel.getChat().getId())) {
            selectedChatModel.reloadMessages();
        }
        groupChatListModel.reloadChats();
        directChatListModel.reloadChats();
    }

    @Override
    public void selectChat(Chat chat) {
        if (selectedChatModel != null) {
            selectedChatModel.unsubscribeAll();
        }
        switch(chat.getType()) {
            case GROUP:
                selectedChatModel = new GroupChatModel(invoker, chat);
                break;
            case DIRECT:
                selectedChatModel = new DirectChatModel(invoker, chat);
                break;
            default:
        }
        for (ModelContract.Listener listener : listeners) {
            listener.selectionChanged();
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
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(ModelContract.Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void unsubscribeAll() {
        listeners.clear();
    }

    @Override
    public void reloadAll() {
        groupChatListModel.reloadChats();
        directChatListModel.reloadChats();
        userProfileModel.loadUserProfile();
        if (selectedChatModel != null) {
            selectedChatModel.reloadMessages();
        }
    }
}
