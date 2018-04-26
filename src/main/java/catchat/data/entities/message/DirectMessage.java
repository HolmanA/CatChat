package catchat.data.entities.message;

import java.util.List;

/**
 * Created by andrew on 4/17/18.
 */
public class DirectMessage extends Message {
    public DirectMessage(String id, String sourceGUID, String text, String senderName, long createdAt, List<String> likes) {
        super(id, sourceGUID, text, senderName, createdAt, likes);
    }
}
