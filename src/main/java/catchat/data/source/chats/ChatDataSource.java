package catchat.data.source.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.source.BaseDataSource;
import catchat.data.source.BaseCallback;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public abstract class ChatDataSource extends BaseDataSource {
    protected static final String BASE_API_URL = "https://api.groupme.com/v3/";

    public interface GetChatsCallback extends BaseCallback {
        void onChatsLoaded(List<Chat> chats);
    }

    public interface GetChatCallback extends BaseCallback {
        void onChatLoaded(Chat chat);
    }

    abstract public void getChats(int page, int pageSize, GetChatsCallback callback);
    abstract public void getChat(Chat chat, GetChatCallback callback);
}
