package catchat.chats;

import catchat.chats.view.ChatListCell;
import catchat.data.entities.chat.Chat;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
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
    private ListView<Chat> chatListView;
    private ScrollBar chatListScrollBar;
    private Label listTitle;

    public ChatsView() {
        super(5);
        chatList = FXCollections.observableArrayList();
        chatListView = new ListView<>();
        chatListView.setItems(chatList);
        chatListView.setCellFactory(param -> new ChatListCell());
        chatListView.setOnMouseClicked(event ->
                presenter.loadChat(chatListView.getSelectionModel().getSelectedItem()));

        listTitle = new Label();
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnMouseClicked(event -> presenter.refreshChats());

        getChildren().addAll(listTitle, chatListView, refreshButton);
        VBox.setVgrow(chatListView, Priority.ALWAYS);
        setAlignment(Pos.CENTER);
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
        listTitle.setText("No Chats");
    }

    @Override
    public void clearChats() {
        chatList.clear();
    }

    @Override
    public void setTitle(String text) {
        listTitle.setText(text);
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
