package catchat.data.source;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.DirectChat;
import catchat.data.entities.message.DirectMessage;
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
 * Created by andrew on 4/17/18.
 */
public class GroupMeDirectDS extends DataSource {
    private static GroupMeDirectDS INSTANCE;
    private ObjectMapper mapper;

    private GroupMeDirectDS() {
        mapper = new ObjectMapper();
    }

    public static GroupMeDirectDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupMeDirectDS();
        }
        return INSTANCE;
    }

    @Override
    public void getChats(int page, int pageSize, ChatsCallback callback) {
        HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl(BASE_API_URL + "chats");
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
                    callback.onChatsLoaded(parseChats(responseNode));
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
        GenericUrl url = new GenericUrl(BASE_API_URL + "direct_messages");
        url.set("token", getAuthToken());
        url.set("other_user_id", chatId);

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
        GenericUrl url = new GenericUrl(BASE_API_URL + "direct_messages");
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

    private List<Chat> parseChats(JsonNode responseNode) {
        List<Chat> chatList = new ArrayList<>();
        if (responseNode.isArray()) {
            for (JsonNode node : responseNode) {
                List<Profile> memberList = new ArrayList<>();
                String preview = node.get("last_message").get("text").asText();

                JsonNode otherMember = node.get("other_user");
                String otherName = otherMember.get("name").asText();
                String otherId = otherMember.get("id").asText();
                memberList.add(new MemberProfile(otherId, otherName, ""));

                chatList.add(new DirectChat(otherId, otherName, preview, memberList));
            }
        }
        return chatList;
    }

    private List<Message> parseMessages(JsonNode responseNode) {
        List<Message> messageList = new ArrayList<>();
        JsonNode messages = responseNode.get("direct_messages");

        if (messages.isArray()) {
            for (JsonNode message : messages) {
                String messageId = message.get("id").asText();
                String messageGUID = message.get("source_guid").asText();
                String senderId = message.get("sender_id").asText();
                long createdAt = message.get("created_at").asLong();
                String text = message.get("text").asText();
                messageList.add(new DirectMessage(messageId, messageGUID, text, senderId, createdAt));
            }
        }
        return messageList;
    }

    private byte[] createMessage(String chatId, String sourceGUID, String messageText) {
        String message = "{\"direct_message\": {";
        message += "\"source_guid\": \"" + sourceGUID + "\", ";
        message += "\"recipient_id\": \"" + chatId + "\", ";
        message += "\"text\": \"" + messageText + "\"}}";
        try {
            return message.getBytes("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
