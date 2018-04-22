package catchat.data.source.connection.groupme.group;

import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

/**
 * Created by andrew on 4/21/18.
 */
public class SendMessageHttpFactory implements HttpFactory {
    private static final String URL = "https://api.groupme.com/v3/groups/";
    private GenericUrl url;
    private String message;

    public SendMessageHttpFactory(String authToken, String chatId, String sourceGUID, String text) {
        url = new GenericUrl(URL + chatId + "/messages");
        url.set("token", authToken);

        message = "{\"message\": {";
        message += "\"source_guid\": \"" + sourceGUID + "\", ";
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
