package catchat.data.source.connection;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;

import java.util.List;

/**
 * Created by andrew on 4/21/18.
 */
public interface ConnectionFactory {
    HttpFactory<List<Chat>> createGetChatsFactory(String authToken, int page, int pageSize);
    HttpFactory<List<Message>> createGetMessagesFactory(String authToken, String chatId, String beforeMessageId, String afterMessageId);
    HttpFactory createSendMessageFactory(String authToken, String chatId, String sourceGUID, String text);
    HttpFactory createLikeMessageFactory(String authToken, String chatId, String messageId);
    HttpFactory createUnlikeMessageFactory(String authToken, String chatId, String messageId);
}
