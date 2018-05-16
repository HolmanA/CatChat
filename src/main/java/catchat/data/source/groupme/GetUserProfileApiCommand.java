package catchat.data.source.groupme;

import catchat.data.entities.profile.UserProfile;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class GetUserProfileApiCommand extends BaseGroupMeApiCommand<UserProfile> {
    private static final Logger log = LoggerFactory.getLogger(GetUserProfileApiCommand.class);
    private static final String URL = "https://api.groupme.com/v3/users/me";
    private URL url;

    public GetUserProfileApiCommand(Listener<UserProfile> listener) throws IOException {
        super(listener);
        url = new URL(URL);
        log.debug("Creating with URL: {}", url);
    }

    @Override
    public void buildCommand(String authToken) throws IOException {
        log.debug("Building with AuthToken: {}", authToken);

        connection = (HttpsURLConnection) url.openConnection();
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
