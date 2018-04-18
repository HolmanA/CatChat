package catchat.data.entities.message;

public abstract class Message {
    private String id;
    private String sourceGUID;
    private String text;
    private String senderId;
    private long createdAt;

    protected Message(String id, String sourceGUID, String text, String senderId, long createdAt) {
        this.id = id;
        this.sourceGUID = sourceGUID;
        this.text = text;
        this.senderId = senderId;
        this.createdAt = createdAt;
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

    public long getCreatedAt() {
        return createdAt;
    }
}