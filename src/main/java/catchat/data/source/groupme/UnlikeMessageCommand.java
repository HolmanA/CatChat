package catchat.data.source.groupme;

import catchat.data.source.ApiCommand;
import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class UnlikeMessageCommand extends ApiCommand<Void> {
    private static final String URL = "https://api.groupme.com/v3/messages/";
    private URL url;

    public UnlikeMessageCommand(Listener<Void> listener, String chatId, String messageId) throws IOException {
        super(listener);
        url = new URL(URL + chatId + "/" + messageId + "/unlike");
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
    }

    @Override
    public void buildCommand(String authToken) throws IOException {
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Access-Token", authToken);
    }

    @Override
    protected Void parseContent(JsonNode content) {
        return null;
    }
}
