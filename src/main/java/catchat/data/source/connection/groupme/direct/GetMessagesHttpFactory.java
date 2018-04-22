package catchat.data.source.connection.groupme.direct;

import catchat.data.entities.message.DirectMessage;
import catchat.data.entities.message.Message;
import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/21/18.
 */
public class GetMessagesHttpFactory implements HttpFactory<List<Message>> {
    private static final String URL = "https://api.groupme.com/v3/direct_messages";
    private GenericUrl url;

    public GetMessagesHttpFactory(String authToken, String chatId, String beforeMessageId, String afterMessageId) {
        url = new GenericUrl(URL);
        url.set("token", authToken);
        url.set("other_user_id", chatId);
    }

    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        return httpRequestFactory.buildGetRequest(url);
    }

    @Override
    public HttpResponseParser<List<Message>> getResponseParser() {
        return response -> {
            if (response.isSuccessStatusCode()) {
                List<Message> messages = new ArrayList<>();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode responseTree = mapper.readTree(response.getContent());
                    messages = parse(responseTree.get("response"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return messages;
            } else {
                throw new HttpResponseException(response);
            }
        };
    }

    private List<Message> parse(JsonNode responseNode) {
        List<Message> messageList = new ArrayList<>();
        JsonNode messages = responseNode.get("direct_messages");

        if (messages.isArray()) {
            for (JsonNode message : messages) {
                String messageId = message.get("id").asText();
                String messageGUID = message.get("source_guid").asText();
                String senderId = message.get("sender_id").asText();
                long createdAt = message.get("created_at").asLong();
                String text = message.get("text").asText();
                messageList.add(new DirectMessage(messageId, messageGUID, text, senderId, createdAt));
            }
        }
        return messageList;
    }
}
