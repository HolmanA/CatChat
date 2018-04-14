package catchat.data.entities.thread;

import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;

import java.util.List;

public abstract class Thread {
    protected String id;
    protected String name;

    protected List<Profile> members;
    protected List<Message> messages;
}