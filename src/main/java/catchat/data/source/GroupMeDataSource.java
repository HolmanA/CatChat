package catchat.data.source;

import catchat.data.DataMediator;
import catchat.data.authentication.OAuthService;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.data.source.groupme.GetUserProfileInteractor;
import catchat.data.source.groupme.LikeMessageInteractor;
import catchat.data.source.groupme.UnlikeMessageInteractor;
import catchat.data.source.groupme.direct.GetDirectChatsInteractor;
import catchat.data.source.groupme.direct.GetDirectMessagesInteractor;
import catchat.data.source.groupme.direct.SendDirectMessageInteractor;
import catchat.data.source.groupme.group.GetGroupChatsInteractor;
import catchat.data.source.groupme.group.GetGroupMessagesInteractor;
import catchat.data.source.groupme.group.SendGroupMessageInteractor;

import java.io.IOException;

/**
 * Created by andrew on 4/26/18.
 */
public class GroupMeDataSource implements DataSource {
    private static final String BASE_SOURCE_GUID = "com.catchat.guid-";
    private OAuthService authService;
    private DataMediator dataMediator;
    private Profile userProfile;

    public GroupMeDataSource(OAuthService authService, DataMediator dataMediator) {
        this.authService = authService;
        this.dataMediator = dataMediator;
    }

    @Override
    public void getGroupChats(int page, int pageSize) {
        try {
            dataMediator.chatsLoaded(execute(new GetGroupChatsInteractor(
                    authService.getAPIToken(), page, pageSize)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGroupChat(Chat chat) {
        dataMediator.chatLoaded(chat);
    }

    @Override
    public void getGroupMessages(Chat chat, Message lastMessage) {
        try {
            dataMediator.messagesLoaded(execute(new GetGroupMessagesInteractor(
                    authService.getAPIToken(), chat.getId(),
                    (lastMessage == null) ? "" : lastMessage.getId(), "", "", 20)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendGroupMessage(Chat chat, String messageId, String text) {
        try {
            execute(new SendGroupMessageInteractor(
                    authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text));
            dataMediator.messageSent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeGroupMessage(Chat chat, Message message) {
        try {
            execute(new LikeMessageInteractor(authService.getAPIToken(), chat.getId(), message.getId()));
            dataMediator.messageLiked();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeGroupMessage(Chat chat, Message message) {
        try {
            execute(new UnlikeMessageInteractor(authService.getAPIToken(), chat.getId(), message.getId()));
            dataMediator.messageLiked();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDirectChats(int page, int pageSize) {
        try {
            dataMediator.chatsLoaded(execute(new GetDirectChatsInteractor(authService.getAPIToken(), page, pageSize)));
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDirectChat(Chat chat) {
        dataMediator.chatLoaded(chat);
    }

    @Override
    public void getDirectMessages(Chat chat, Message lastMessage) {
        try {
            dataMediator.messagesLoaded(execute(new GetDirectMessagesInteractor(
                    authService.getAPIToken(), chat.getId(),
                    (lastMessage == null) ? "" : lastMessage.getId(), "")));
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDirectMessage(Chat chat, String messageId, String text) {
        try {
            execute(new SendDirectMessageInteractor(
                    authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text));
            dataMediator.messageSent();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeDirectMessage(Chat chat, Message message) {
        if (userProfile == null) {
            getUserProfile();
        }
        String id = chat.getId() + "+" + userProfile.getId();

        try {
            execute(new LikeMessageInteractor(authService.getAPIToken(), id, message.getId()));
            dataMediator.messageLiked();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeDirectMessage(Chat chat, Message message) {
        if (userProfile == null) {
            getUserProfile();
        }
        String id = chat.getId() + "+" + userProfile.getId();

        try {
            execute(new UnlikeMessageInteractor(authService.getAPIToken(), id, message.getId()));
            dataMediator.messageUnliked();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserProfile() {
        if (userProfile == null) {
            try {
                userProfile = execute(new GetUserProfileInteractor(authService.getAPIToken()));
            } catch (RuntimeException | IOException e) {
                e.printStackTrace();
            }
        }
        dataMediator.profileLoaded(userProfile);
    }

    private <T> T execute(ApiInteractor<T> interactor) throws IOException, RuntimeException {
        int responseCode = interactor.getResponseCode();
        if (200 <= responseCode && responseCode < 300) {
            T content = interactor.getContent();
            interactor.disconnect();
            return content;
        } else if (responseCode == 401) {
            authService.tokenRejected();
        } else {
            dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
        }
        interactor.disconnect();
        throw new RuntimeException();
    }
}
