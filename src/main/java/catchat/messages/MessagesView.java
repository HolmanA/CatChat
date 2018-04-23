package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.messages.view.MessageListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    private TextField input;
    private Button sendMessage;

    public MessagesView() {
        chatInfo = new Text();
        messageList = new ListView<>();
        messageList.setFocusTraversable(false);
        refresh = new Button("Refresh");
        refresh.setOnMouseClicked(event -> presenter.refreshMessages());
        input = new TextField();
        sendMessage = new Button("Send");
        sendMessage.setOnMouseClicked(event -> presenter.sendMessage());
        HBox sendBox = new HBox();
        sendBox.getChildren().addAll(input, sendMessage);
        getChildren().addAll(chatInfo, messageList, refresh, sendBox);
    }

    @Override
    public void setPresenter(MessagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showMessages(List<Message> messages) {
        ObservableList<Message> obsMessageList = FXCollections.observableArrayList(messages);
        messageList.setItems(obsMessageList);
        messageList.setCellFactory(param ->
                new MessageListCell(presenter));
        messageList.scrollTo(messages.size() - 1);
    }

    @Override
    public void showNoMessages() {
        chatInfo.setText("No Messages");
    }

    @Override
    public void showChatDetails(Chat chat) {
        String preview = "Name: " + chat.getName();
        preview += "\nMembers:";
        for (Profile p : chat.getMembers()) {
            preview += "\n\t" + p.getName();
        }
        chatInfo.setText(preview);
    }

    @Override
    public void showNoChatSelected() {
        chatInfo.setText("No Chat Selected");
    }

    @Override
    public String getMessageText() {
        return input.getText();
    }

    @Override
    public Message getFocusedMessage() {
        return messageList.getFocusModel().getFocusedItem();
    }

    @Override
    public void clearMessageText() {
        input.clear();
    }
}
