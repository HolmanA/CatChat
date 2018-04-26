package catchat.data.source.groupme;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.GroupChat;
import catchat.data.entities.profile.MemberProfile;
import catchat.data.entities.profile.Profile;
import catchat.data.source.ApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 4/26/18.
 */
public class LikeMessageInteractor implements ApiInteractor {
    private static final String URL = "https://api.groupme.com/v3/messages/";
    private GenericUrl url;

    public LikeMessageInteractor(String authToken, String chatId, String messageId) {
        url = new GenericUrl(URL + chatId + "/" + messageId + "/like");
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
