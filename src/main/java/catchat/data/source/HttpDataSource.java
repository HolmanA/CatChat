package catchat.data.source;

import catchat.data.auth.OAuthService;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.connection.ConnectionFactory;
import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public class HttpDataSource implements DataSource {
    private static final String BASE_SOURCE_GUID = "com.catchat.guid-";
    private OAuthService authService;
    private ConnectionFactory connectionFactory;

    public HttpDataSource(OAuthService authService, ConnectionFactory connectionFactory) {
        this.authService = authService;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void getChat(Chat chat, MessagesCallback callback) {
        callback.setDataSource(this);
        callback.onChatLoaded(chat);
    }

    @Override
    public void getChats(int page, int pageSize, ChatsCallback callback) {
        HttpFactory<List<Chat>> httpFactory = connectionFactory.createGetChatsFactory(
                authService.getAPIToken(), page, pageSize);

        HttpResponseParser<List<Chat>> parser = httpFactory.getResponseParser();

        try {
            HttpRequest httpRequest = httpFactory.getRequest();
            HttpResponse httpResponse = httpRequest.execute();
            JsonNode content = parser.getContentFromResponse(httpResponse); // Might throw HttpResponseException
            List<Chat> chats = parser.parseContent(content);
            callback.onChatsLoaded(chats);
        } catch (HttpResponseException e) {
            handleBaseResponseException(e, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMessages(String chatId, String beforeMessageId, String sinceMessageId, MessagesCallback callback) {
        HttpFactory<List<Message>> httpFactory = connectionFactory.createGetMessagesFactory(
                authService.getAPIToken(), chatId, beforeMessageId, sinceMessageId);

        HttpResponseParser<List<Message>> parser = httpFactory.getResponseParser();

        try {
            HttpRequest httpRequest = httpFactory.getRequest();
            HttpResponse httpResponse = httpRequest.execute();
            JsonNode content = parser.getContentFromResponse(httpResponse); // Might throw HttpResponseException
            List<Message> messages = parser.parseContent(content);
            callback.onMessagesLoaded(messages);
        } catch (HttpResponseException e) {
            handleBaseResponseException(e, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String chatId, String sourceGUID, String messageText, MessagesCallback callback) {
        HttpFactory httpFactory = connectionFactory.createSendMessageFactory(
                authService.getAPIToken(), chatId, BASE_SOURCE_GUID + sourceGUID, messageText);

        HttpResponseParser parser = httpFactory.getResponseParser();

        try {
            HttpRequest httpRequest = httpFactory.getRequest();
            HttpResponse httpResponse = httpRequest.execute();
            JsonNode content = parser.getContentFromResponse(httpResponse); // Might throw HttpResponseException
            parser.parseContent(content);
            callback.onMessageSent();
        } catch (HttpResponseException e) {
            handleBaseResponseException(e, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
   }

    @Override
    public void likeMessage(String chatId, String messageId, MessagesCallback callback) {
        HttpFactory httpFactory = connectionFactory.createLikeMessageFactory(
                authService.getAPIToken(), chatId, messageId);

        HttpResponseParser parser = httpFactory.getResponseParser();

        try {
            HttpRequest httpRequest = httpFactory.getRequest();
            HttpResponse httpResponse = httpRequest.execute();
            JsonNode content = parser.getContentFromResponse(httpResponse); // Might throw HttpResponseException
            parser.parseContent(content);
        } catch (HttpResponseException e) {
            handleBaseResponseException(e, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeMessage(String chatId, String messageId, MessagesCallback callback) {
        HttpFactory httpFactory = connectionFactory.createUnlikeMessageFactory(
                authService.getAPIToken(), chatId, messageId);

        HttpResponseParser parser = httpFactory.getResponseParser();

        try {
            HttpRequest httpRequest = httpFactory.getRequest();
            HttpResponse httpResponse = httpRequest.execute();
            JsonNode content = parser.getContentFromResponse(httpResponse); // Might throw HttpResponseException
            parser.parseContent(content);
        } catch (HttpResponseException e) {
            handleBaseResponseException(e, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBaseResponseException(HttpResponseException exception, Callback callback) {
        switch (exception.getStatusCode()) {
            default :
                callback.unknownResponseCode(exception.getStatusCode() + ": " + exception.getStatusMessage());
        }
    }
}
