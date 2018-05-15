package catchat.data.entities.profile;

/**
 * Created by andrew on 4/26/18.
 */
public class UserProfile {
    private String id;
    private String name;

    public UserProfile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
