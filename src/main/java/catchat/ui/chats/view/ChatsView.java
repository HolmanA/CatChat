package catchat.ui.chats.view;

import catchat.ui.chats.ChatsContract;
import catchat.data.entities.chat.Chat;

import javafx.animation.PauseTransition;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
    private ChatsContract.Presenter presenter;
    private Label title;
    private ListView<Chat> groupChatListView;
    private ListView<Chat> directChatListView;
    private ScrollBar groupChatListScrollBar;
    private ScrollBar directChatListScrollBar;

    public ChatsView() {
        super();
        getStylesheets().add("/chats/css/chats_view.css");
        getStyleClass().add("container");

        title = new Label("Chats");
        title.getStyleClass().add("title");

        Button refresh = new Button("↻");
        refresh.getStyleClass().add("refresh");
        refresh.setOnMouseClicked(event -> {
            presenter.reloadGroupChats();
            presenter.reloadDirectChats();
        });

        HBox titleContainer = new HBox();
        titleContainer.getStyleClass().add("title-container");
        titleContainer.getChildren().addAll(refresh, title);

        groupChatListView = new ListView<>();
        groupChatListView.getStyleClass().add("chat-list");
        groupChatListView.setCellFactory(param -> new ChatListCell());
        groupChatListView.setOnMouseClicked(event ->
                presenter.selectChat(groupChatListView.getSelectionModel().getSelectedItem()));

        directChatListView = new ListView<>();
        directChatListView.getStyleClass().add("chat-list");
        directChatListView.setCellFactory(param -> new ChatListCell());
        directChatListView.setOnMouseClicked(event ->
                presenter.selectChat(directChatListView.getSelectionModel().getSelectedItem()));

        getChildren().addAll(titleContainer, new Separator(Orientation.HORIZONTAL),
                groupChatListView, new Separator(Orientation.HORIZONTAL), directChatListView);
        VBox.setVgrow(directChatListView, Priority.ALWAYS);
        VBox.setVgrow(groupChatListView, Priority.ALWAYS);
    }

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setGroupChats(List<Chat> chats) {
        groupChatListView.getItems().addAll(chats);
        if (groupChatListScrollBar == null) {
            initializeGroupChatListScrollBars();
        }
    }

    @Override
    public void setDirectChats(List<Chat> chats) {
        directChatListView.getItems().addAll(chats);
        if (directChatListScrollBar == null) {
            initializeDirectChatListScrollBars();
        }
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

    private void initializeGroupChatListScrollBars() {
        for (Node node : groupChatListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar)node;
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
        for (Node node : directChatListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar)node;
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
