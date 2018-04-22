package catchat;

import catchat.chats.ChatsPresenter;
import catchat.chats.ChatsView;
import catchat.data.auth.OAuthService;
import catchat.data.source.DataSource;
import catchat.data.source.HttpDataSource;
import catchat.data.source.connection.groupme.direct.DirectConnectionFactory;
import catchat.data.source.connection.groupme.group.GroupConnectionFactory;
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
    private DataSource groupChatDS;
    private DataSource directChatDS;

    public ApplicationStage(OAuthService service) {
        this.service = service;
        this.groupChatDS = new HttpDataSource(service, new GroupConnectionFactory());
        this.directChatDS = new HttpDataSource(service, new DirectConnectionFactory());
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

        MessagesView messagesView = new MessagesView();
        MessagesPresenter messagesPresenter = new MessagesPresenter(messagesView);
        messagesView.setPresenter(messagesPresenter);
        messagesPresenter.start();

        ChatsView groupsView = new ChatsView();
        ChatsPresenter groupsPresenter = new ChatsPresenter(groupChatDS, groupsView, messagesPresenter);
        groupsView.setPresenter(groupsPresenter);
        groupsPresenter.start();

        ChatsView chatsView = new ChatsView();
        ChatsPresenter chatsPresenter = new ChatsPresenter(directChatDS, chatsView, messagesPresenter);
        chatsView.setPresenter(chatsPresenter);
        chatsPresenter.start();

        borderPane.setLeft(groupsView);
        borderPane.setCenter(messagesView);
        borderPane.setRight(chatsView);

        return borderPane;
    }
}
