package catchat.data.source.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.GroupChat;
import catchat.data.entities.message.GroupMessage;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.MemberProfile;
import catchat.data.entities.profile.Profile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public class GroupMeGroupChatDS extends ChatDataSource {
    private static GroupMeGroupChatDS INSTANCE;
    private ObjectMapper mapper;

    private GroupMeGroupChatDS() {
        mapper = new ObjectMapper();
    }

    public static GroupMeGroupChatDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupMeGroupChatDS();
        }
        return INSTANCE;
    }

    @Override
    public void getChats(int page, int pageSize, ChatsCallback callback) {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl(BASE_API_URL + "groups");
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
    public void getMessages(String chatId, String beforeMessageId, String sinceMessageId, MessagesCallback callback) {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl(BASE_API_URL + "groups/" + chatId + "/messages");
        url.set("token", getAuthToken());

        try {
            HttpRequest httpRequest = httpRequestFactory.buildGetRequest(url);
            HttpResponse httpResponse = httpRequest.execute();
            String response = httpResponse.parseAsString();
            callback.onMessagesLoaded(parseMessages(response));
        } catch (IOException e) {
            // TODO: Should parse out the return code from the http response and alert the callback accordingly
            e.printStackTrace();
            callback.dataNotAvailable();
        }
    }

    @Override
    public void sendMessage(String chatId, String sourceGUID, String messageText, MessagesCallback callback) {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl(BASE_API_URL + "groups/" + chatId + "/messages");
        url.set("token", getAuthToken());

        sourceGUID = BASE_SOURCE_GUID + sourceGUID;
        byte[] content = createMessage(chatId, sourceGUID, messageText);
        HttpContent httpContent = new ByteArrayContent("application/json", content);

        try {
            HttpRequest httpRequest = httpRequestFactory.buildPostRequest(url, httpContent);
            HttpResponse httpResponse = httpRequest.execute();
            String response = httpResponse.parseAsString();
            callback.onMessageSent();
        } catch (IOException e) {
            // TODO: Should parse out the return code from the http response and alert the callback accordingly
            e.printStackTrace();
            callback.dataNotAvailable();
        }
    }

    @Override
    public void likeMessage(String chatId, String messageId, MessagesCallback callback) {

    }

    @Override
    public void unlikeMessage(String chatId, String messageId, MessagesCallback callback) {

    }

    private List<Chat> parseGroups(String json) {
        List<Chat> groupList = new ArrayList<>();

        try {
            JsonNode groups = mapper.readTree(json).get("response");
            if (groups.isArray()) {
                for (JsonNode node : groups) {
                    groupList.add(parseGroupFromJson(node));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    private Chat parseGroupFromJson(JsonNode node) {
        String groupId = node.get("group_id").asText();
        String name = node.get("name").asText();
        String preview = node.get("messages").get("preview").get("text").asText();

        JsonNode members = node.get("members");
        List<Profile> memberList = new ArrayList<>();
        if (members.isArray()) {
            for (JsonNode member : members) {
                String nickname = member.get("nickname").asText();
                String userId = member.get("user_id").asText();
                String memberId = member.get("id").asText();
                memberList.add(new MemberProfile(userId, nickname, memberId));
            }
        }
        return new GroupChat(groupId, name, preview, memberList);
    }

    private List<Message> parseMessages(String json) {
        List<Message> messageList = new ArrayList<>();
        try {
            JsonNode messages = mapper.readTree(json).get("response").get("messages");
            if (messages.isArray()) {
                for (JsonNode message : messages) {
                    String messageId = message.get("id").asText();
                    String messageGUID = message.get("source_guid").asText();
                    String senderId = message.get("sender_id").asText();
                    String text = message.get("text").asText();
                    long createdAt = message.get("created_at").asLong();
                    messageList.add(new GroupMessage(messageId, messageGUID, text, senderId, createdAt));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messageList;
    }

    private byte[] createMessage(String chatId, String sourceGUID, String messageText) {
        String message = "{\"message\": {";
        message += "\"source_guid\": \"" + sourceGUID + "\", ";
        message += "\"text\": \"" + messageText + "\"}}";
        try {
            return message.getBytes("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
