package catchat.messages.view;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
import catchat.messages.MessagesContract;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesView extends VBox implements MessagesContract.View {
    private MessagesContract.Presenter presenter;
    private ObservableList<Message> messageList;
    private Label chatTitle;
    private ComboBox<Profile> memberList;
    private ListView<Message> messageListView;
    private ScrollBar messageListScrollBar;
    private Button refresh;
    private TextField input;
    private Button sendMessage;

    public MessagesView() {
        super();
        getStylesheets().add("/messages/css/messages_view.css");
        getStyleClass().add("container");

        messageList = FXCollections.observableArrayList();

        chatTitle = new Label();
        chatTitle.getStyleClass().add("chat-title");

        refresh = new Button("↻");
        refresh.getStyleClass().add("refresh");
        refresh.setOnMouseClicked(event -> presenter.refreshMessages());

        memberList = new ComboBox<>();
        memberList.getStyleClass().add("member-list");
        memberList.setPromptText("Members");
        memberList.setEditable(false);
        memberList.setCellFactory(param -> new MemberListCell());
        memberList.setOnKeyPressed(event -> {});

        HBox chatTitleContainer = new HBox();
        chatTitleContainer.getStyleClass().add("chat-title-container");
        chatTitleContainer.getChildren().addAll(refresh, chatTitle);

        messageListView = new ListView<>(messageList);
        messageListView.getStyleClass().add("message-list");
        messageListView.setCellFactory(param -> new MessageListCell(presenter));

        input = new TextField();
        input.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                presenter.sendMessage();
            }
        });

        sendMessage = new Button("➤");
        sendMessage.getStyleClass().add("send");
        sendMessage.setOnMouseClicked(event -> presenter.sendMessage());

        HBox sendContainer = new HBox();
        sendContainer.getStyleClass().add("send-container");
        sendContainer.getChildren().addAll(input, sendMessage);
        HBox.setHgrow(input, Priority.ALWAYS);

        getChildren().addAll(chatTitleContainer, memberList, messageListView, sendContainer);
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
        chatTitle.setText("No Messages");
    }

    @Override
    public void clearMessages() {
        messageList.clear();
    }

    @Override
    public void clearMemberList() {
        memberList.getItems().clear();
    }

    @Override
    public void showChatDetails(Chat chat) {
        chatTitle.setText(chat.getName());
        refresh.setVisible(true);
        input.setVisible(true);
        sendMessage.setVisible(true);
    }

    @Override
    public void showNoChatSelected() {
        chatTitle.setText("No Chat Selected");
        memberList.setVisible(false);
        refresh.setVisible(false);
        input.setVisible(false);
        sendMessage.setVisible(false);
    }

    @Override
    public void showMembers(List<Profile> members) {
        memberList.setVisible(true);
        memberList.getItems().addAll(members);
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
                        PauseTransition pause = new PauseTransition(Duration.millis(250));
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
