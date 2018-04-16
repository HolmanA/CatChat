package catchat.data.source.chat;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.BaseDataSource;
import catchat.data.source.BaseCallback;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public abstract class ChatDataSource extends BaseDataSource {
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

    abstract public void getChats(int page, int pageSize, String omit, GetChatsCallback callback);
    abstract public void getMessages(String chatId, String beforeMessageId, String sinceMessageId, GetMessagesCallback callback);
    abstract public void sendMessage(String chatId, String sourceGUID, String messageText, SendMessageCallback callback);
    abstract public void likeMessage(String chatId, String messageId, LikeMessageCallback callback);
    abstract public void unlikeMessage(String chatId, String messageId, UnlikeMessageCallback callback);
}
