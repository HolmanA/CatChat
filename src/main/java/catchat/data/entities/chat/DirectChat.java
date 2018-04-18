package catchat.data.entities.chat;

import catchat.data.entities.profile.Profile;

import java.util.List;

/**
 * Created by andrew on 4/17/18.
 */
public class DirectChat extends Chat {
    public DirectChat(String id, String name, String preview, List<Profile> members) {
        super(id, name, preview, members);
    }
}
