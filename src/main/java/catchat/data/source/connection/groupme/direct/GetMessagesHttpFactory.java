package catchat.data.source.connection.groupme.direct;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.DirectMessage;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
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
    private Chat chat;

    public GetMessagesHttpFactory(String authToken, Chat chat, String beforeMessageId, String afterMessageId) {
        this.chat = chat;
        url = new GenericUrl(URL);
        url.set("token", authToken);
        url.set("other_user_id", this.chat.getId());
    }

    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        return httpRequestFactory.buildGetRequest(url);
    }

    @Override
    public HttpResponseParser<List<Message>> getResponseParser() {
        return new HttpResponseParser<List<Message>>() {
            @Override
            public List<Message> parseContent(JsonNode content) {
                List<Message> messageList = new ArrayList<>();
                JsonNode messages = content.get("direct_messages");

                if (messages != null && messages.isArray()) {
                    for (JsonNode message : messages) {
                        String messageId = message.get("id").asText();
                        String messageGUID = message.get("source_guid").asText();
                        String senderName = message.get("name").asText();
                        long createdAt = message.get("created_at").asLong();
                        String text = message.get("text").asText();
                        List<Profile> likeList = new ArrayList<>();
                        JsonNode likes = message.get("favorited_by");
                        for (JsonNode like : likes) {
                            Profile temp = chat.findMemberById(like.asText());
                            if (temp != null) {
                                likeList.add(temp);
                            }
                        }
                        messageList.add(new DirectMessage(messageId, messageGUID, text, senderName, createdAt, likeList));
                    }
                }
                return messageList;
            }
        };
    }
}
