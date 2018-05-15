package catchat.data.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

/**
 * Created by andrew on 4/27/18.
 */
public abstract class ApiCommand<T> {
    public interface Listener<T> {
        void complete(T result);
    }

    protected HttpsURLConnection connection;
    private Listener<T> listener;

    protected ApiCommand(Listener<T> listener) {
        this.listener = listener;
    }

    public abstract void buildCommand(String authToken) throws IOException;

    public void execute() throws IOException, RuntimeException {
        int responseCode = connection.getResponseCode();
        if (200 <= responseCode && responseCode < 300) {
            T content = getContent();
            connection.disconnect();
            listener.complete(content);
        } else {
            connection.disconnect();
            throw new RuntimeException(responseCode + ": " + connection.getResponseMessage());
        }
    }

    private T getContent() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseTree = mapper.readTree(connection.getInputStream());
        JsonNode responseNode = responseTree.get("response");
        JsonNode content = (responseNode != null) ? responseNode : NullNode.getInstance();
        return parseContent(content);
    }

    protected abstract T parseContent(JsonNode content);
}
