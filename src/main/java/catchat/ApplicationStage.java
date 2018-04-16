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
        BorderPane borderPane = new BorderPane();

        Node groupList = initializeGroupChatsList();
        Node messageList = initializeMessageList();
        borderPane.setLeft(groupList);
        borderPane.setCenter(messageList);

        setScene(new Scene(borderPane));
        setTitle("Cat Chat");
    }

    private Node initializeGroupChatsList() {
        ChatsView view = new ChatsView();
        ChatsPresenter presenter = new ChatsPresenter(groupChatDS, view);
        view.setPresenter(presenter);
        presenter.start();
        return view;
    }

    private Node initializeMessageList() {
        MessagesView view = new MessagesView();
        MessagesPresenter presenter = new MessagesPresenter(groupChatDS, view);
        view.setPresenter(presenter);
        presenter.start();
        return view;
    }
}
