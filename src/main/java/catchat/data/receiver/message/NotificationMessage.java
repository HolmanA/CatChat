package catchat.data.receiver.message;


/**
 * Created by andrew on 5/4/18.
 */
public class NotificationMessage {
    private String chatId;
    private String senderName;
    private String messageText;

    NotificationMessage(String chatId, String senderName, String messageText) {
        this.chatId = chatId;
        this.senderName = senderName;
        this.messageText = messageText;
    }

    public String getChatId() {
        return chatId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageText() {
        return messageText;
    }
}
