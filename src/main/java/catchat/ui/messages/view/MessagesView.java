package catchat.ui.messages.view;

import catchat.data.source.entities.message.Message;
import catchat.ui.messages.MessagesContract;

import javafx.animation.PauseTransition;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
    private Label chatTitle;
    //private ComboBox<Profile> memberList;
    private ListView<Message> messageListView;
    private ScrollBar messageListScrollBar;
    private Button refresh;
    private TextField input;
    private Button sendMessage;

    public MessagesView() {
        super();
        getStylesheets().add("/messages/css/messages_view.css");
        getStyleClass().add("container");

        chatTitle = new Label();
        chatTitle.getStyleClass().add("chat-title");

        refresh = new Button("↻");
        refresh.getStyleClass().add("refresh");
        refresh.setOnMouseClicked(event -> presenter.reloadMessages());

        /*
        memberList = new ComboBox<>();
        memberList.getStyleClass().add("member-list");
        memberList.setPromptText("Members");
        memberList.setEditable(false);
        memberList.setCellFactory(param -> new MemberListCell());
        memberList.setOnKeyPressed(event -> {});
        */

        HBox chatTitleContainer = new HBox();
        chatTitleContainer.getStyleClass().add("chat-title-container");
        chatTitleContainer.getChildren().addAll(refresh, chatTitle);

        messageListView = new ListView<>();
        messageListView.getStyleClass().add("message-list");
        messageListView.setCellFactory(param -> new MessageListCell(presenter));

        input = new TextField();
        input.getStyleClass().add("input");
        input.setPromptText("New Message");
        input.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                presenter.sendMessage();
            }
        });

        sendMessage = new Button("➤");
        sendMessage.getStyleClass().add("send");
        sendMessage.setOnMouseClicked(event -> {
            presenter.sendMessage();
            input.requestFocus();
        });

        HBox sendContainer = new HBox();
        sendContainer.getStyleClass().add("send-container");
        sendContainer.getChildren().addAll(input, sendMessage);
        HBox.setHgrow(input, Priority.ALWAYS);

        getChildren().addAll(chatTitleContainer, messageListView, sendContainer);
        VBox.setVgrow(messageListView, Priority.ALWAYS);
    }

    @Override
    public void setPresenter(MessagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showChatPane() {
        setVisible(true);
        input.requestFocus();
    }

    @Override
    public void hideChatPane() {
        setVisible(false);
    }

    @Override
    public boolean chatPainVisible() {
        return isVisible();
    }

    @Override
    public void setMessages(List<Message> messages) {
        input.requestFocus();
        messageListView.getItems().addAll(0, messages);
        if (messageListScrollBar == null) {
            initializeMessageListScrollBar();
        }
    }

    @Override
    public int getMessagesSize() {
        return messageListView.getItems().size();
    }

    @Override
    public void clearMessages() {
        messageListView.getItems().clear();
    }

    @Override
    public void scrollMessagesTo(int index) {
        messageListView.scrollTo(index);
    }

    /*
    @Override
    public void clearMemberList() {
        memberList.getItems().clear();
    }
    */

    /*
    @Override
    public void showChatDetails(Chat chat) {
        chatTitle.setText(chat.getName());
        refresh.setVisible(true);
        input.setVisible(true);
        sendMessage.setVisible(true);
    }
    */

    /*
    @Override
    public void showMembers(List<Profile> members) {
        memberList.setVisible(true);
        memberList.getItems().addAll(members);
    }
    */

    @Override
    public String getMessageText() {
        return input.getText();
    }

    @Override
    public void clearMessageText() {
        input.clear();
    }

    @Override
    public void setChatTitle(String title) {
        chatTitle.setText(title);
    }

    private void initializeMessageListScrollBar() {
        for (Node node : messageListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
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
