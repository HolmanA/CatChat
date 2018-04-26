package catchat.data.source.groupme;

import catchat.data.source.ApiInteractor;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

/**
 * Created by andrew on 4/26/18.
 */
public class UnlikeMessageInteractor implements ApiInteractor {
    private static final String URL = "https://api.groupme.com/v3/messages/";
    private GenericUrl url;

    public UnlikeMessageInteractor(String authToken, String chatId, String messageId) {
        url = new GenericUrl(URL + chatId + "/" + messageId + "/unlike");
        url.set("token", authToken);
    }

    @Override
    public HttpRequest getRequest() throws IOException {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        return httpRequestFactory.buildPostRequest(url, null);
    }

    @Override
    public Object parseResponse(HttpResponse response) throws HttpResponseException {
        if (response.isSuccessStatusCode()) {
            return null;
        } else {
            throw new HttpResponseException(response);
        }
    }
}
