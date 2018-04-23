package catchat.data.source.connection.groupme;

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

/**
 * Created by andrew on 4/22/18.
 */
public class UnlikeMessageHttpFactoryTest {
    private HttpFactory httpFactory;

    private String testToken = "test_token";
    private String testId = "test_id";
    private String testMessageId = "test_message_id";

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        httpFactory = new UnlikeMessageHttpFactory(testToken, testId, testMessageId);
    }

    @Test
    public void getRequest_verifyUrlPath() throws IOException {
        String testUrl = "https://api.groupme.com/v3/messages/";
        testUrl += testId + "/" + testMessageId + "/unlike";

        HttpRequest request = httpFactory.getRequest();
        GenericUrl url = request.getUrl();
        String generatedUrl = url.buildAuthority() + url.getRawPath();
        assert(generatedUrl.equals(testUrl));
    }

    @Test
    public void getRequest_verifyParameters() throws IOException {
        HttpRequest request = httpFactory.getRequest();
        GenericUrl url = request.getUrl();
        assert(url.get("token").equals(testToken));
    }

    @Test
    public void getRequest_verifyMethod() throws IOException {
        HttpRequest request = httpFactory.getRequest();
        String method = request.getRequestMethod();
        assert(method.equals(HttpMethods.POST));
    }

    @Test
    public void responseParser_parseContent_nullContent() throws IOException {
        HttpResponseParser parser = httpFactory.getResponseParser();
        parser.parseContent(NullNode.getInstance());
    }
}

