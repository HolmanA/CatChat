package catchat.data.source.threads;


import java.util.ArrayList;

/**
 * Created by andrew on 4/13/18.
 */
public class GroupMeGroupsDS extends ThreadDataSource {
    private static GroupMeGroupsDS INSTANCE;

    public static GroupMeGroupsDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupMeGroupsDS();
        }
        return INSTANCE;
    }

    private GroupMeGroupsDS() {
        super(new ArrayList<>());
    }

    @Override
    public void getThreads(int page, int pageSize, String omit) {

    }

    @Override
    public void getMessages(String threadId, String beforeMessageId, String sinceMessageId) {

    }

    @Override
    public void sendMessage(String threadId, String sourceGUID, String messageText) {

    }

    @Override
    public void likeMessage(String threadId, String messageId) {

    }

    @Override
    public void unlikeMessage(String threadId, String messageId) {

    }
}
