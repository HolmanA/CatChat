package catchat.data.source.entities.message;

import java.util.List;

/**
 * Created by andrew on 4/17/18.
 */
public class DirectMessage extends Message {
    public DirectMessage(String id, String senderAvatar, String sourceGUID, String text, String senderName, long createdAt, List<String> likes) {
        super(id, senderAvatar, sourceGUID, text, senderName, createdAt, likes);
    }
}
