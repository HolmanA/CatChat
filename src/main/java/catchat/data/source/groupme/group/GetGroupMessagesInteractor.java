package catchat.data.source.groupme.group;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.GroupMessage;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.source.ApiInteractor;
import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/21/18.
 */
public class GetGroupMessagesInteractor implements ApiInteractor<List<Message>> {
    private static final String URL = "https://api.groupme.com/v3/groups/";
    private GenericUrl url;

    public GetGroupMessagesInteractor(String authToken, String chatId, String beforeMessageId, String afterMessageId) {
        url = new GenericUrl(URL + chatId + "/messages");
        url.set("token", authToken);
    }

    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        return httpRequestFactory.buildGetRequest(url);
    }

    @Override
    public List<Message> parseResponse(HttpResponse response) throws HttpResponseException {
        if (response.isSuccessStatusCode()) {
            JsonNode content = NullNode.getInstance();
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseTree = mapper.readTree(response.getContent());
                content = (responseTree.get("response") != null) ? responseTree.get("response") : content;
                response.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parseContent(content);
        } else {
            throw new HttpResponseException(response);
        }
    }

    private List<Message> parseContent(JsonNode content) {
        List<Message> messageList = new ArrayList<>();

        JsonNode messages = content.get("messages");
        if (messages != null && messages.isArray()) {
            for (JsonNode message : messages) {
                String messageId = message.get("id").asText();
                String messageGUID = message.get("source_guid").asText();
                String senderName = message.get("name").asText();
                String text = message.get("text").asText();
                long createdAt = message.get("created_at").asLong();
                List<String> likeList = new ArrayList<>();
                JsonNode likes = message.get("favorited_by");
                for (JsonNode like : likes) {
                    likeList.add(like.asText());
                }
                messageList.add(new GroupMessage(messageId, messageGUID, text, senderName, createdAt, likeList));
            }
        }
        return messageList;
    }
}
