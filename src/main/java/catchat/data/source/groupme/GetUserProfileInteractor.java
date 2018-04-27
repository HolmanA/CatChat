package catchat.data.source.groupme;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.GroupChat;
import catchat.data.entities.profile.MemberProfile;
import catchat.data.entities.profile.Profile;
import catchat.data.entities.profile.UserProfile;
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
public class GetUserProfileInteractor implements ApiInteractor<Profile> {
    private static final String URL = "https://api.groupme.com/v3/users/me";
    private GenericUrl url;

    public GetUserProfileInteractor(String authToken) {
        url = new GenericUrl(URL);
        url.set("token", authToken);
    }
    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        return httpRequestFactory.buildGetRequest(url);
    }

    @Override
    public Profile parseResponse(HttpResponse response) throws HttpResponseException {
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

    private Profile parseContent(JsonNode content) {
        String userId = content.get("id").asText();
        String userName = content.get("name").asText();
        return new UserProfile(userId, userName);
    }
}
