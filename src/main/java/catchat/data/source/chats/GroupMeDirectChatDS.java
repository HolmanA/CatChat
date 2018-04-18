package catchat.data.source.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.chat.DirectChat;
import catchat.data.entities.message.DirectMessage;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.MemberProfile;
import catchat.data.entities.profile.Profile;
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
 * Created by andrew on 4/17/18.
 */
public class GroupMeDirectChatDS extends ChatDataSource {
    private static GroupMeDirectChatDS INSTANCE;
    private ObjectMapper mapper;

    private GroupMeDirectChatDS() {
        mapper = new ObjectMapper();
    }

    public static GroupMeDirectChatDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupMeDirectChatDS();
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
            String response = httpResponse.parseAsString();
            callback.onChatsLoaded(parseChats(response));
        } catch (IOException e) {
            // TODO: Should parse out the return code from the http response and alert the callback accordingly
            e.printStackTrace();
            callback.dataNotAvailable();
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

    }

    @Override
    public void likeMessage(String chatId, String messageId, MessagesCallback callback) {

    }

    @Override
    public void unlikeMessage(String chatId, String messageId, MessagesCallback callback) {

    }

    private List<Chat> parseChats(String json) {
        List<Chat> chatList = new ArrayList<>();
        try {
            JsonNode chats = mapper.readTree(json).get("response");
            if (chats.isArray()) {
                for (JsonNode node : chats) {
                    chatList.add(parseChatFromJson(node));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chatList;
    }

    private Chat parseChatFromJson(JsonNode node) {
        List<Profile> memberList = new ArrayList<>();

        String preview = node.get("last_message").get("text").asText();

        JsonNode otherMember = node.get("other_user");
        String otherName = otherMember.get("name").asText();
        String otherId = otherMember.get("id").asText();

        memberList.add(new MemberProfile(otherId, otherName, ""));
        return new DirectChat(otherId, otherName, preview, memberList);
    }

    private List<Message> parseMessages(String json) {
        List<Message> messageList = new ArrayList<>();
        try {
            JsonNode messages = mapper.readTree(json).get("response").get("direct_messages");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messageList;
    }
}
