package catchat;

import catchat.chats.ChatsContract;
import catchat.chats.ChatsPresenter;
import catchat.chats.ChatsView;
import catchat.data.auth.OAuthService;
import catchat.data.source.chats.ChatDataSource;
import catchat.data.source.chats.GroupMeGroupChatDS;

/**
 * Created by andrew on 4/14/18.
 */
public class ApplicationManager {
    private OAuthService service;

    public ApplicationManager(OAuthService service) {
        this.service = service;
    }

    public void start() {
        System.out.println("Main Application Started");
        loadGroupChatList();
    }

    private void loadGroupChatList() {
        ChatDataSource groupChatsDS = new GroupMeGroupChatDS();
        groupChatsDS.setAuthenticationToken(service.getAPIToken());
        ChatsContract.View view = new ChatsView();
        ChatsContract.Presenter presenter = new ChatsPresenter(groupChatsDS, view);
        view.setPresenter(presenter);
        presenter.start();
    }
}
