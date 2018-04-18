package catchat.data.source.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.BaseDataSource;
import catchat.data.source.BaseCallback;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public abstract class ChatDataSource extends BaseDataSource {
    protected static final String BASE_API_URL = "https://api.groupme.com/v3/";

    public interface GetChatCallback extends BaseCallback {
        void onChatLoaded(Chat chat);
        void setDataSource(ChatDataSource dataSource);
    }

    public interface GetChatsCallback extends BaseCallback {
        void onChatsLoaded(List<Chat> chats);
    }

    public interface GetMessagesCallback extends BaseCallback {
        void onMessagesLoaded(List<Message> messages);
    }

    public interface SendMessageCallback extends BaseCallback {
        void onMessageSent();
    }

    public interface LikeMessageCallback extends BaseCallback {
        void onMessageLiked();
    }

    public interface UnlikeMessageCallback extends BaseCallback {
        void onMessageUnliked();
    }

    public void getChat(Chat chat, GetChatCallback callback) {
        callback.onChatLoaded(chat);
        callback.setDataSource(this);
    }

    abstract public void getChats(int page, int pageSize, GetChatsCallback callback);
    abstract public void getMessages(String chatId, String beforeMessageId, String sinceMessageId, GetMessagesCallback callback);
    abstract public void sendMessage(String chatId, String sourceGUID, String messageText, SendMessageCallback callback);
    abstract public void likeMessage(String chatId, String messageId, LikeMessageCallback callback);
    abstract public void unlikeMessage(String chatId, String messageId, UnlikeMessageCallback callback);
}
