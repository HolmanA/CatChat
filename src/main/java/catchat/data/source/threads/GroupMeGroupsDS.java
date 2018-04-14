package catchat.data.source.threads;



/**
 * Created by andrew on 4/13/18.
 */
public class GroupMeGroupsDS extends ThreadDataSource {

    public GroupMeGroupsDS() {}

    @Override
    public void getThreads(int page, int pageSize, String omit, GetThreadsCallback callback) {

    }

    @Override
    public void getMessages(String threadId, String beforeMessageId, String sinceMessageId, GetMessagesCallback callback) {

    }

    @Override
    public void sendMessage(String threadId, String sourceGUID, String messageText, SendMessageCallback callback) {

    }

    @Override
    public void likeMessage(String threadId, String messageId, LikeMessageCallback callback) {

    }

    @Override
    public void unlikeMessage(String threadId, String messageId, UnlikeMessageCallback callback) {

    }
}
