package catchat.data.source;

import catchat.data.auth.OAuthService;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.groupme.LikeMessageInteractor;
import catchat.data.source.groupme.UnlikeMessageInteractor;
import catchat.data.source.groupme.direct.GetDirectChatsInteractor;
import catchat.data.source.groupme.direct.GetDirectMessagesInteractor;
import catchat.data.source.groupme.direct.SendDirectMessageInteractor;
import catchat.data.source.groupme.group.GetGroupChatsInteractor;
import catchat.data.source.groupme.group.GetGroupMessagesInteractor;
import catchat.data.source.groupme.group.SendGroupMessageInteractor;
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
        ApiInteractor<List<Chat>> interactor = new GetGroupChatsInteractor(
                authService.getAPIToken(), 1, 10);

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
    public void getGroupChat(Chat chat, GetGroupChatCallback callback) {
        callback.onGroupChatLoaded(chat);
    }

    @Override
    public void getGroupMessages(Chat chat, GetMessagesCallback callback) {
        ApiInteractor<List<Message>> interactor = new GetGroupMessagesInteractor(
                authService.getAPIToken(), chat.getId(), "", "");

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            List<Message> messages = interactor.parseResponse(response);
            callback.onMessagesLoaded(messages);
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendGroupMessage(Chat chat, String messageId, String text, SendMessageCallback callback) {
        ApiInteractor interactor = new SendGroupMessageInteractor(
                authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text);

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            interactor.parseResponse(response);
            callback.onMessageSent();
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeGroupMessage(Chat chat, Message message, LikeMessageCallback callback) {
        ApiInteractor interactor = new LikeMessageInteractor(
                authService.getAPIToken(), chat.getId(), message.getId());

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            interactor.parseResponse(response);
            callback.onMessageLiked();
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeGroupMessage(Chat chat, Message message, UnlikeMessageCallback callback) {
        ApiInteractor interactor = new UnlikeMessageInteractor(
                authService.getAPIToken(), chat.getId(), message.getId());

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            interactor.parseResponse(response);
            callback.onMessageUnliked();
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDirectChats(GetChatsCallback callback) {
        ApiInteractor<List<Chat>> interactor = new GetDirectChatsInteractor(
                authService.getAPIToken(), 1, 10);

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
    public void getDirectChat(Chat chat, GetDirectChatCallback callback) {
        callback.onDirectChatLoaded(chat);
    }

    @Override
    public void getDirectMessages(Chat chat, GetMessagesCallback callback) {
        ApiInteractor<List<Message>> interactor = new GetDirectMessagesInteractor(
                authService.getAPIToken(), chat.getId(), "", "");

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            List<Message> messages = interactor.parseResponse(response);
            callback.onMessagesLoaded(messages);
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDirectMessage(Chat chat, String messageId, String text, SendMessageCallback callback) {
        ApiInteractor interactor = new SendDirectMessageInteractor(
                authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text);

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            interactor.parseResponse(response);
            callback.onMessageSent();
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeDirectMessage(Chat chat, Message message, LikeMessageCallback callback) {
        // FIXME: chatId for direct messages is "user_id+other_user_id"
        ApiInteractor interactor = new LikeMessageInteractor(
                authService.getAPIToken(), chat.getId(), message.getId());

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            interactor.parseResponse(response);
            callback.onMessageLiked();
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeDirectMessage(Chat chat, Message message, UnlikeMessageCallback callback) {
        // FIXME: chatId for direct messages is "user_id+other_user_id"
        ApiInteractor interactor = new UnlikeMessageInteractor(
                authService.getAPIToken(), chat.getId(), message.getId());

        try {
            HttpRequest request = interactor.getRequest();
            HttpResponse response = request.execute();
            interactor.parseResponse(response);
            callback.onMessageUnliked();
        } catch (HttpResponseException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserProfile(GetUserProfileCallback callback) {
        //TODO: Finish implementation
    }
}
