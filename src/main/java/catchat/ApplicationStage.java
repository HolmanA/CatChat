package catchat;

import catchat.chats.ChatsPresenter;
import catchat.chats.ChatsView;
import catchat.data.auth.OAuthService;
import catchat.data.source.chats.ChatDataSource;
import catchat.data.source.chats.GroupMeGroupChatDS;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by andrew on 4/14/18.
 */
public class ApplicationStage extends Stage {
    private OAuthService service;

    public ApplicationStage(OAuthService service) {
        this.service = service;
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
        borderPane.setLeft(groupList);
        setScene(new Scene(borderPane));
        setTitle("Cat Chat");
    }

    private Node initializeGroupChatsList() {
        ChatDataSource groupChatsDS = new GroupMeGroupChatDS();
        groupChatsDS.setAuthenticationToken(service.getAPIToken());

        ChatsView view = new ChatsView();
        ChatsPresenter presenter = new ChatsPresenter(groupChatsDS, view);
        view.setPresenter(presenter);
        presenter.start();
        return view;
    }
}
