package catchat.messages;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.messages.view.MessageListCell;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesView extends VBox implements MessagesContract.View {
    private MessagesContract.Presenter presenter;
    private ObservableList<Message> messageList;
    private Label chatInfo;
    private ListView<Message> messageListView;
    private ScrollBar messageListScrollBar;
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
        refresh = new Button("Refresh");
        refresh.setOnMouseClicked(event -> presenter.refreshMessages());
        sendMessage = new Button("Send");
        sendMessage.setOnMouseClicked(event -> presenter.sendMessage());
        HBox sendBox = new HBox(2);
        sendBox.getChildren().addAll(refresh, input, sendMessage);
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
        messageListView.scrollTo(messages.size());
        if (messageListScrollBar == null) {
            initializeMessageListScrollBar();
        }
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

    private void initializeMessageListScrollBar() {
        for (Node node : messageListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar)node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    messageListScrollBar = bar;
                    messageListScrollBar.valueProperty().addListener(((observable, oldValue, newValue) -> {
                        PauseTransition pause = new PauseTransition(Duration.millis(500));
                        pause.setOnFinished(event -> {
                            double position = newValue.doubleValue();
                            if (position == messageListScrollBar.getMin()) {
                                presenter.loadMoreMessages();
                            }
                        });
                        pause.playFromStart();
                    }));
                }
            }
        }
    }
}
