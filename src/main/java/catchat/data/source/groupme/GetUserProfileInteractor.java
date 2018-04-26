package catchat.data.source.groupme;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.GroupChat;
import catchat.data.entities.profile.MemberProfile;
import catchat.data.entities.profile.Profile;
import catchat.data.source.ApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 4/26/18.
 */
public class GetUserProfileInteractor implements ApiInteractor<List<Chat>> {
    private static final String URL = "https://api.groupme.com/v3/groups";
    private GenericUrl url;

    public GetUserProfileInteractor(String authToken, int page, int pageSize) {
        url = new GenericUrl(URL);
        url.set("token", authToken);
        url.set("page", page);
        url.set("per_page", pageSize);
    }
    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        return httpRequestFactory.buildGetRequest(url);
    }

    @Override
    public List<Chat> parseResponse(HttpResponse response) throws HttpResponseException {
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

    private List<Chat> parseContent(JsonNode content) {
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
