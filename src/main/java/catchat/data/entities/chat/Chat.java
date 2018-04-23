package catchat.data.entities.chat;

import catchat.data.entities.profile.Profile;

import java.util.*;

public abstract class Chat {
    protected String id;
    protected String name;
    protected String preview;
    protected Map<String,Profile> members;

    protected Chat(String id, String name, String preview, Map<String, Profile> members) {
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
        Collection<Profile> membersCollection = members.values();
        return new ArrayList<>(membersCollection);
    }

    public Profile findMemberById(String memberId) {
        if (members.containsKey(memberId)) {
            return members.get(memberId);
        }
        return null;
    }
}