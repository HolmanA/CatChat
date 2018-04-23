package catchat.data.source.connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import java.io.IOException;

/**
 * Created by andrew on 4/21/18.
 */
public abstract class HttpResponseParser<T> {
    public JsonNode getContentFromResponse(HttpResponse response) throws HttpResponseException {
        if (response.isSuccessStatusCode()) {
            JsonNode content = NullNode.getInstance();
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseTree = mapper.readTree(response.getContent());
                content = (responseTree.get("response") != null) ? responseTree.get("response") : content;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        } else {
            throw new HttpResponseException(response);
        }
    }

    public abstract T parseContent(JsonNode content);
}
