package catchat.data.source.groupme;

import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class LikeMessageInteractor extends BaseApiInteractor<Void> {
    private static final String URL = "https://api.groupme.com/v3/messages/";

    public LikeMessageInteractor(String authToken, String chatId, String messageId) throws IOException {
        URL url = new URL(URL + chatId + "/" + messageId + "/like");
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    public Void getContent() {
        return null;
    }

    @Override
    protected Void parseContent(JsonNode content) {
        return null;
    }
}
