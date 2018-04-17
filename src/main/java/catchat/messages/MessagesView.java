package catchat.messages;

import catchat.data.entities.message.Message;
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
        setText("Test Message");
    }

    @Override
    public void showMessages(List<Message> messages) {
        setText("Test Call Received");
    }
}
