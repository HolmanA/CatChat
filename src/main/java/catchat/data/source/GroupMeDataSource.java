package catchat.data.source;

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
import java.util.List;

/**
 * Created by andrew on 4/26/18.
 */
public class GroupMeDataSource implements DataSource, DataSource.GetUserProfileCallback {
    private static final String BASE_SOURCE_GUID = "com.catchat.guid-";
    private OAuthService authService;
    private Profile userProfile;

    public GroupMeDataSource(OAuthService authService) {
        this.authService = authService;
        getUserProfile(this);
    }

    @Override
    public void getGroupChats(int page, int pageSize, GetChatsCallback callback) {
        try {
            ApiInteractor<List<Chat>> interactor = new GetGroupChatsInteractor(
                    authService.getAPIToken(), page, pageSize);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Chat> chats = interactor.getContent();
                callback.onChatsLoaded(chats);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGroupChat(Chat chat, GetGroupChatCallback callback) {
        callback.onGroupChatLoaded(chat);
    }

    @Override
    public void getGroupMessages(Chat chat, Message lastMessage, GetMessagesCallback callback) {
        try {
            ApiInteractor<List<Message>> interactor = new GetGroupMessagesInteractor(
                    authService.getAPIToken(), chat.getId(),
                    (lastMessage == null) ? "" : lastMessage.getId(), "", "", 20);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Message> messages = interactor.getContent();
                callback.onMessagesLoaded(messages);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendGroupMessage(Chat chat, String messageId, String text, SendMessageCallback callback) {
        try {
            ApiInteractor interactor = new SendGroupMessageInteractor(
                    authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                callback.onMessageSent();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeGroupMessage(Chat chat, Message message, LikeMessageCallback callback) {
        try {
            ApiInteractor interactor = new LikeMessageInteractor(
                    authService.getAPIToken(), chat.getId(), message.getId());

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                callback.onMessageLiked();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeGroupMessage(Chat chat, Message message, UnlikeMessageCallback callback) {
        try {
            ApiInteractor interactor = new UnlikeMessageInteractor(
                    authService.getAPIToken(), chat.getId(), message.getId());

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                callback.onMessageUnliked();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDirectChats(int page, int pageSize, GetChatsCallback callback) {
        try {
            ApiInteractor<List<Chat>> interactor = new GetDirectChatsInteractor(
                    authService.getAPIToken(), page, pageSize);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Chat> chats = interactor.getContent();
                callback.onChatsLoaded(chats);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDirectChat(Chat chat, GetDirectChatCallback callback) {
        callback.onDirectChatLoaded(chat);
    }

    @Override
    public void getDirectMessages(Chat chat, Message lastMessage, GetMessagesCallback callback) {
        try {
            ApiInteractor<List<Message>> interactor = new GetDirectMessagesInteractor(
                    authService.getAPIToken(), chat.getId(),
                    (lastMessage == null) ? "" : lastMessage.getId(), "");

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Message> messages = interactor.getContent();
                callback.onMessagesLoaded(messages);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDirectMessage(Chat chat, String messageId, String text, SendMessageCallback callback) {
        try {
            ApiInteractor interactor = new SendDirectMessageInteractor(
                    authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                callback.onMessageSent();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeDirectMessage(Chat chat, Message message, LikeMessageCallback callback) {
        if (userProfile != null) {
            String id = chat.getId() + "+" + userProfile.getId();

            try {
                ApiInteractor interactor = new LikeMessageInteractor(
                        authService.getAPIToken(), id, message.getId());

                int responseCode = interactor.getResponseCode();
                if (200 <= responseCode && responseCode < 300) {
                    callback.onMessageLiked();
                } else if (responseCode == 401) {
                    authService.tokenRejected();
                } else {
                    callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
                }
                interactor.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getUserProfile(this);
        }
    }

    @Override
    public void unlikeDirectMessage(Chat chat, Message message, UnlikeMessageCallback callback) {
        if (userProfile != null) {
            String id = chat.getId() + "+" + userProfile.getId();

            try {
                ApiInteractor interactor = new UnlikeMessageInteractor(
                        authService.getAPIToken(), id, message.getId());

                int responseCode = interactor.getResponseCode();
                if (200 <= responseCode && responseCode < 300) {
                    callback.onMessageUnliked();
                } else if (responseCode == 401) {
                    authService.tokenRejected();
                } else {
                    callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
                }
                interactor.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getUserProfile(this);
        }
    }

    @Override
    public void getUserProfile(GetUserProfileCallback callback) {
        if (userProfile == null) {
            try {
                ApiInteractor<Profile> interactor = new GetUserProfileInteractor(
                        authService.getAPIToken());

                int responseCode = interactor.getResponseCode();
                if (200 <= responseCode && responseCode < 300) {
                    Profile profile = interactor.getContent();
                    callback.onUserProfileLoaded(profile);
                } else if (responseCode == 401) {
                    authService.tokenRejected();
                } else {
                    callback.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
                }
                interactor.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            callback.onUserProfileLoaded(userProfile);
        }
    }

    @Override
    public void onUserProfileLoaded(Profile profile) {
        this.userProfile = profile;
    }

    @Override
    public void unknownResponseCode(String response) {
        System.err.println(response);
    }
}
