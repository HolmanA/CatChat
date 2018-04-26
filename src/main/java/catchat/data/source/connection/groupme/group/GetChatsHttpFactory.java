package catchat.data.source.connection.groupme.group;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.GroupChat;
import catchat.data.entities.profile.MemberProfile;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 4/21/18.
 */
public class GetChatsHttpFactory implements HttpFactory<List<Chat>> {
    private static final String URL = "https://api.groupme.com/v3/groups";
    private GenericUrl url;

    public GetChatsHttpFactory(String authToken, int page, int pageSize) {
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
    public HttpResponseParser<List<Chat>> getResponseParser() {
        return new HttpResponseParser<List<Chat>>() {
            @Override
            public List<Chat> parseContent(JsonNode content) {
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
        };
    }
}
