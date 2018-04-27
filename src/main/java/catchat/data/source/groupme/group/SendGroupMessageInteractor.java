package catchat.data.source.groupme.group;

import catchat.data.source.groupme.BaseApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class SendGroupMessageInteractor extends BaseApiInteractor {
    private static final String URL = "https://api.groupme.com/v3/groups/";

    public SendGroupMessageInteractor(String authToken, String chatId,
                                      String sourceGUID, String text) throws IOException {

        URL url = new URL(URL + chatId + "/messages");
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-Access-Token", authToken);
        connection.setDoOutput(true);

        String message = "{\"message\": {";
        message += "\"source_guid\": \"" + sourceGUID + "\", ";
        message += "\"text\": \"" + text + "\"}}";

        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(message);
        pw.flush();
        pw.close();
    }

    @Override
    public Object getContent() {
        return null;
    }

    @Override
    protected Object parseContent(JsonNode content) {
        return null;
    }
}
