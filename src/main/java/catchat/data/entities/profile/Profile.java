package catchat.data.entities.profile;

import catchat.data.entities.chat.Chat;

import java.util.HashMap;

public abstract class Profile {
    protected String id;
    protected String name;

    //Groups mapped to the user's memberID for that group
    // Can map to empty String for direct chats
    protected HashMap<Chat, String> groups;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}