package catchat.data.source.connection.groupme.direct;

import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

/**
 * Created by andrew on 4/21/18.
 */
public class SendMessageHttpFactory implements HttpFactory {
    private static final String URL = "https://api.groupme.com/v3/direct_messages/";
    private GenericUrl url;
    private String message;

    SendMessageHttpFactory(String authToken, String chatId, String sourceGUID, String text) {
        url = new GenericUrl(URL);
        url.set("token", authToken);
        url.set("other_user_id", chatId);

        message = "{\"direct_message\": {";
        message += "\"source_guid\": \"" + sourceGUID + "\", ";
        message += "\"recipient_id\": \"" + chatId + "\", ";
        message += "\"text\": \"" + text + "\"}}";
    }

    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        byte[] byteContent = message.getBytes("UTF-8");
        HttpContent content = new ByteArrayContent("application/json", byteContent);
        return httpRequestFactory.buildPostRequest(url, content);
    }

    @Override
    public HttpResponseParser getResponseParser() {
        return response -> {
            if (!response.isSuccessStatusCode()) {
                throw new HttpResponseException(response);
            }
            return null;
        };
    }
}
