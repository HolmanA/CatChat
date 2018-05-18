package catchat.ui;

import catchat.data.authentication.GroupMeOAuthService;
import catchat.data.authentication.OAuthService;
import catchat.model.Model;
import catchat.data.receiver.message.MessageReceiver;
import catchat.data.source.ApiInvoker;
import catchat.ui.authentication.AuthPresenter;
import catchat.ui.authentication.AuthView;
import catchat.ui.chats.ChatsPresenter;
import catchat.ui.chats.view.ChatsView;
import catchat.ui.error.ErrorBox;
import catchat.ui.messages.MessagesPresenter;
import catchat.ui.messages.view.MessagesView;
import catchat.ui.system.TrayManager;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by andrew on 4/14/18.
 */
public class ApplicationStage extends Stage implements OAuthService.AuthListener {

    private OAuthService authService;
    private ApiInvoker invoker;
    private TrayManager trayManager;
    private Model model;
    private MessageReceiver messageReceiver;

    public ApplicationStage() {
        authService = new GroupMeOAuthService(this);

        setTitle("Cat Chat");
        getIcons().add(new Image(getClass().getResourceAsStream("/system/SystemTrayIcon.png")));
    }

    public void start() {
        authenticate();
    }

    /**
     * onSuccess is called upon completion of the OAuthService's authentication routine. Since this method is called on
     * the authentication thread and not the application main UI thread, we must move execution back to the main UI
     * thread by calling Platform.runLater().
     */
    @Override
    public void onSuccess() {
        Platform.runLater(() -> initializeMainApplication());
    }

    @Override
    public void onFailure() {
        messageReceiver.stop();
        Scene scene = new Scene(new ErrorBox());
        setWidth(scene.getWidth());
        setHeight(scene.getHeight());
        setScene(scene);
    }

    private void authenticate() {
        AuthView authenticationView = new AuthView();
        AuthPresenter authenticationPresenter = new AuthPresenter(authService, authenticationView);
        authenticationView.setPresenter(authenticationPresenter);
        authenticationPresenter.start();

        setScene(new Scene(authenticationView));
        setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        show();
    }

    private void initializeMainApplication() {
        invoker = ApiInvoker.getInstance();
        invoker.setAuthService(authService);

        model = new Model(invoker);

        messageReceiver = new MessageReceiver(authService, model.getUserProfileModel());
        messageReceiver.subscribe(model);

        trayManager = new TrayManager(messageReceiver);

        // Show desktop notifications only when CatChat is not the active window
        focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                trayManager.unsubscribeFromMessageReceiver();
            } else {
                trayManager.subscribeToMessageReceiver();
            }
        }));


        messageReceiver.start();
        setOnCloseRequest(event -> {
            messageReceiver.stop();
            Platform.exit();
            System.exit(0);
        });

        BorderPane pane = initializeBorderPane();
        setScene(new Scene(pane));
        setMaximized(true);
        show();
    }

    private BorderPane initializeBorderPane() {
        MessagesView messagesView = new MessagesView();
        MessagesPresenter messagesPresenter = new MessagesPresenter(model, messagesView);
        messagesView.setPresenter(messagesPresenter);
        messagesPresenter.start();

        ChatsView chatsView = new ChatsView();
        ChatsPresenter chatsPresenter = new ChatsPresenter(model, chatsView);
        chatsView.setPresenter(chatsPresenter);
        chatsPresenter.start();

        return new BorderPane(messagesView, null, null, null, chatsView);
    }
}
