package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesView extends Text implements MessagesContract.View {
    private MessagesContract.Presenter presenter;

    @Override
    public void setPresenter(MessagesContract.Presenter presenter) {
        this.presenter = presenter;
        setText("No Chats Selected");
    }

    @Override
    public void showMessages(List<Message> messages) {
    }

    @Override
    public void showChatDetails(Chat chat) {
        String preview = "Name: " + chat.getName();
        preview += "\nPreview: " + chat.getPreview();
        preview += "\nMembers:";
        for (Profile p : chat.getMembers()) {
            preview += "\n\t" + p.getName();
        }
        setText(preview);
    }
}
