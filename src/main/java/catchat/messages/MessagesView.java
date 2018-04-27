package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.messages.view.MessageListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesView extends VBox implements MessagesContract.View {
    private MessagesContract.Presenter presenter;
    private ObservableList<Message> messageList;
    private Label chatInfo;
    private ListView<Message> messageListView;
    private Button loadMore;
    private Button refresh;
    private TextField input;
    private Button sendMessage;

    public MessagesView() {
        super(5);
        messageList = FXCollections.observableArrayList();
        chatInfo = new Label();
        chatInfo.setFont(Font.font(null, FontWeight.BOLD, 14));
        HBox chatInfoBox = new HBox(5);
        chatInfoBox.getChildren().addAll(chatInfo);
        chatInfoBox.setAlignment(Pos.CENTER);

        messageListView = new ListView<>(messageList);
        messageListView.setFocusTraversable(false);
        messageListView.setCellFactory(param ->
                new MessageListCell(presenter));

        input = new TextField();
        loadMore = new Button("Load More");
        loadMore.setOnMouseClicked(event -> presenter.loadMoreMessages());
        refresh = new Button("Refresh");
        refresh.setOnMouseClicked(event -> presenter.refreshMessages());
        sendMessage = new Button("Send");
        sendMessage.setOnMouseClicked(event -> presenter.sendMessage());
        HBox sendBox = new HBox(2);
        sendBox.getChildren().addAll(loadMore, refresh, input, sendMessage);
        HBox.setHgrow(input, Priority.ALWAYS);

        getChildren().addAll(chatInfoBox, messageListView, sendBox);
        VBox.setVgrow(messageListView, Priority.ALWAYS);
    }

    @Override
    public void setPresenter(MessagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showMessages(List<Message> messages) {
        messageList.addAll(0, messages);
        messageListView.scrollTo(messages.size() - 1);
    }

    @Override
    public void showNoMessages() {
        chatInfo.setText("No Messages");
    }

    @Override
    public void clearMessages() {
        messageList.clear();
    }

    @Override
    public void showChatDetails(Chat chat) {
        chatInfo.setText(chat.getName());
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
    public void clearMessageText() {
        input.clear();
    }
}
