package catchat.data.source.connection.groupme;

import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

/**
 * Created by andrew on 4/21/18.
 */
public class LikeMessageHttpFactory implements HttpFactory {
    private static final String URL = "https://api.groupme.com/v3/messages/";
    private GenericUrl url;

    public LikeMessageHttpFactory(String authToken, String chatId, String messageId) {
        url = new GenericUrl(URL + chatId + "/" + messageId + "/like");
        url.set("token", authToken);
    }

    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        return httpRequestFactory.buildPostRequest(url, null);
    }

    @Override
    public HttpResponseParser getResponseParser() {
        return new HttpResponseParser() {
            @Override
            public Object parseContent(JsonNode content) {
                return null;
            }
        };
    }
}
