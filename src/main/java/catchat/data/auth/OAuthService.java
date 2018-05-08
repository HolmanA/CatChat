package catchat.data.auth;

public interface OAuthService extends Runnable {

    interface AuthListener {
        void onSuccess();
        void onFailure(String message);
    }

    String getAPIToken();
    String getAuthURL();
    void tokenRejected();
}