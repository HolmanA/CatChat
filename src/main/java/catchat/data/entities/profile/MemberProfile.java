package catchat.data.entities.profile;

/**
 * Created by andrew on 4/15/18.
 */
public class MemberProfile extends Profile {
    private String memberId;

    public MemberProfile(String id, String name, String memberId) {
        super(id, name);
        this.memberId = memberId;
    }
}
