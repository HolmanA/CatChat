package catchat.data.entities.profile;

import catchat.data.entities.chat.Chat;

import java.util.HashMap;

public abstract class Profile {
    protected String id;
    protected String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}