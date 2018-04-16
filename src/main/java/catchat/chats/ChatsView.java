package catchat.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.profile.Profile;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class ChatsView extends Text implements ChatsContract.View {
    ChatsContract.Presenter presenter;

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showGroups(List<Chat> groups) {
        String output = "";
        for (Chat c : groups) {
            output += "ID: " + c.getId() + "\n";
            output += "Name: " + c.getName() + "\n";
            output += "Preview: " + c.getPreview() + "\n";
            output += "Members:\n";
            for (Profile p : c.getMembers()) {
                output += "\t" + p.getName() + "\n";
            }
        }
        setText(output);
    }
}
