package catchat.model.chatlist;

import catchat.data.source.entities.chat.Chat;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.BaseGroupMeApiCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseChatListModel
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseChatListModel implements ChatListContract.Model {
    private List<ChatListContract.Listener> listeners;
    private ApiInvoker invoker;
    private List<Chat> chats;
    private int page;

    BaseChatListModel(ApiInvoker invoker) {
        this.invoker = invoker;
        listeners = new ArrayList<>();
        page = 1;
    }

    @Override
    public void reloadChats() {
        try {
            invoker.execute(getChatsCommand(result -> {
                if (result != null && !result.isEmpty()) {
                    chats = result;
                    page = 1;
                    for (ChatListContract.Listener listener : listeners) {
                        listener.chatListChanged();
                    }
                }
            }, 1, 20));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMoreChats() {
        try {
            invoker.execute(getChatsCommand(result -> {
                if (result != null && !result.isEmpty()) {
                    chats.addAll(result);
                    page++;
                    for (ChatListContract.Listener listener : listeners) {
                        listener.chatListChanged();
                    }
                }
            }, page + 1, 20));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Chat> getChats() {
        return chats;
    }

    @Override
    public void subscribe(ChatListContract.Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(ChatListContract.Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void unsubscribeAll() {
        listeners.clear();
    }

    abstract BaseGroupMeApiCommand<List<Chat>> getChatsCommand(BaseGroupMeApiCommand.Listener<List<Chat>> listener, int page, int pageSize) throws IOException;
}
