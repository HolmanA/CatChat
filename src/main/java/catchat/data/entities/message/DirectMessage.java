package catchat.data.entities.message;

/**
 * Created by andrew on 4/17/18.
 */
public class DirectMessage extends Message {
    public DirectMessage(String id, String sourceGUID, String text, String senderId) {
        super(id, sourceGUID, text, senderId);
    }
}
