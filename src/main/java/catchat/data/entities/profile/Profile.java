package catchat.data.entities.profile;

import catchat.data.entities.thread.Thread;

import java.util.HashMap;

public abstract class Profile {
    protected String id;
    protected String name;

    //Groups mapped to the user's memberID for that group
    // Can map to empty String for direct chats
    protected HashMap<Thread, String> groups;
}