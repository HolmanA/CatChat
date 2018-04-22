package catchat.data.source.connection.groupme.direct;

import catchat.data.entities.chat.Chat;
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
public class GetChatsHttpFactoryTest {
    private HttpFactory<List<Chat>> httpFactory;

    private String testToken = "test_token";
    private int testPage = 2;
    private int testPageSize = 10;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        httpFactory = new GetChatsHttpFactory(testToken, testPage, testPageSize);
    }

    @Test
    public void getRequest_verifyUrl() throws IOException {
        HttpRequest request = httpFactory.getRequest();
        GenericUrl url = request.getUrl();
        assert (url.get("token").equals(testToken));
        assert (url.get("page").equals(testPage));
        assert (url.get("per_page").equals(testPageSize));
    }

    @Test
    public void getRequest_verifyMethod() throws IOException {
        HttpRequest request = httpFactory.getRequest();
        String method = request.getRequestMethod();
        assert (method.equals(HttpMethods.GET));
    }

    @Test
    public void responseParser_parseContent_nullContent() throws IOException {
        HttpResponseParser<List<Chat>> parser = httpFactory.getResponseParser();
        List<Chat> chats = parser.parseContent(NullNode.getInstance());
        assert (chats != null);
        assert (chats.size() == 0);
    }
}

