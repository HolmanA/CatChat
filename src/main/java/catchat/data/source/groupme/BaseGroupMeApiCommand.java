package catchat.data.source.groupme;

import catchat.data.source.ApiCommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

/**
 * Created by andrew on 4/27/18.
 */
public abstract class BaseGroupMeApiCommand<T> implements ApiCommand {
    private static Logger log = LoggerFactory.getLogger(BaseGroupMeApiCommand.class);
    protected HttpsURLConnection connection;
    private Listener<T> listener;

    protected BaseGroupMeApiCommand(Listener<T> listener) {
        this.listener = listener;
    }

    @Override
    public abstract void buildCommand(String authToken) throws IOException;

    @Override
    public void execute() throws IOException, RuntimeException {
        int responseCode = connection.getResponseCode();
        if (200 <= responseCode && responseCode < 300) {
            T content = getContent();
            connection.disconnect();
            listener.complete(content);
        } else {
            log.error("Response Code: {}", responseCode);
            log.error("Response Message: {}", connection.getResponseMessage());
            connection.disconnect();
            throw new RuntimeException(responseCode + ": " + connection.getResponseMessage());
        }
    }

    private T getContent() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseTree = mapper.readTree(connection.getInputStream());
        JsonNode responseNode = responseTree.get("response");
        JsonNode content = (responseNode != null) ? responseNode : NullNode.getInstance();
        log.trace(responseTree.toString());
        return parseContent(content);
    }

    protected abstract T parseContent(JsonNode content);
}
