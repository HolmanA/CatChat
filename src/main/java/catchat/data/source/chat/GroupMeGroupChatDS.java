package catchat.data.source.chat;

import catchat.data.entities.chat.GroupChat;
import catchat.data.entities.chat.Chat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public class GroupMeGroupChatDS extends ChatDataSource {
    private static final String BASE_API_URL = "https://api.groupme.com/v3/";

    public GroupMeGroupChatDS() {}

    @Override
    public void getChats(int page, int pageSize, String omit, GetChatsCallback callback) {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl(BASE_API_URL + "groupchats");
        url.set("token", getAuthToken());
        url.set("page", page);
        url.set("per_page", pageSize);

        try {
            HttpRequest httpRequest = httpRequestFactory.buildGetRequest(url);
            HttpResponse httpResponse = httpRequest.execute();
            String response = httpResponse.parseAsString();
            callback.onChatsLoaded(parseGroups(response));
        } catch (IOException e) {
            // TODO: Should parse out the return code from the http response and alert the callback accordingly
            e.printStackTrace();
            callback.dataNotAvailable();
        }
    }

    @Override
    public void getMessages(String chatId, String beforeMessageId, String sinceMessageId, GetMessagesCallback callback) {

    }

    @Override
    public void sendMessage(String chatId, String sourceGUID, String messageText, SendMessageCallback callback) {

    }

    @Override
    public void likeMessage(String chatId, String messageId, LikeMessageCallback callback) {

    }

    @Override
    public void unlikeMessage(String chatId, String messageId, UnlikeMessageCallback callback) {

    }

    private List<Chat> parseGroups(String json) {
        List<Chat> groupList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode groups = mapper.readTree(json).get("response");
            if (groups.isArray()) {
                for (JsonNode node : groups) {
                    String groupId = node.get("group_id").asText();
                    String name = node.get("name").asText();
                    String preview = node.get("messages").get("preview").get("text").asText();
                    groupList.add(new GroupChat(groupId, name, preview));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groupList;
    }
}
