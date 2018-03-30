package catchat.data.auth;

public interface OAuthService extends Runnable {

    interface AuthListener {
        void onSuccess();
        void onFailure();
    }

    String getAPIToken();
    void setAuthListener(AuthListener listener);
    void removeAuthListener();
    String getAuthURL();
}