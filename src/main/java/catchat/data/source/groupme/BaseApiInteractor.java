package catchat.data.source.groupme;

import catchat.data.source.ApiInteractor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

/**
 * Created by andrew on 4/27/18.
 */
public abstract class BaseApiInteractor<T> implements ApiInteractor<T> {
    protected HttpsURLConnection connection;

    @Override
    public void disconnect() {
        connection.disconnect();
    }

    @Override
    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    @Override
    public String getResponseMessage() throws IOException {
        return connection.getResponseMessage();
    }

    @Override
    public T getContent() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseTree = mapper.readTree(connection.getInputStream());
        JsonNode responseNode = responseTree.get("response");
        JsonNode content = (responseNode != null) ? responseNode : NullNode.getInstance();
        return parseContent(content);
    }

    protected abstract T parseContent(JsonNode content);
}
