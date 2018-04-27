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
public class GetUserProfileInteractor extends BaseApiInteractor<Profile> {
    private static final String URL = "https://api.groupme.com/v3/users/me";

    public GetUserProfileInteractor(String authToken) throws IOException {
        URL url = new URL(URL);
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    protected Profile parseContent(JsonNode content) {
        String userId = content.get("id").asText();
        String userName = content.get("name").asText();
        return new UserProfile(userId, userName);
    }
}
