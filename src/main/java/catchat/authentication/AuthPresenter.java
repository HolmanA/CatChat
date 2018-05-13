package catchat.authentication;

import catchat.data.authentication.OAuthService;

public class AuthPresenter implements AuthContract.Presenter {
    private final OAuthService authService;
    private final AuthContract.View authView;

    public AuthPresenter(OAuthService authService, AuthContract.View authView) {
        this.authService = authService;
        this.authView = authView;
    }

    @Override
    public void start() {}

    @Override
    public void authenticate() {
        authView.showAuthenticating();
        // Begin authorization execution in a new thread
        Thread thread = new Thread(authService);
        thread.setDaemon(true);
        thread.start();

        authView.openWebPage(authService.getAuthURL());
    }
}
