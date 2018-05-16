package catchat.data.entities.chat;

import catchat.data.entities.ChatType;
import catchat.data.entities.profile.MemberProfile;

import java.util.Map;

/**
 * Created by andrew on 4/17/18.
 */
public class DirectChat extends Chat {
    public DirectChat(String id, String name, String preview, Map<String, MemberProfile> members) {
        super(ChatType.DIRECT, id, name, preview, members);
    }
}
