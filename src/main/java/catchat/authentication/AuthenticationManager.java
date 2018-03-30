package catchat.authentication;

import catchat.data.auth.GroupMeOAuthService;
import catchat.data.auth.OAuthService;
import javafx.application.Platform;

/**
 * AuthenticationManager handles instantiation and dependency injection for implementing classes of the AuthContract
 *  Presenter and View interfaces.
 * AuthenticationManager implements the OAuthService AuthListener interface, allowing it to receive messages from the
 *  OAuthService object indicating when authentication has succeeded or failed.
 * AuthenticationManager additionally provides some application control flow commands. It is responsible for beginning
 *  authentication, as well as subsequently launching the main chat module.
 */
public class AuthenticationManager implements OAuthService.AuthListener {
    private OAuthService service;
    private AuthStage authenticationView;
    private AuthContract.Presenter authenticationPresenter;

    public AuthenticationManager() {
        this.service = GroupMeOAuthService.getInstance();
        service.setAuthListener(this);
    }

    /**
     * Instantiates AuthContract objects and injects dependencies. Begins the authentication process.
     */
    public void start() {
        authenticationView = new AuthStage();
        authenticationPresenter = new AuthPresenter(service, authenticationView);
        authenticationView.setPresenter(authenticationPresenter);
        authenticationPresenter.start();
    }

    /**
     * onSuccess is called upon completion of the OAuthService's authentication routine. Since this method is called on
     * the authentication thread and not the application main UI thread, we must move execution back to the main UI
     * thread.
     */
    @Override
    public void onSuccess() {
        Platform.runLater(() -> {
                System.out.println("Authenticated");
                authenticationPresenter.stop();
                launchMainApplication();
        });
    }

    @Override
    public void onFailure() {
        // TODO: Create a try again / restart application scene for failure
    }

    private void launchMainApplication() {
        // TODO: Create a new Manager for the application
        System.out.println("Launching main application...");
    }
}
