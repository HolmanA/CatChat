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
import java.util.List;

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
            ApiInteractor<List<Chat>> interactor = new GetGroupChatsInteractor(
                    authService.getAPIToken(), page, pageSize);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Chat> chats = interactor.getContent();
                dataMediator.chatsLoaded(chats);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
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
            ApiInteractor<List<Message>> interactor = new GetGroupMessagesInteractor(
                    authService.getAPIToken(), chat.getId(),
                    (lastMessage == null) ? "" : lastMessage.getId(), "", "", 20);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Message> messages = interactor.getContent();
                dataMediator.messagesLoaded(messages);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendGroupMessage(Chat chat, String messageId, String text) {
        try {
            ApiInteractor interactor = new SendGroupMessageInteractor(
                    authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                dataMediator.messageSent();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeGroupMessage(Chat chat, Message message) {
        try {
            ApiInteractor interactor = new LikeMessageInteractor(
                    authService.getAPIToken(), chat.getId(), message.getId());

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                dataMediator.messageLiked();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlikeGroupMessage(Chat chat, Message message) {
        try {
            ApiInteractor interactor = new UnlikeMessageInteractor(
                    authService.getAPIToken(), chat.getId(), message.getId());

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                dataMediator.messageLiked();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDirectChats(int page, int pageSize) {
        try {
            ApiInteractor<List<Chat>> interactor = new GetDirectChatsInteractor(
                    authService.getAPIToken(), page, pageSize);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Chat> chats = interactor.getContent();
                dataMediator.chatsLoaded(chats);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
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
            ApiInteractor<List<Message>> interactor = new GetDirectMessagesInteractor(
                    authService.getAPIToken(), chat.getId(),
                    (lastMessage == null) ? "" : lastMessage.getId(), "");

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                List<Message> messages = interactor.getContent();
                dataMediator.messagesLoaded(messages);
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDirectMessage(Chat chat, String messageId, String text) {
        try {
            ApiInteractor interactor = new SendDirectMessageInteractor(
                    authService.getAPIToken(), chat.getId(), BASE_SOURCE_GUID + messageId, text);

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                dataMediator.messageSent();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
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
            ApiInteractor interactor = new LikeMessageInteractor(
                    authService.getAPIToken(), id, message.getId());

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                dataMediator.messageLiked();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
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
            ApiInteractor interactor = new UnlikeMessageInteractor(
                    authService.getAPIToken(), id, message.getId());

            int responseCode = interactor.getResponseCode();
            if (200 <= responseCode && responseCode < 300) {
                dataMediator.messageUnliked();
            } else if (responseCode == 401) {
                authService.tokenRejected();
            } else {
                dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
            }
            interactor.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserProfile() {
        if (userProfile == null) {
            try {
                ApiInteractor<Profile> interactor = new GetUserProfileInteractor(
                        authService.getAPIToken());

                int responseCode = interactor.getResponseCode();
                if (200 <= responseCode && responseCode < 300) {
                    userProfile = interactor.getContent();
                } else if (responseCode == 401) {
                    authService.tokenRejected();
                } else {
                    dataMediator.unknownResponseCode(responseCode + ": " + interactor.getResponseMessage());
                }
                interactor.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataMediator.profileLoaded(userProfile);
    }
}
