package catchat;

import catchat.chats.ChatsView;
import catchat.chats.DirectChatsPresenter;
import catchat.chats.GroupChatsPresenter;
import catchat.data.auth.OAuthService;
import catchat.data.receiver.message.MessageChangeEventBus;
import catchat.data.source.DataSource;
import catchat.data.source.GroupMeDataSource;
import catchat.messages.MessagesPresenter;
import catchat.messages.MessagesView;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by andrew on 4/14/18.
 */
public class ApplicationStage extends Stage {
    private OAuthService service;
    private DataSource dataSource;

    public ApplicationStage(OAuthService service) {
        this.service = service;
        this.dataSource = new GroupMeDataSource(service);
    }

    public void start() {
        System.out.println("Main Application Started");
        setMaximized(true);
        initialize();
        show();
    }

    private void initialize() {
        BorderPane pane = initializePane();
        setScene(new Scene(pane));
        setTitle("Cat Chat");
    }

    private BorderPane initializePane() {
        BorderPane borderPane = new BorderPane();

        MessageChangeEventBus messageChangeEventBus = new MessageChangeEventBus();

        MessagesView messagesView = new MessagesView();
        MessagesPresenter messagesPresenter = new MessagesPresenter(dataSource, messageChangeEventBus, messagesView);
        messagesView.setPresenter(messagesPresenter);
        messagesPresenter.start();

        ChatsView groupChatsView = new ChatsView();
        GroupChatsPresenter groupChatsPresenter = new GroupChatsPresenter(dataSource, groupChatsView, messagesPresenter);
        messageChangeEventBus.subscribe(groupChatsPresenter);
        groupChatsView.setPresenter(groupChatsPresenter);
        groupChatsPresenter.start();

        ChatsView directChatsView = new ChatsView();
        DirectChatsPresenter directChatsPresenter = new DirectChatsPresenter(dataSource, directChatsView, messagesPresenter);
        messageChangeEventBus.subscribe(directChatsPresenter);
        directChatsView.setPresenter(directChatsPresenter);
        directChatsPresenter.start();

        borderPane.setLeft(groupChatsView);
        borderPane.setCenter(messagesView);
        borderPane.setRight(directChatsView);

        return borderPane;
    }
}
