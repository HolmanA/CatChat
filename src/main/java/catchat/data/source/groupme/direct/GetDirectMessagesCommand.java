package catchat.data.source.groupme.direct;

import catchat.data.entities.message.DirectMessage;
import catchat.data.entities.message.Message;
import catchat.data.source.ApiCommand;
import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/21/18.
 */
public class GetDirectMessagesCommand extends ApiCommand<List<Message>> {
    private static final String URL = "https://api.groupme.com/v3/direct_messages";
    private URL url;

    public GetDirectMessagesCommand(Listener<List<Message>> listener, String chatId,
                                    String beforeId, String sinceId) throws IOException {
        super(listener);
        String parameters = "?";
        parameters += "other_user_id=" + chatId;
        parameters += "&before_id=" + beforeId;
        parameters += "&since_id=" + sinceId;

        url = new URL(URL + parameters);
    }

    @Override
    public void buildCommand(String authToken) throws IOException {
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    protected List<Message> parseContent(JsonNode content) {
        List<Message> messageList = new ArrayList<>();
        JsonNode messages = content.get("direct_messages");

        if (messages != null && messages.isArray()) {
            for (JsonNode message : messages) {
                String messageId = message.get("id").asText();
                String senderAvatar = message.get("avatar_url").asText();
                String messageGUID = message.get("source_guid").asText();
                String senderName = message.get("name").asText();
                long createdAt = message.get("created_at").asLong();
                String text = message.get("text").asText();
                List<String> likeList = new ArrayList<>();
                JsonNode likes = message.get("favorited_by");
                for (JsonNode like : likes) {
                    likeList.add(like.asText());
                }
                messageList.add(new DirectMessage(messageId, senderAvatar, messageGUID, text, senderName, createdAt, likeList));
            }
        }
        return messageList;
    }
}
