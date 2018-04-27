package catchat.data.source.groupme.direct;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.DirectChat;
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
public class GetDirectChatsInteractor implements ApiInteractor<List<Chat>> {
    private static final String URL = "https://api.groupme.com/v3/chats";
    private GenericUrl url;

    public GetDirectChatsInteractor(String authToken, int page, int pageSize) {
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
