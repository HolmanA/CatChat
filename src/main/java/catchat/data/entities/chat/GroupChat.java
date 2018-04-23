package catchat.data.entities.chat;

import catchat.data.entities.profile.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 4/14/18.
 */
public class GroupChat extends Chat {
    public GroupChat(String id, String name, String preview, Map<String,Profile> members) {
        super(id, name, preview, members);
    }
}
