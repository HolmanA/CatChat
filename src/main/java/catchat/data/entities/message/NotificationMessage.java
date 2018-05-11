package catchat.data.entities.message;


/**
 * Created by andrew on 5/4/18.
 */
public class NotificationMessage extends Message {
    public NotificationMessage(String text, String senderName) {
        super("", "", "", text, senderName, 0, null);
    }
}
