package catchat.data.source.connection.groupme.direct;

import catchat.data.entities.message.Message;
import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

/**
 * Created by andrew on 4/22/18.
 */
public class GetMessagesHttpFactoryTest {
    private HttpFactory<List<Message>> httpFactory;

    private String testToken = "test_token";
    private String testId = "test id";

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        httpFactory = new GetMessagesHttpFactory(testToken, testId, "", "");
    }

    @Test
    public void getRequest_verifyUrlPath() throws IOException {
        String testUrl = "https://api.groupme.com/v3/direct_messages";
        HttpRequest request = httpFactory.getRequest();
        GenericUrl url = request.getUrl();
        String generatedUrl = url.buildAuthority() + url.getRawPath();
        assert(generatedUrl.equals(testUrl));
    }

    @Test
    public void getRequest_verifyParameters() throws IOException {
        HttpRequest request = httpFactory.getRequest();
        GenericUrl url = request.getUrl();
        assert (url.get("token").equals(testToken));
        assert (url.get("other_user_id").equals(testId));
    }

    @Test
    public void getRequest_verifyMethod() throws IOException {
        HttpRequest request = httpFactory.getRequest();
        String method = request.getRequestMethod();
        assert (method.equals(HttpMethods.GET));
    }

    @Test
    public void responseParser_parseContent_nullContent() throws IOException {
        HttpResponseParser<List<Message>> parser = httpFactory.getResponseParser();
        List<Message> messages = parser.parseContent(NullNode.getInstance());
        assert (messages != null);
        assert (messages.size() == 0);
    }
}

