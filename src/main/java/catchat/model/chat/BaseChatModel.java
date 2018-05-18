package catchat.model.chat;

import catchat.data.source.entities.chat.Chat;
import catchat.data.source.entities.message.Message;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.BaseGroupMeApiCommand;
import catchat.data.source.groupme.LikeMessageApiCommand;
import catchat.data.source.groupme.UnlikeMessageApiCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(BaseChatModel.class);
    private static int sentMessageId = 1;
    private List<ChatContract.Listener> listeners;
    private ApiInvoker invoker;
    private Chat chat;
    private List<Message> messages;
    private String oldestMessageId;


    BaseChatModel(ApiInvoker invoker, Chat chat) {
        this.invoker = invoker;
        this.chat = chat;
        listeners = new ArrayList<>();
        messages = new ArrayList<>();
        oldestMessageId = "";
        log.debug("Chat model created for chat: {}", chat.getName());
    }

    @Override
    public void loadMoreMessages() {
        log.debug("Loading more messages");
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
        log.debug("Reloading messages");
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
        log.debug("Sending message");
        try {
            invoker.execute(sendMessageCommand(result -> {
                for (ChatContract.Listener listener : listeners) {
                    log.trace("Message Sent: {}", listener);
                    listener.messageSent();
                }
            }, chat.getId(), Integer.toString(sentMessageId++), messageText));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clearMessages() {
        log.debug("Clearing messages");
        messages.clear();
    }

    @Override
    public void likeMessage(Message message) {
        log.debug("Liking message");
        try {
            invoker.execute(new LikeMessageApiCommand(result -> {
                reloadMessages();
            }, chat.getId(), message.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeMessage(Message message) {
        log.debug("Unliking message");
        try {
            invoker.execute(new UnlikeMessageApiCommand(result -> {
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
        log.debug("Subscribing {}", listener);
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(ChatContract.Listener listener) {
        log.debug("Unsubscribing {}", listener);
        listeners.remove(listener);
    }

    @Override
    public void unsubscribeAll() {
        log.debug("Unsubscribing All");
        listeners.clear();
    }

    abstract BaseGroupMeApiCommand<List<Message>> getMessagesCommand(BaseGroupMeApiCommand.Listener<List<Message>> listener, String chatId, String beforeId, String sinceId) throws IOException;

    abstract BaseGroupMeApiCommand<Void> sendMessageCommand(BaseGroupMeApiCommand.Listener<Void> listener, String chatId, String messageId, String messageText) throws IOException;

    void parseGetMessagesResult(List<Message> result) {
        log.debug("Setting messages");
        if (result != null && !result.isEmpty()) {
            messages.addAll(result);
            oldestMessageId = messages.get(messages.size() - 1).getId();
            for (ChatContract.Listener listener : listeners) {
                log.trace("Chat Changed: {}", listener);
                listener.chatChanged();
            }
        }
    }
}
