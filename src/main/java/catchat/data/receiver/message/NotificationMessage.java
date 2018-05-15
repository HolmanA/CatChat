package catchat.data.receiver.message;


/**
 * Created by andrew on 5/4/18.
 */
public class NotificationMessage {
    private String id;
    private String senderName;
    private String messageText;

    NotificationMessage(String id, String senderName, String messageText) {
        this.id = id;
        this.senderName = senderName;
        this.messageText = messageText;
    }

    public String getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageText() {
        return messageText;
    }
}
