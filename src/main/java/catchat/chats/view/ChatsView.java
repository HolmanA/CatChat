package catchat.chats.view;

import catchat.chats.ChatsContract;
import catchat.data.entities.chat.Chat;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
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
    private ObservableList<Chat> chatList;
    private Label title;
    private ListView<Chat> chatListView;
    private ScrollBar chatListScrollBar;

    public ChatsView() {
        super();
        getStylesheets().add("/chats/css/chats_view.css");
        getStyleClass().add("container");

        chatList = FXCollections.observableArrayList();
        chatListView = new ListView<>();
        chatListView.getStyleClass().add("chat-list");
        chatListView.setItems(chatList);
        chatListView.setCellFactory(param -> new ChatListCell());
        chatListView.setOnMouseClicked(event ->
                presenter.loadChat(chatListView.getSelectionModel().getSelectedItem()));

        title = new Label();
        title.getStyleClass().add("title");

        Button refresh = new Button("\uD83D\uDD04");
        refresh.getStyleClass().add("refresh");
        refresh.setOnMouseClicked(event -> presenter.refreshChats());

        HBox titleContainer = new HBox();
        titleContainer.getStyleClass().add("title-container");
        titleContainer.getChildren().addAll(refresh, title);

        getChildren().addAll(titleContainer, chatListView);
        VBox.setVgrow(chatListView, Priority.ALWAYS);
    }

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showChats(List<Chat> chats) {
        chatList.addAll(chats);
        chatListView.scrollTo(chatList.size() - chats.size());
        if (chatListScrollBar == null) {
            initializeChatListScrollBar();
        }
    }

    @Override
    public void showNoChats() {
        title.setText("No Chats");
    }

    @Override
    public void clearChats() {
        chatList.clear();
    }

    @Override
    public void setTitle(String text) {
        title.setText(text);
    }

    private void initializeChatListScrollBar() {
        for (Node node : chatListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar)node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    chatListScrollBar = bar;
                    chatListScrollBar.valueProperty().addListener(((observable, oldValue, newValue) -> {
                        PauseTransition pause = new PauseTransition(Duration.millis(500));
                        pause.setOnFinished(event -> {
                            double position = newValue.doubleValue();
                            if (position == chatListScrollBar.getMin()) {
                                presenter.loadMoreChats();
                            }
                        });
                        pause.playFromStart();
                    }));
                }
            }
        }
    }
}
