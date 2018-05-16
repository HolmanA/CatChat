package catchat.data.source.groupme;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class LikeMessageApiCommand extends BaseGroupMeApiCommand<Void> {
    private static final Logger log = LoggerFactory.getLogger(LikeMessageApiCommand.class);
    private static final String URL = "https://api.groupme.com/v3/messages/";
    private URL url;

    public LikeMessageApiCommand(Listener<Void> listener, String chatId, String messageId) throws IOException {
        super(listener);
        url = new URL(URL + chatId + "/" + messageId + "/like");
        log.debug("Creating with URL: {}", url);
    }

    @Override
    public void buildCommand(String authToken) throws IOException {
        log.debug("Building with AuthToken: {}", authToken);

        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    protected Void parseContent(JsonNode content) {
        return null;
    }
}
