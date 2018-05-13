package catchat.data.entities.chat;

import catchat.data.entities.ChatType;
import catchat.data.entities.profile.Profile;

import java.util.Map;

/**
 * Created by andrew on 4/14/18.
 */
public class GroupChat extends Chat {
    public GroupChat(String id, String name, String preview, Map<String,Profile> members) {
        super(ChatType.GROUP, id, name, preview, members);
    }
}
