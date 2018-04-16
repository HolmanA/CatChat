package catchat.data.entities.chat;

import catchat.data.entities.profile.Profile;

import java.util.List;

public abstract class Chat {
    protected String id;
    protected String name;
    protected String preview;
    protected List<Profile> members;

    protected Chat(String id, String name, String preview, List<Profile> members) {
        this.id = id;
        this.name = name;
        this.preview = preview;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPreview() {
        return preview;
    }

    public List<Profile> getMembers() {
        return members;
    }
}