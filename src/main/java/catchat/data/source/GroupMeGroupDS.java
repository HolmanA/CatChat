package catchat.data.source;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public class GroupMeGroupDS extends DataSource {
    private static GroupMeGroupDS INSTANCE;
    private ObjectMapper mapper;

    private GroupMeGroupDS() {
        mapper = new ObjectMapper();
    }

    public static GroupMeGroupDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupMeGroupDS();
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
            JsonNode responseTree = mapper.readTree(httpResponse.getContent());
            JsonNode metaNode = responseTree.get("meta");

            String responseCode = metaNode.get("code").asText();
            switch (responseCode) {
                case RESPONSE_OK :
                    JsonNode responseNode = responseTree.get("response");
                    callback.onChatsLoaded(parseGroups(responseNode));
                    break;
                default :
                    callback.unknownResponseCode(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            JsonNode responseTree = mapper.readTree(httpResponse.getContent());
            JsonNode metaNode = responseTree.get("meta");

            String responseCode = metaNode.get("code").asText();
            switch (responseCode) {
                case RESPONSE_OK :
                    JsonNode responseNode = responseTree.get("response");
                    callback.onMessagesLoaded(parseMessages(responseNode));
                    break;
                default :
                    callback.unknownResponseCode(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            JsonNode responseTree = mapper.readTree(httpResponse.getContent());
            JsonNode metaNode = responseTree.get("meta");

            String responseCode = metaNode.get("code").asText();
            switch (responseCode) {
                case RESPONSE_CREATED :
                    callback.onMessageSent();
                    break;
                default :
                    callback.unknownResponseCode(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeMessage(String chatId, String messageId, MessagesCallback callback) {

    }

    @Override
    public void unlikeMessage(String chatId, String messageId, MessagesCallback callback) {

    }

    private List<Chat> parseGroups(JsonNode responseNode) {
        List<Chat> groupList = new ArrayList<>();

        if (responseNode.isArray()) {
            for (JsonNode node : responseNode) {
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
                groupList.add(new GroupChat(groupId, name, preview, memberList));
            }
        }
        return groupList;
    }

    private List<Message> parseMessages(JsonNode responseNode) {
        List<Message> messageList = new ArrayList<>();

        JsonNode messages = responseNode.get("messages");
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
