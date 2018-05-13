package catchat.data.entities.chat;

import catchat.data.entities.ChatType;
import catchat.data.entities.profile.Profile;

import java.util.Map;

/**
 * Created by andrew on 4/17/18.
 */
public class DirectChat extends Chat {
    public DirectChat(String id, String name, String preview, Map<String,Profile> members) {
        super(ChatType.DIRECT, id, name, preview, members);
    }
}
