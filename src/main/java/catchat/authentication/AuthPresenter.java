package catchat.authentication;

import catchat.data.auth.OAuthService;

public class AuthPresenter implements AuthContract.Presenter {
    private final OAuthService authService;
    private final AuthContract.View authView;

    public AuthPresenter(OAuthService authService, AuthContract.View authView) {
        if (authService == null) {
            throw new NullPointerException("AuthPresenter OAuthService reference is null");
        } else if (authView == null) {
            throw new NullPointerException("AuthPresenter View reference is null");
        } else {
            this.authService = authService;
            this.authView = authView;
        }
    }

    @Override
    public void start() {
        authView.openWebPage(authService.getAuthURL());

        // Begin authorization execution in a new thread
        Thread thread = new Thread(authService);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() {
        authView.closeWebPage();
    }
}
