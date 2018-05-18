package catchat.data.source.entities.chat;

import catchat.data.source.entities.ChatType;
import catchat.data.source.entities.profile.MemberProfile;

import java.util.Map;

/**
 * Created by andrew on 4/17/18.
 */
public class DirectChat extends Chat {
    public DirectChat(String id, String name, String preview, Map<String, MemberProfile> members) {
        super(ChatType.DIRECT, id, name, preview, members);
    }
}
