package catchat.data.entities.profile;

/**
 * Created by andrew on 4/15/18.
 */
public class GroupMemberProfile extends Profile {
    private String memberId;

    public GroupMemberProfile(String id, String name, String memberId) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
    }
}
