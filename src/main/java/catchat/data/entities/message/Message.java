package catchat.data.entities.message;

import catchat.data.entities.profile.Profile;

import java.util.List;

public abstract class Message {
    protected String id;
    protected String sourceGUID;
    protected String text;
    protected Profile sender;

    protected List<Profile> likedBy;
}