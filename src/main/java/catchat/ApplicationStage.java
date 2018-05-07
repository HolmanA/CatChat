package catchat;

import catchat.authentication.AuthPresenter;
import catchat.authentication.AuthView;
import catchat.chats.ChatsView;
import catchat.chats.DirectChatsPresenter;
import catchat.chats.GroupChatsPresenter;
import catchat.data.MessageEventBus;
import catchat.data.auth.GroupMeOAuthService;
import catchat.data.auth.OAuthService;
import catchat.data.receiver.message.MessageReceiver;
import catchat.data.source.DataSource;
import catchat.data.source.GroupMeDataSource;
import catchat.error.ErrorBox;
import catchat.messages.MessagesPresenter;
import catchat.messages.MessagesView;
import catchat.systemtray.TrayManager;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by andrew on 4/14/18.
 */
public class ApplicationStage extends Stage implements OAuthService.AuthListener {
    private OAuthService authService;
    private TrayManager trayManager;
    private DataSource dataSource;
    private MessageEventBus messageEventBus;
    private MessageReceiver messageReceiver;

    public ApplicationStage() {
        authService = new GroupMeOAuthService(this);
        messageEventBus = new MessageEventBus();
        trayManager = new TrayManager(messageEventBus);

        // Show desktop notifications only when CatChat is not the active window
        focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                trayManager.unsubscribeFromEventBus();
            } else {
                trayManager.subscribeToEventBus();
            }
        }));
        setTitle("Cat Chat");
    }

    public void start() {
        authenticate();
    }

    /**
     * onSuccess is called upon completion of the OAuthService's authentication routine. Since this method is called on
     * the authentication thread and not the application main UI thread, we must move execution back to the main UI
     * thread.
     */
    @Override
    public void onSuccess() {
        System.out.println("\tAuthenticated");
        Platform.runLater(() -> initializeMainApplication());
    }

    @Override
    public void onFailure(String message) {
        messageReceiver.stop();
        Scene scene = new Scene(new ErrorBox(message));
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
        show();
    }

    private void initializeMainApplication() {
        System.out.println("Main Application Started");

        dataSource = new GroupMeDataSource(authService);
        messageReceiver = new MessageReceiver(authService, dataSource, messageEventBus);
        BorderPane pane = initializeBorderPane();

        Thread thread = new Thread(() -> messageReceiver.start());
        thread.start();

        setOnCloseRequest(event -> {
            messageReceiver.stop();
            Platform.exit();
            System.exit(0);
        });
        setScene(new Scene(pane));
        setMaximized(true);
        show();
    }

    private BorderPane initializeBorderPane() {
        MessagesView messagesView = new MessagesView();
        MessagesPresenter messagesPresenter = new MessagesPresenter(dataSource, messageEventBus, messagesView);
        messagesView.setPresenter(messagesPresenter);
        messagesPresenter.start();

        ChatsView groupChatsView = new ChatsView();
        GroupChatsPresenter groupChatsPresenter = new GroupChatsPresenter(dataSource, groupChatsView, messagesPresenter);
        messageEventBus.subscribe(groupChatsPresenter);
        groupChatsView.setPresenter(groupChatsPresenter);
        groupChatsPresenter.start();

        ChatsView directChatsView = new ChatsView();
        DirectChatsPresenter directChatsPresenter = new DirectChatsPresenter(dataSource, directChatsView, messagesPresenter);
        messageEventBus.subscribe(directChatsPresenter);
        directChatsView.setPresenter(directChatsPresenter);
        directChatsPresenter.start();

        return new BorderPane(messagesView, null, directChatsView, null, groupChatsView);
    }
}
