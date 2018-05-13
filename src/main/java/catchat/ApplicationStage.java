package catchat;

import catchat.authentication.AuthPresenter;
import catchat.authentication.AuthView;
import catchat.chats.ChatsPresenter;
import catchat.chats.view.ChatsView;
import catchat.data.DataMediator;
import catchat.data.authentication.GroupMeOAuthService;
import catchat.data.authentication.OAuthService;
import catchat.data.entities.ChatType;
import catchat.data.receiver.message.MessageReceiver;
import catchat.data.source.DataSource;
import catchat.data.source.GroupMeDataSource;
import catchat.error.ErrorBox;
import catchat.messages.MessagesPresenter;
import catchat.messages.view.MessagesView;
import catchat.system.TrayManager;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * ApplicationStage is the Main Stage for the Cat Chat JavaFX Application. Handles receiving authentication signals,
 * dependency injection of various components, and the general application lifecycle. Begins with executing the
 * authentication component before executing the main application component.
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public class ApplicationStage extends Stage implements OAuthService.AuthListener {
    private OAuthService authService;
    private TrayManager trayManager;
    private DataSource dataSource;
    private DataMediator dataMediator;
    private MessageReceiver messageReceiver;

    /**
     * Constructor
     */
    public ApplicationStage() {
        authService = new GroupMeOAuthService(this);
        dataMediator = new DataMediator();
        trayManager = new TrayManager(dataMediator);

        // Show desktop notifications only when CatChat is not the active window
        focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                trayManager.unsubscribeFromDataMediator();
            } else {
                trayManager.subscribeToDataMediator();
            }
        }));
        setTitle("Cat Chat");
        getIcons().add(new Image(getClass().getResourceAsStream("/system/SystemTrayIcon.png")));
    }

    public void start() {
        authenticate();
    }

    /**
     * Starts the main application scene upon completion of the OAuthService's authentication routine. Since this
     * method is called on authentication thread and not the application main UI thread, we must move execution back to
     * the main UI thread by calling Platform.runLater().
     */
    @Override
    public void onSuccess() {
        System.out.println("\tAuthenticated");
        Platform.runLater(() -> initializeMainApplication());
    }

    /**
     * Displays an error message if something goes wrong in the authentication process. This is either the result of an
     * error in OAuthService's authentication routine, or a rejected authentication token.
     * @param message Message describing the nature of the failure
     */
    @Override
    public void onFailure(String message) {
        messageReceiver.stop();
        Scene scene = new Scene(new ErrorBox(message));
        setWidth(scene.getWidth());
        setHeight(scene.getHeight());
        setScene(scene);
    }

    /**
     * Initializes authentication component, injects dependencies, and begins authentication
     */
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

    /**
     * Initializes main application components and sets the main application scene, effectively launching the main
     * application
     */
    private void initializeMainApplication() {
        System.out.println("Main Application Started");

        dataSource = new GroupMeDataSource(authService, dataMediator);
        messageReceiver = new MessageReceiver(authService, dataSource, dataMediator);
        messageReceiver.start();

        //Stop all threads when application window is closed
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

    /**
     * Initializes Message and Chat components, injects dependencies, and subscribes components to the DataMediator
     * @return BorderPane containing each of the main Cat Chat components
     */
    private BorderPane initializeBorderPane() {
        MessagesView messagesView = new MessagesView();
        MessagesPresenter messagesPresenter = new MessagesPresenter(dataSource, messagesView);
        dataMediator.subscribe(messagesPresenter);
        messagesView.setPresenter(messagesPresenter);
        messagesPresenter.start();

        ChatsView groupChatsView = new ChatsView();
        ChatsPresenter groupChatsPresenter = new ChatsPresenter(ChatType.GROUP, dataSource, groupChatsView);
        dataMediator.subscribe(groupChatsPresenter);
        groupChatsView.setPresenter(groupChatsPresenter);
        groupChatsPresenter.start();

        ChatsView directChatsView = new ChatsView();
        ChatsPresenter directChatsPresenter = new ChatsPresenter(ChatType.DIRECT, dataSource, directChatsView);
        dataMediator.subscribe(directChatsPresenter);
        directChatsView.setPresenter(directChatsPresenter);
        directChatsPresenter.start();

        return new BorderPane(messagesView, null, directChatsView, null, groupChatsView);
    }
}
