package catchat.data.source;

import catchat.data.auth.OAuthService;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.DataSource;
import catchat.data.source.groupme.group.GetGroupChatsInteractor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by andrew on 4/26/18.
 */
public class GroupMeDataSource implements DataSource {
    private static final String BASE_SOURCE_GUID = "com.catchat.guid-";
    private OAuthService authService;

    public GroupMeDataSource(OAuthService authService) {
        this.authService = authService;
    }

    @Override
    public void getGroupChats(GetChatsCallback callback) {
        ApiInteractor<List<Chat>> interactor = new GetGroupChatsInteractor(authService.getAPIToken(), 1, 10);

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            List<Chat> chats = interactor.parseResponse(response);
            callback.onChatsLoaded(chats);
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGroupChat(Chat chat, GetChatCallback callback) {

    }

    @Override
    public void getGroupMessages(Chat chat, GetMessagesCallback callback) {

    }

    @Override
    public void sendGroupMessage(Chat chat, Message message, SendMessageCallback callback) {

    }

    @Override
    public void likeGroupMessage(Chat chat, Message message, LikeMessageCallback callback) {

    }

    @Override
    public void unlikeGroupMessage(Chat chat, Message message, UnlikeMessageCallback callback) {

    }

    @Override
    public void getDirectChats(GetChatsCallback callback) {

    }

    @Override
    public void getDirectChat(Chat chat, GetChatCallback callback) {

    }

    @Override
    public void getDirectMessages(Chat chat, GetMessagesCallback callback) {

    }

    @Override
    public void sendDirectMessage(Chat chat, Message message, SendMessageCallback callback) {

    }

    @Override
    public void likeDirectMessage(Chat chat, Message message, LikeMessageCallback callback) {

    }

    @Override
    public void unlikeDirectMessage(Chat chat, Message message, UnlikeMessageCallback callback) {

    }

    @Override
    public void getUserProfile(GetUserProfileCallback callback) {

    }
}
