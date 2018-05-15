package catchat.data.source.groupme;

import catchat.data.entities.profile.UserProfile;
import catchat.data.source.ApiCommand;
import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class GetUserProfileCommand extends ApiCommand<UserProfile> {
    private static final String URL = "https://api.groupme.com/v3/users/me";
    private URL url;

    public GetUserProfileCommand(Listener<UserProfile> listener) throws IOException {
        super(listener);
        url = new URL(URL);
    }

    @Override
    public void buildCommand(String authToken) throws IOException {
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    protected UserProfile parseContent(JsonNode content) {
        String userId = content.get("id").asText();
        String userName = content.get("name").asText();
        return new UserProfile(userId, userName);
    }
}
