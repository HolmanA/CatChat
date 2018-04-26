package catchat.data.source.groupme.direct;

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
public class SendDirectMessageInteractor implements ApiInteractor {
    private static final String URL = "https://api.groupme.com/v3/direct_messages";
    private GenericUrl url;
    private String message;

    public SendDirectMessageInteractor(String authToken, String chatId, String sourceGUID, String text) {
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
    public Object parseResponse(HttpResponse response) throws HttpResponseException {
        if (response.isSuccessStatusCode()) {
            return null;
        } else {
            throw new HttpResponseException(response);
        }
    }
}
