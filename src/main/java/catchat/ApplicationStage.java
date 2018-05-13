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
 * Created by andrew on 4/14/18.
 */
public class ApplicationStage extends Stage implements OAuthService.AuthListener {
    private OAuthService authService;
    private TrayManager trayManager;
    private DataSource dataSource;
    private DataMediator dataMediator;
    private MessageReceiver messageReceiver;

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
     * onSuccess is called upon completion of the OAuthService's authentication routine. Since this method is called on
     * the authentication thread and not the application main UI thread, we must move execution back to the main UI
     * thread by calling Platform.runLater().
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
        setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        show();
    }

    private void initializeMainApplication() {
        System.out.println("Main Application Started");

        dataSource = new GroupMeDataSource(authService, dataMediator);
        messageReceiver = new MessageReceiver(authService, dataSource, dataMediator);
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
