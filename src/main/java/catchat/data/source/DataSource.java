package catchat.data.source;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;

/**
 * Created by andrew on 4/13/18.
 */
public interface DataSource {
    void getGroupChats(int page, int pageSize);
    void getGroupChat(Chat chat);
    void getGroupMessages(Chat chat, Message lastMessage);
    void sendGroupMessage(Chat chat, String messageId, String text);
    void likeGroupMessage(Chat chat, Message message);
    void unlikeGroupMessage(Chat chat, Message message);

    void getDirectChats(int page, int pageSize);
    void getDirectChat(Chat chat);
    void getDirectMessages(Chat chat, Message lastMessage);
    void sendDirectMessage(Chat chat, String messageId, String text);
    void likeDirectMessage(Chat chat, Message message);
    void unlikeDirectMessage(Chat chat, Message message);

    void getUserProfile();
}
