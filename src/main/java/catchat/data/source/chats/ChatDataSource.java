package catchat.data.source.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.BaseCallback;
import catchat.data.source.BaseDataSource;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public abstract class ChatDataSource extends BaseDataSource {
    protected static final String BASE_API_URL = "https://api.groupme.com/v3/";
    protected static final String BASE_SOURCE_GUID = "com.catchat.guid-";

    public interface ChatsCallback extends BaseCallback {
        void onChatsLoaded(List<Chat> chats);
    }

    public interface MessagesCallback extends BaseCallback {
        void setDataSource(ChatDataSource dataSource);
        void onChatLoaded(Chat chat);
        void onMessagesLoaded(List<Message> messages);
        void onMessageSent();
    }

    public void getChat(Chat chat, MessagesCallback callback) {
        callback.setDataSource(this);
        callback.onChatLoaded(chat);
    }

    abstract public void getChats(int page, int pageSize, ChatsCallback callback);
    abstract public void getMessages(String chatId, String beforeMessageId, String sinceMessageId, MessagesCallback callback);
    abstract public void sendMessage(String chatId, String sourceGUID, String messageText, MessagesCallback callback);
    abstract public void likeMessage(String chatId, String messageId, MessagesCallback callback);
    abstract public void unlikeMessage(String chatId, String messageId, MessagesCallback callback);
}
