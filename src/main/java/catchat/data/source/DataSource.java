package catchat.data.source;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public interface DataSource {
    interface GetChatsCallback extends Callback {
        void onChatsLoaded(List<Chat> chats);
    }

    interface GetGroupChatCallback extends Callback {
        void onGroupChatLoaded(Chat chat);
    }

    interface GetDirectChatCallback extends Callback {
        void onDirectChatLoaded(Chat chat);
    }

    interface GetMessagesCallback extends Callback {
        void onMessagesLoaded(List<Message> messages);
    }

    interface SendMessageCallback extends Callback {
        void onMessageSent();
    }

    interface LikeMessageCallback extends Callback {
        void onMessageLiked();
    }

    interface UnlikeMessageCallback extends Callback {
        void onMessageUnliked();
    }

    interface GetUserProfileCallback extends Callback {
        void onUserProfileLoaded(Profile profile);
    }

    void getGroupChats(GetChatsCallback callback);
    void getGroupChat(Chat chat, GetGroupChatCallback callback);
    void getGroupMessages(Chat chat, Message lastMessage, GetMessagesCallback callback);
    void sendGroupMessage(Chat chat, String messageId, String text, SendMessageCallback callback);
    void likeGroupMessage(Chat chat, Message message, LikeMessageCallback callback);
    void unlikeGroupMessage(Chat chat, Message message, UnlikeMessageCallback callback);

    void getDirectChats(GetChatsCallback callback);
    void getDirectChat(Chat chat, GetDirectChatCallback callback);
    void getDirectMessages(Chat chat, Message lastMessage, GetMessagesCallback callback);
    void sendDirectMessage(Chat chat, String messageId, String text, SendMessageCallback callback);
    void likeDirectMessage(Chat chat, Message message, LikeMessageCallback callback);
    void unlikeDirectMessage(Chat chat, Message message, UnlikeMessageCallback callback);

    void getUserProfile(GetUserProfileCallback callback);
}
