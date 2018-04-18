package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.messages.view.MessageListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesView extends VBox implements MessagesContract.View {
    private MessagesContract.Presenter presenter;
    private Text chatInfo;
    private ListView<Message> messageList;
    private Button refresh;

    public MessagesView() {
        chatInfo = new Text();
        messageList = new ListView<>();
        refresh = new Button("Refresh");
        refresh.setOnMouseClicked(event -> presenter.refreshMessages());
        getChildren().addAll(chatInfo, messageList, refresh);
    }

    @Override
    public void setPresenter(MessagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showMessages(List<Message> messages) {
        ObservableList<Message> obsMessageList = FXCollections.observableArrayList(messages);
        messageList.setItems(obsMessageList);
        messageList.setCellFactory(param -> new MessageListCell());
    }

    @Override
    public void showChatDetails(Chat chat) {
        String preview = "Name: " + chat.getName();
        preview += "\nPreview: " + chat.getPreview();
        preview += "\nMembers:";
        for (Profile p : chat.getMembers()) {
            preview += "\n\t" + p.getName();
        }
        chatInfo.setText(preview);
    }
}
