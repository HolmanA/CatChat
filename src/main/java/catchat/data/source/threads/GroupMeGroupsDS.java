package catchat.data.source.threads;


import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

/**
 * Created by andrew on 4/13/18.
 */
public class GroupMeGroupsDS extends ThreadDataSource {
    private static final String BASE_API_URL = "https://api.groupme.com/v3/";

    public GroupMeGroupsDS() {}

    @Override
    public void getThreads(int page, int pageSize, String omit, GetThreadsCallback callback) {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl(BASE_API_URL + "groups");
        url.set("token", getAuthToken());
        url.set("page", page);
        url.set("per_page", pageSize);
        try {
            HttpRequest httpRequest = httpRequestFactory.buildGetRequest(url);
            System.out.print(httpRequest.execute().parseAsString());
        } catch (IOException e) {
            e.printStackTrace();
            callback.dataNotAvailable();
        }
    }

    @Override
    public void getMessages(String threadId, String beforeMessageId, String sinceMessageId, GetMessagesCallback callback) {

    }

    @Override
    public void sendMessage(String threadId, String sourceGUID, String messageText, SendMessageCallback callback) {

    }

    @Override
    public void likeMessage(String threadId, String messageId, LikeMessageCallback callback) {

    }

    @Override
    public void unlikeMessage(String threadId, String messageId, UnlikeMessageCallback callback) {

    }
}
