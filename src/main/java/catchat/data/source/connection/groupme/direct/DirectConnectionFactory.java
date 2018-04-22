package catchat.data.source.connection.groupme.direct;

import catchat.data.source.connection.ConnectionFactory;
import catchat.data.source.connection.HttpFactory;

/**
 * Created by andrew on 4/21/18.
 */
public class DirectConnectionFactory implements ConnectionFactory {
    @Override
    public HttpFactory createGetChatsFactory(String authToken, int page, int pageSize) {
        return new GetChatsHttpFactory(authToken, page, pageSize);
    }

    @Override
    public HttpFactory createGetMessagesFactory(String authToken, String chatId, String beforeMessageId, String afterMessageId) {
        return new GetMessagesHttpFactory(authToken, chatId, beforeMessageId, afterMessageId);
    }

    @Override
    public HttpFactory createSendMessageFactory(String authToken, String chatId, String sourceGUID, String text) {
        return new SendMessageHttpFactory(authToken, chatId, sourceGUID, text);
    }

    @Override
    public HttpFactory createLikeMessageFactory(String authToken, String chatId, String messageId) {
        return new LikeMessageHttpFactory(authToken, chatId, messageId);
    }

    @Override
    public HttpFactory createUnlikeMessageFactory(String authToken, String chatId, String messageId) {
        return new UnlikeMessageHttpFactory(authToken, chatId, messageId);
    }
}
