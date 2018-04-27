package catchat.data.source.groupme.group;

import catchat.data.source.ApiInteractor;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

/**
 * Created by andrew on 4/26/18.
 */
public class SendGroupMessageInteractor implements ApiInteractor {
    private static final String URL = "https://api.groupme.com/v3/groups/";
    private GenericUrl url;
    private String message;

    public SendGroupMessageInteractor(String authToken, String chatId, String sourceGUID, String text) {
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
    public Object parseResponse(HttpResponse response) throws HttpResponseException {
        if (response.isSuccessStatusCode()) {
            return null;
        } else {
            throw new HttpResponseException(response);
        }
    }
}
