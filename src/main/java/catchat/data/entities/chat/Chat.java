package catchat.data.entities.chat;

import catchat.data.entities.ChatType;
import catchat.data.entities.profile.MemberProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class Chat {
    private ChatType type;
    protected String id;
    protected String name;
    protected String preview;
    protected Map<String, MemberProfile> members;

    protected Chat(ChatType type, String id, String name, String preview, Map<String, MemberProfile> members) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.preview = preview;
        this.members = members;
    }

    public ChatType getType() {
        return type;
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

    public List<MemberProfile> getMembers() {
        Collection<MemberProfile> membersCollection = members.values();
        return new ArrayList<>(membersCollection);
    }

    public MemberProfile findMemberById(String memberId) {
        if (members.containsKey(memberId)) {
            return members.get(memberId);
        }
        return null;
    }
}