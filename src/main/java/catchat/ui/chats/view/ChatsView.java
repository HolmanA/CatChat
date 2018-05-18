package catchat.ui.chats.view;

import catchat.data.entities.chat.Chat;
import catchat.ui.chats.ChatsContract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.PauseTransition;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class ChatsView extends VBox implements ChatsContract.View {
    private static final Logger log = LoggerFactory.getLogger(ChatsView.class);
    private ChatsContract.Presenter presenter;
    private Label groupChatsTitle;
    private Label directChatsTitle;
    private VBox groupChatsContainer;
    private VBox directChatsContainer;
    private ListView<Chat> groupChatListView;
    private ListView<Chat> directChatListView;
    private ScrollBar groupChatListScrollBar;
    private ScrollBar directChatListScrollBar;

    public ChatsView() {
        super();
        getStylesheets().add("/chats/css/chats_view.css");
        getStyleClass().add("container");

        groupChatsTitle = new Label("\u2BC6 Group Chats");
        groupChatsTitle.getStyleClass().add("title");

        HBox groupChatsTitleContainer = new HBox();
        groupChatsTitleContainer.getStyleClass().add("title-container");
        groupChatsTitleContainer.getChildren().add(groupChatsTitle);

        groupChatListView = new ListView<>();
        groupChatListView.getStyleClass().add("chat-list");
        groupChatListView.setCellFactory(param -> new ChatListCell(presenter));

        groupChatsContainer = new VBox();
        groupChatsContainer.getStyleClass().add("chat-container");
        groupChatsContainer.getChildren().addAll(groupChatsTitleContainer,
                new Separator(Orientation.HORIZONTAL), groupChatListView);
        groupChatsContainer.setOnMouseClicked(event -> {
            log.debug("Group Chats Title Selected");
            presenter.selectGroupChatsTitle();
        });

        directChatsTitle = new Label("\u2BC6 Direct Chats");
        directChatsTitle.getStyleClass().add("title");

        HBox directChatsTitleContainer = new HBox();
        directChatsTitleContainer.getStyleClass().add("title-container");
        directChatsTitleContainer.getChildren().add(directChatsTitle);

        directChatListView = new ListView<>();
        directChatListView.getStyleClass().add("chat-list");
        directChatListView.setCellFactory(param -> new ChatListCell(presenter));

        directChatsContainer = new VBox();
        directChatsContainer.getStyleClass().add("chat-container");
        directChatsContainer.getChildren().addAll(directChatsTitleContainer,
                new Separator(Orientation.HORIZONTAL), directChatListView);
        directChatsContainer.setOnMouseClicked(event -> {
            log.debug("Direct Chats Title Selected");
            presenter.selectDirectChatsTitle();
        });

        getChildren().addAll(groupChatsContainer, directChatsContainer);
        VBox.setVgrow(directChatListView, Priority.SOMETIMES);
        VBox.setVgrow(groupChatListView, Priority.SOMETIMES);
    }

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
        this.presenter = presenter;
        log.debug("Set Presenter: {}", presenter);
    }

    @Override
    public void setGroupChats(List<Chat> chats) {
        groupChatListView.getItems().addAll(chats);
        if (groupChatListScrollBar == null) {
            initializeGroupChatListScrollBars();
        }
        log.debug("Set Group Chats: {}", chats);
    }

    @Override
    public void setDirectChats(List<Chat> chats) {
        directChatListView.getItems().addAll(chats);
        if (directChatListScrollBar == null) {
            initializeDirectChatListScrollBars();
        }
        log.debug("Set Direct Chats: {}", chats);
    }

    @Override
    public int getGroupChatsSize() {
        return groupChatListView.getItems().size();
    }

    @Override
    public int getDirectChatsSize() {
        return directChatListView.getItems().size();
    }

    @Override
    public void clearGroupChatList() {
        groupChatListView.getItems().clear();
    }

    @Override
    public void clearDirectChatList() {
        directChatListView.getItems().clear();
    }

    @Override
    public void scrollGroupChatsTo(int index) {
        groupChatListView.scrollTo(index);
    }

    @Override
    public void scrollDirectChatsTo(int index) {
        directChatListView.scrollTo(index);
    }

    @Override
    public void hideGroupChats() {
        groupChatsContainer.getChildren().remove(groupChatListView);
        groupChatsTitle.setText("\u2BC8 Group Chats");
    }

    @Override
    public void showGroupChats() {
        groupChatsContainer.getChildren().add(groupChatListView);
        groupChatsTitle.setText("\u2BC6 Group Chats");
    }

    @Override
    public boolean groupChatsVisible() {
        return groupChatsContainer.getChildren().contains(groupChatListView);
    }

    @Override
    public void hideDirectChats() {
        directChatsContainer.getChildren().remove(directChatListView);
        directChatsTitle.setText("\u2BC8 Direct Chats");
    }

    @Override
    public void showDirectChats() {
        directChatsContainer.getChildren().add(directChatListView);
        directChatsTitle.setText("\u2BC6 Direct Chats");
    }

    @Override
    public boolean directChatsVisible() {
        return directChatsContainer.getChildren().contains(directChatListView);
    }

    @Override
    public void clearGroupChatSelection() {
        groupChatListView.getSelectionModel().clearSelection();
        log.debug("Group Chat Selection Cleared");
    }

    @Override
    public void clearDirectChatSelection() {
        directChatListView.getSelectionModel().clearSelection();
        log.debug("Direct Chat Selection Cleared");
    }

    private void initializeGroupChatListScrollBars() {
        log.debug("Initializing Group Chat List ScrollBar");
        for (Node node : groupChatListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    groupChatListScrollBar = bar;
                    groupChatListScrollBar.valueProperty().addListener(((observable, oldValue, newValue) -> {
                        PauseTransition pause = new PauseTransition(Duration.millis(500));
                        pause.setOnFinished(event -> {
                            double position = newValue.doubleValue();
                            if (position == groupChatListScrollBar.getMin()) {
                                presenter.loadMoreGroupChats();
                            }
                        });
                        pause.playFromStart();
                    }));
                }
            }
        }
    }

    private void initializeDirectChatListScrollBars() {
        log.debug("Initializing Direct Chat List ScrollBar");
        for (Node node : directChatListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    directChatListScrollBar = bar;
                    directChatListScrollBar.valueProperty().addListener(((observable, oldValue, newValue) -> {
                        PauseTransition pause = new PauseTransition(Duration.millis(500));
                        pause.setOnFinished(event -> {
                            double position = newValue.doubleValue();
                            if (position == directChatListScrollBar.getMin()) {
                                presenter.loadMoreDirectChats();
                            }
                        });
                        pause.playFromStart();
                    }));
                }
            }
        }
    }
}
