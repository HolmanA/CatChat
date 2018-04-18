package catchat.data.entities.message;

public abstract class Message {
    private String id;
    private String sourceGUID;
    private String text;
    private String senderId;

    protected Message(String id, String sourceGUID, String text, String senderId) {
        this.id = id;
        this.sourceGUID = sourceGUID;
        this.text = text;
        this.senderId = senderId;
    }

    public String getId() {
        return id;
    }

    public String getSourceGUID() {
        return sourceGUID;
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }
}