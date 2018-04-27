package catchat.data.source.groupme.group;

import catchat.data.entities.message.GroupMessage;
import catchat.data.entities.message.Message;
import catchat.data.source.ApiInteractor;
import catchat.data.source.groupme.BaseApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/21/18.
 */
public class GetGroupMessagesInteractor extends BaseApiInteractor<List<Message>> {
    private static final String URL = "https://api.groupme.com/v3/groups/";

    public GetGroupMessagesInteractor(String authToken, String chatId,
                                      String beforeId, String sinceId,
                                      String afterId, int limit) throws IOException {
        String parameters = "?";
        parameters += "before_id=" + beforeId;
        parameters += "&since_id=" + sinceId;
        parameters += "&after_id=" + afterId;
        parameters += "&limit=" + limit;

        URL url = new URL(URL + chatId + "/messages" + parameters);
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    protected List<Message> parseContent(JsonNode content) {
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
