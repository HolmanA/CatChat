package catchat.data.source.groupme.direct;

import catchat.data.source.groupme.BaseApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class SendDirectMessageInteractor extends BaseApiInteractor<Void> {
    private static final String URL = "https://api.groupme.com/v3/direct_messages";

    public SendDirectMessageInteractor(String authToken, String chatId,
                                       String sourceGUID, String text) throws IOException {
        String parameters = "?";
        parameters += "other_user_id=" + chatId;

        URL url = new URL(URL + parameters);
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-Access-Token", authToken);
        connection.setDoOutput(true);

        String message = "{\"direct_message\": {";
        message += "\"source_guid\": \"" + sourceGUID + "\", ";
        message += "\"recipient_id\": \"" + chatId + "\", ";
        message += "\"text\": \"" + text + "\"}}";

        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(message);
        pw.flush();
        pw.close();
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
