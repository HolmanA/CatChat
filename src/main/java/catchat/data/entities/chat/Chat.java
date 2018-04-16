package catchat.data.entities.chat;

public abstract class Chat {
    protected String id;
    protected String name;
    protected String preview;

    protected Chat(String id, String name, String preview) {
        this.id = id;
        this.name = name;
        this.preview = preview;
    }

    //protected List<Profile> members;
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPreview() {
        return preview;
    }
}