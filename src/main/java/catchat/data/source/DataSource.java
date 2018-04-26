package catchat.data.source;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public interface DataSource {
    interface ChatsCallback extends Callback {
        void onChatsLoaded(List<Chat> chats);
    }

    interface MessagesCallback extends Callback {
        void setDataSource(DataSource dataSource);
        void onChatLoaded(Chat chat);
        void onMessagesLoaded(List<Message> messages);
        void onMessageSent();
        void onMessageLiked();
        void onMessageUnliked();
    }

    void getChat(Chat chat, MessagesCallback callback);
    void getChats(int page, int pageSize, ChatsCallback callback);
    void getMessages(Chat chat, String beforeMessageId, String sinceMessageId, MessagesCallback callback);
    void sendMessage(String chatId, String sourceGUID, String messageText, MessagesCallback callback);
    void likeMessage(String chatId, String messageId, MessagesCallback callback);
    void unlikeMessage(String chatId, String messageId, MessagesCallback callback);
}
