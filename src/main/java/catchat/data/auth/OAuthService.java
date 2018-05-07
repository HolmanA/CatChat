package catchat.data.auth;

public interface OAuthService extends Runnable {

    interface AuthListener {
        void onSuccess();
        void onFailure();
    }

    String getAPIToken();
    String getAuthURL();
}