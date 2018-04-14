package catchat.data.source.threads;

import catchat.data.entities.message.Message;
import catchat.data.source.BaseDataSource;
import catchat.data.source.BaseCallback;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public abstract class ThreadDataSource extends BaseDataSource {
    interface GetThreadsCallback extends BaseCallback {
        void onThreadsLoaded(List<Thread> threads);
    }

    interface GetMessagesCallback extends BaseCallback {
        void onMessagesLoaded(List<Message> messages);
    }

    interface SendMessageCallback extends BaseCallback {
        void onMessageSent();
    }

    interface LikeMessageCallback extends BaseCallback {
        void onMessageLiked();
    }

    interface UnlikeMessageCallback extends BaseCallback {
        void onMessageUnliked();
    }

    abstract public void getThreads(int page, int pageSize, String omit, GetThreadsCallback callback);
    abstract public void getMessages(String threadId, String beforeMessageId, String sinceMessageId, GetMessagesCallback callback);
    abstract public void sendMessage(String threadId, String sourceGUID, String messageText, SendMessageCallback callback);
    abstract public void likeMessage(String threadId, String messageId, LikeMessageCallback callback);
    abstract public void unlikeMessage(String threadId, String messageId, UnlikeMessageCallback callback);
}
