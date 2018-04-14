package catchat.data.source;

/**
 * Created by andrew on 3/24/18.
 */
public abstract class BaseDataSource {
    private String authToken;

    public void setAuthenticationToken(String authToken) {
        this.authToken = authToken;
    }

    protected String getAuthToken() {
        return authToken;
    }
}
