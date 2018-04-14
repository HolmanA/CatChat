package catchat.data.source.threads;

import catchat.data.entities.message.Message;
import catchat.data.source.BaseDataSource;
import catchat.data.source.BaseListener;

import java.util.List;

/**
 * Created by andrew on 4/13/18.
 */
public abstract class ThreadDataSource extends BaseDataSource {
    interface ThreadListener extends BaseListener {
        void threadsLoaded(List<Thread> threads);
        void messagesLoaded(List<Message> messages);
        void messageSent();
        void messageLiked();
        void messageUnliked();
    }

    private List<ThreadListener> listeners;

    ThreadDataSource(List<ThreadListener> listeners) {
        this.listeners = listeners;
    }

    public void subscribe(ThreadListener listener) {
        listeners.add(listener);
    }

    public void unSubscribe(ThreadListener listener) {
        listeners.remove(listener);
    }

    public void unSubscribeAll() {
        listeners.clear();
    }

    abstract void getThreads(int page, int pageSize, String omit);
    abstract void getMessages(String threadId, String beforeMessageId, String sinceMessageId);
    abstract void sendMessage(String threadId, String sourceGUID, String messageText);
    abstract void likeMessage(String threadId, String messageId);
    abstract void unlikeMessage(String threadId, String messageId);
}
