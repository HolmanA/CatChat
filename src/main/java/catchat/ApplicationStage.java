package catchat;

import catchat.chats.ChatsPresenter;
import catchat.chats.ChatsView;
import catchat.data.auth.OAuthService;
import catchat.data.source.chats.ChatDataSource;
import catchat.data.source.chats.GroupMeGroupChatDS;
import catchat.messages.MessagesPresenter;
import catchat.messages.MessagesView;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by andrew on 4/14/18.
 */
public class ApplicationStage extends Stage {
    private OAuthService service;
    private ChatDataSource groupChatDS;

    public ApplicationStage(OAuthService service) {
        this.service = service;
        this.groupChatDS = GroupMeGroupChatDS.getInstance();
        groupChatDS.setAuthenticationToken(service.getAPIToken());
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
        MessagesPresenter messagesPresenter = new MessagesPresenter(groupChatDS, messagesView);
        messagesView.setPresenter(messagesPresenter);
        messagesPresenter.start();

        ChatsView groupsView = new ChatsView();
        ChatsPresenter groupsPresenter = new ChatsPresenter(groupChatDS, groupsView, messagesPresenter);
        groupsView.setPresenter(groupsPresenter);
        groupsPresenter.start();

        borderPane.setLeft(groupsView);
        borderPane.setCenter(messagesView);

        return borderPane;
    }
}
