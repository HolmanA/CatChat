package catchat.data.model.chat;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.ApiCommand;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.LikeMessageCommand;
import catchat.data.source.groupme.UnlikeMessageCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseChatModel
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseChatModel implements ChatContract.Model {
    private List<ChatContract.Listener> listeners;
    private ApiInvoker invoker;
    private Chat chat;
    private List<Message> messages;
    private String oldestMessageId;
    private int sentMessageId;


    BaseChatModel(ApiInvoker invoker, Chat chat) {
        this.invoker = invoker;
        this.chat = chat;
        listeners = new ArrayList<>();
        messages = new ArrayList<>();
        oldestMessageId = "";
        sentMessageId = 1;
    }

    @Override
    public void loadMoreMessages() {
        try {
            invoker.execute(getMessagesCommand(result -> {
                parseGetMessagesResult(result);
            }, chat.getId(), oldestMessageId, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadMessages() {
        try {
            invoker.execute(getMessagesCommand(result -> {
                clearMessages();
                parseGetMessagesResult(result);
            }, chat.getId(), "", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String messageText) {
        try {
            invoker.execute(sendMessageCommand(result -> {
                reloadMessages();
            }, chat.getId(), Integer.toString(sentMessageId++), messageText));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clearMessages() {
        messages.clear();
    }

    @Override
    public void likeMessage(Message message) {
        try {
            invoker.execute(new LikeMessageCommand(result -> {
                reloadMessages();
            }, chat.getId(), message.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeMessage(Message message) {
        try {
            invoker.execute(new UnlikeMessageCommand(result -> {
                reloadMessages();
            }, chat.getId(), message.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void subscribe(ChatContract.Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(ChatContract.Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void unsubscribeAll() {
        listeners.clear();
    }

    abstract ApiCommand<List<Message>> getMessagesCommand(ApiCommand.Listener<List<Message>> listener, String chatId, String beforeId, String sinceId) throws IOException;
    abstract ApiCommand<Void> sendMessageCommand(ApiCommand.Listener<Void> listener, String chatId, String messageId, String messageText) throws IOException;

    void parseGetMessagesResult(List<Message> result) {
        if (result != null && !result.isEmpty()) {
            messages.addAll(result);
            oldestMessageId = messages.get(messages.size() - 1).getId();
            for (ChatContract.Listener listener : listeners) {
                listener.chatChanged();
            }
        }
    }
}
