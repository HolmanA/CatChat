package catchat.data.entities.message;

import java.util.List;

public abstract class Message {
    private String id;
    private String sourceGUID;
    private String text;
    private String senderName;
    private long createdAt;
    private List<String> likes;

    protected Message(String id, String sourceGUID, String text, String senderName, long createdAt, List<String> likes) {
        this.id = id;
        this.sourceGUID = sourceGUID;
        this.text = text;
        this.senderName = senderName;
        this.createdAt = createdAt;
        this.likes = likes;
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

    public String getSenderName() {
        return senderName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public List<String> getLikes() {
        return likes;
    }
}