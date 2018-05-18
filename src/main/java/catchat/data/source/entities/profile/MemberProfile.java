package catchat.data.source.entities.profile;

/**
 * Created by andrew on 4/15/18.
 */
public class MemberProfile {
    private String id;
    private String memberId;
    private String name;

    public MemberProfile(String id, String name, String memberId) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
