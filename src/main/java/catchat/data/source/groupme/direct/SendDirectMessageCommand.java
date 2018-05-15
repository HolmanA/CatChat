package catchat.data.source.groupme.direct;

import catchat.data.source.ApiCommand;
import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Created by andrew on 4/26/18.
 */
public class SendDirectMessageCommand extends ApiCommand<Void> {
    private static final String URL = "https://api.groupme.com/v3/direct_messages";
    private static final String BASE_SOURCE_GUID = "com.catchat.guid-";
    private URL url;
    private String message;

    public SendDirectMessageCommand(Listener<Void> listener,  String chatId,
                                    String messageId, String text) throws IOException {
        super(listener);
        String parameters = "?";
        parameters += "other_user_id=" + chatId;

        message = "{\"direct_message\": {";
        message += "\"source_guid\": \"" + BASE_SOURCE_GUID + messageId + "\", ";
        message += "\"recipient_id\": \"" + chatId + "\", ";
        message += "\"text\": \"" + text + "\"}}";

        url = new URL(URL + parameters);
    }

    @Override
    public void buildCommand(String authToken) throws IOException {
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setRequestProperty("X-Access-Token", authToken);

        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(message);
        pw.flush();
        pw.close();
    }

    @Override
    protected Void parseContent(JsonNode content) {
        return null;
    }
}
