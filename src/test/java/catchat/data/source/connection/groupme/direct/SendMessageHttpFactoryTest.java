package catchat.data.source.connection.groupme.direct;

import catchat.data.source.connection.HttpFactory;
import catchat.data.source.connection.HttpResponseParser;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by andrew on 4/22/18.
 */
public class SendMessageHttpFactoryTest {
    private HttpFactory httpFactory;

    private String testToken = "test_token";
    private String testId = "test id";
    private String testGUID = "test guid";
    private String testText = "test text";

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        httpFactory = new SendMessageHttpFactory(testToken, testId, testGUID, testText);
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
        assert (method.equals(HttpMethods.POST));
    }

    @Test
    public void getRequest_verifyMessage() throws IOException {
        String testMessage = "{\"direct_message\": {";
        testMessage += "\"source_guid\": \"" + testGUID + "\", ";
        testMessage += "\"recipient_id\": \"" + testId + "\", ";
        testMessage += "\"text\": \"" + testText + "\"}}";

        HttpRequest request = httpFactory.getRequest();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        request.getContent().writeTo(baos);
        String message = baos.toString();
        baos.close();

        assert(message.equals(testMessage));
    }

    @Test
    public void responseParser_parseContent_nullContent() throws IOException {
        HttpResponseParser parser = httpFactory.getResponseParser();
        parser.parseContent(NullNode.getInstance());
    }
}

