package catchat.data.source.groupme.direct;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.DirectChat;
import catchat.data.entities.profile.MemberProfile;
import catchat.data.entities.profile.Profile;
import catchat.data.source.ApiInteractor;
import catchat.data.source.groupme.BaseApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 4/26/18.
 */
public class GetDirectChatsInteractor extends BaseApiInteractor<List<Chat>> {
    private static final String URL = "https://api.groupme.com/v3/chats";

    public GetDirectChatsInteractor(String authToken, int page, int pageSize) throws IOException {
        String parameters = "?";
        parameters += "page=" + Integer.toString(page);
        parameters += "&per_page=" + Integer.toString(pageSize);

        URL url = new URL(URL + parameters);
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    protected List<Chat> parseContent(JsonNode content) {
        List<Chat> chatList = new ArrayList<>();
        if (content.isArray()) {
            for (JsonNode node : content) {
                Map<String,Profile> memberMap = new HashMap<>();
                String preview = node.get("last_message").get("text").asText();

                JsonNode otherMember = node.get("other_user");
                String otherName = otherMember.get("name").asText();
                String otherId = otherMember.get("id").asText();
                memberMap.put(otherId, new MemberProfile(otherId, otherName, ""));

                chatList.add(new DirectChat(otherId, otherName, preview, memberMap));
            }
        }
        return chatList;
    }
}
