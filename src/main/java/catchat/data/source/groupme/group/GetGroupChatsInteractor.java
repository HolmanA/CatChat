package catchat.data.source.groupme.group;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.GroupChat;
import catchat.data.entities.profile.MemberProfile;
import catchat.data.entities.profile.Profile;
import catchat.data.source.groupme.BaseApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;

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
public class GetGroupChatsInteractor extends BaseApiInteractor<List<Chat>> {
    private static final String URL = "https://api.groupme.com/v3/groups";

    public GetGroupChatsInteractor(String authToken, int page, int pageSize) throws IOException {
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
        List<Chat> groupList = new ArrayList<>();

        if (content.isArray()) {
            for (JsonNode node : content) {
                String groupId = node.get("group_id").asText();
                String name = node.get("name").asText();
                String preview = node.get("messages").get("preview").get("text").asText();
                JsonNode members = node.get("members");

                Map<String,Profile> memberMap = new HashMap<>();
                if (members.isArray()) {
                    for (JsonNode member : members) {
                        String nickname = member.get("nickname").asText();
                        String userId = member.get("user_id").asText();
                        String memberId = member.get("id").asText();
                        memberMap.put(userId, new MemberProfile(userId, nickname, memberId));
                    }
                }
                groupList.add(new GroupChat(groupId, name, preview, memberMap));
            }
        }
        return groupList;
    }
}
