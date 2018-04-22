package catchat.data.source.connection;

/**
 * Created by andrew on 4/21/18.
 */
public interface ConnectionFactory {
    HttpFactory createGetChatsFactory(String authToken, int page, int pageSize);
    HttpFactory createGetMessagesFactory(String authToken, String chatId, String beforeMessageId, String afterMessageId);
    HttpFactory createSendMessageFactory(String authToken, String chatId, String sourceGUID, String text);
    HttpFactory createLikeMessageFactory(String authToken, String chatId, String messageId);
    HttpFactory createUnlikeMessageFactory(String authToken, String chatId, String messageId);
}
