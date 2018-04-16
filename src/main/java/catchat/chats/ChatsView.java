package catchat.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.profile.Profile;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class ChatsView implements ChatsContract.View {
    ChatsContract.Presenter presenter;

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showGroups(List<Chat> groups) {
        for (Chat c : groups) {
            System.out.println("ID: " + c.getId());
            System.out.println("Name: " + c.getName());
            System.out.println("Preview: " + c.getPreview());
            System.out.println("Members: ");
            for (Profile p : c.getMembers()) {
                System.out.println("\t" + p.getName());
            }
        }
    }
}
