package catchat.data.entities.chat;

import catchat.data.entities.profile.Profile;

import java.util.List;

/**
 * Created by andrew on 4/14/18.
 */
public class GroupChat extends Chat {
    public GroupChat(String id, String name, String preview, List<Profile> members) {
        super(id, name, preview, members);
    }
}
