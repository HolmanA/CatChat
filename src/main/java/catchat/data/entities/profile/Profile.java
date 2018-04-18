package catchat.data.entities.profile;

public abstract class Profile {
    private String id;
    private String name;

    protected Profile(String id, String name) {
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