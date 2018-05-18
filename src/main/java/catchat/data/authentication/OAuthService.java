package catchat.data.authentication;

public interface OAuthService extends Runnable {

    interface AuthListener {
        void onSuccess();

        void onFailure();
    }

    String getAPIToken();

    String getAuthURL();

    void tokenRejected();
}